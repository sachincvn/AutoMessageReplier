package com.chavan.automessagereplier.presentation.home

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chavan.automessagereplier.R
import com.chavan.automessagereplier.core.utils.NavigationScreen
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.home.components.CustomMessageItem
import com.chavan.automessagereplier.presentation.home.components.DrawerModalDrawerSheet
import com.chavan.automessagereplier.presentation.home.components.TopAppBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigator: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.state.value
    val snackBarHostState = remember { SnackbarHostState() }
    val emptyLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_lottie))

    val navigationState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerModalDrawerSheet(scope,navigationState)
        },
        drawerState = navigationState,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.navigate(NavigationScreen.UpsertCustomMessageScreen.route) }
                ) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            topBar = { TopAppBar(scope,navigationState,homeViewModel,state) }
        ) {

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
                        text = state.errorMessage ?: "Something went wrong",
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            LottieAnimation(
                                modifier = Modifier.align(Alignment.Center),
                                composition = emptyLottie,
                                iterations = LottieConstants.IterateForever,
                            )
                        }
                    } else {
                        LazyColumn {
                            items(items = state.customMessages, key = { it.id }) { customMessage ->
                                CustomMessageItem(
                                    customMessage = customMessage,
                                    navigateToDetail = {
                                        navigator.navigate("${NavigationScreen.UpsertCustomMessageScreen.route}?customMessageId=${customMessage.id}")
                                    },
                                    toggleActive = {
                                        homeViewModel.onEvent(
                                            HomeScreenEvents.ToggleActive(
                                                customMessage
                                            )
                                        )
                                    },
                                    toggleDelete = {
                                        homeViewModel.onEvent(
                                            HomeScreenEvents.DeleteCustomMessage(customMessage.id)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    }
    LaunchedEffect(key1 = true) {
        homeViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    LaunchedEffect(true) {
        homeViewModel.onEvent(HomeScreenEvents.Refresh)
    }
}