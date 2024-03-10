package com.chavan.automessagereplier.presentation.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.home.components.CustomMessageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigator: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp)
            ) },
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

    LaunchedEffect(key1 = true) {
        homeViewModel.getAllCustomMessages()
        homeViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}