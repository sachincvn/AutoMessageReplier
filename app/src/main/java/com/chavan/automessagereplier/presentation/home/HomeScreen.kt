package com.chavan.automessagereplier.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.home.components.ReplyEmailListItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigator: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.state.value

    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigator.navigate("AddCustomMessage") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "AutoMessenger",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                actions = {
                    Switch(
                        checked = state.isAutoMessagingActive,
                        onCheckedChange = {
                            homeViewModel.onEvent(HomeScreenEvents.ToggleAutoReplier(it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        thumbContent = if (state.isAutoMessagingActive) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            )
        },
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
                        Text(
                            text = "No item found",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center),
                        )
                    }
                } else {
                    LazyColumn {
                        items(items = state.customMessages, key = { it.id }) { customMessage ->
                            ReplyEmailListItem(
                                customMessage = customMessage,
                                navigateToDetail = {},
                                toggleSelection = {
                                    homeViewModel.onEvent(
                                        HomeScreenEvents.ToggleActive(
                                            customMessage
                                        )
                                    )
                                },
                            )
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