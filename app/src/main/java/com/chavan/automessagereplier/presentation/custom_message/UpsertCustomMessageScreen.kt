package com.chavan.automessagereplier.presentation.custom_message

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.domain.model.CustomMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun  UpsertCustomMessageScreen(
    navigator: NavController,
    upsertCustomMessageViewModel: UpsertCustomMessageViewModel = hiltViewModel()
) {
    val state by upsertCustomMessageViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    upsertCustomMessageViewModel.onEvent(UpsertCustomMessageEvents.UpsertCustomMessage(
                        customMessage = CustomMessage(
                            receivedMessage = state.receivedMessage.value,
                            replyMessage = state.receivedMessage.value
                        )
                    ))
                }
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Icon(imageVector = Icons.Rounded.Check, contentDescription = "Save Custom Message")
                }
            }
        }
    ) { paddingValues ->
            if (state.error != null) {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar(state.error!!)
                }
            }
            if (state.isCustomMessageAdded) {
                navigator.navigateUp()
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.receivedMessage.value,
                    onValueChange = {
                        state.receivedMessage.value = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                    placeholder = {
                        Text(text = "Received Message")
                    }
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.replyMessage.value,
                    onValueChange = {
                        state.replyMessage.value = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                    placeholder = {
                        Text(text = "Reply Message")
                    }
                )
            }
    }
}
