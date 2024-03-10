package com.chavan.automessagereplier.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.presentation.home.components.CustomMessageItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigator: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate("AddCustomMessage") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Auto Messenger")
                }
            )},
        ){
        when (val result = state.result) {
            is ScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            is ScreenState.Success -> {
                Box(
                    modifier = Modifier.padding(top = 50.dp, bottom = 15.dp)
                ) {
                    LazyColumn {
                        items(result.data) { customMessage ->
                            CustomMessageItem(
                                customMessage = customMessage,
                                onCheckBoxClick = {
                                    homeViewModel.onEvent(HomeScreenEvents.ToggleActive(customMessage))
                                },
                                isCheckboxActive = customMessage.isActive,
                                onDeleteClick = {
                                    homeViewModel.onEvent(HomeScreenEvents.DeleteCustomMessage(customMessage.id))
                                }
                            )
                        }
                    }
                }
            }
            is ScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = result.message ?: "Something went wrong",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.getAllCustomMessages()
    }
}