package com.chavan.automessagereplier.presentation.home.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.chavan.automessagereplier.presentation.home.HomeScreenEvents
import com.chavan.automessagereplier.presentation.home.HomeScreenState
import com.chavan.automessagereplier.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    scope: CoroutineScope,
    navigationState: DrawerState,
    homeViewModel: HomeViewModel,
    state: HomeScreenState
) {
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
                IconButton(onClick = {
                    scope.launch {
                        navigationState.open()
                    }
                }) {
                    IconButton(onClick = {
                        scope.launch {
                            navigationState.open()
                        }
                    }) {
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
}