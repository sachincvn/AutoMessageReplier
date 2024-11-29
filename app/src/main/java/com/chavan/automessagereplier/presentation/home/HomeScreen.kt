package com.chavan.automessagereplier.presentation.home

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chavan.automessagereplier.R
import com.chavan.automessagereplier.core.utils.NavigationScreen
import com.chavan.automessagereplier.notification_service.NotificationUtils
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.home.components.AppTopAppBar
import com.chavan.automessagereplier.presentation.home.components.CustomMessageItem
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigator: NavController, homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.state.value
    val snackBarHostState = remember { SnackbarHostState() }
    val emptyLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hello_lottie_1))

    val navigationState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val showPermissionDialog = remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (NotificationUtils.isNotificationAccessGranted(context,context.packageName)) {
            showPermissionDialog.value = false
            homeViewModel.onEvent(HomeScreenEvents.ToggleAutoReplier(true))
        } else {
            Toast.makeText(context,"Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = snackBarHostState, modifier = Modifier.padding(16.dp)
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navigator.navigate(NavigationScreen.UpsertCustomMessageScreen.route) }) {
            Icon(
                Icons.Default.ChatBubbleOutline,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }, topBar = { AppTopAppBar(scope, navigationState, homeViewModel, state) }) {

        if (state.isLoading) {
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
        if (state.errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = state.errorMessage,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
        }

        if (!state.isLoading && state.errorMessage == null) {
            Box(
                modifier = Modifier.padding(top = 60.dp, bottom = 15.dp)
            ) {
                if (state.customMessages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 30.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieAnimation(
                            modifier = Modifier
                                .fillMaxHeight(.4f)
                                .background(Color.Transparent),
                            composition = emptyLottie,
                            iterations = LottieConstants.IterateForever,
                        )

                        Text(
                            text = "No data found, add your custom message replies!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn {
                        items(items = state.customMessages, key = { it.id }) { customMessage ->
                            CustomMessageItem(customMessage = customMessage, navigateToDetail = {
                                navigator.navigate("${NavigationScreen.UpsertCustomMessageScreen.route}?customMessageId=${customMessage.id}")
                            }, toggleActive = {
                                homeViewModel.onEvent(
                                    HomeScreenEvents.ToggleActive(
                                        customMessage
                                    )
                                )
                            }, toggleDelete = {
                                homeViewModel.onEvent(
                                    HomeScreenEvents.DeleteCustomMessage(customMessage.id)
                                )
                            })
                        }
                    }
                }
            }
        }
    }

    if (showPermissionDialog.value) {
        AlertDialog(onDismissRequest = {showPermissionDialog.value = false },
            title = { Text(text = "Notification Access Required") },
            text = { Text("Please grant notification access to enable auto messaging.") },
            confirmButton = {
                Button(onClick = {
                    requestPermissionLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showPermissionDialog.value = false
                }) {
                    Text("Cancel")
                }
            })
    }
    LaunchedEffect(true) {
        homeViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message, duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    LaunchedEffect(true) {
        homeViewModel.onEvent(HomeScreenEvents.Refresh)
    }
    LaunchedEffect(true) {
        homeViewModel.homeEvent.collectLatest {
            when (it) {
                is HomeEvent.RequestNotificationAccess -> {
                    showPermissionDialog.value = true
                }
            }
        }
    }
}