package com.chavan.automessagereplier.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.presentation.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigator: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Theme Settings Section
            SettingsSection(title = "Appearance") {
                SettingsSwitchItem(
                    title = "Dark Mode",
                    description = "Enable dark theme appearance",
                    icon = Icons.Default.DarkMode,
                    checked = state.isDarkMode,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsEvent.ToggleDarkMode(it))
                    }
                )
            }

            // Notification Settings Section
            SettingsSection(title = "Notifications") {
                SettingsSwitchItem(
                    title = "Auto Reply",
                    description = "Automatically reply to incoming messages",
                    icon = Icons.Default.Reply,
                    checked = state.isAutoReplyEnabled,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsEvent.ToggleAutoReply(it))
                    }
                )

                SettingsSwitchItem(
                    title = "Notification Sound",
                    description = "Play sound for notifications",
                    icon = Icons.Default.Notifications,
                    checked = state.isNotificationSoundEnabled,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsEvent.ToggleNotificationSound(it))
                    }
                )
            }

            // General Settings Section
            SettingsSection(title = "General") {
                SettingsClickableItem(
                    title = "Share App", description = "Share this app with friends", icon = Icons.Default.Share
                ) {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out Auto Message Replier: https://play.google.com/store/apps/details?id=${context.packageName}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                SettingsClickableItem(
                    title = "Rate App", description = "Rate us on Play Store", icon = Icons.Default.Star
                ) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("market://details?id=${context.packageName}")
                    }
                    context.startActivity(intent)
                }

                SettingsClickableItem(
                    title = "Privacy Policy", description = "Read our privacy policy", icon = Icons.Default.Security
                ) {
                    // Add your privacy policy URL here
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yourapp.com/privacy"))
                    context.startActivity(intent)
                }

                SettingsClickableItem(
                    title = "About", description = "About Auto Message Replier", icon = Icons.Default.Info
                ) {
                    // Navigate to About screen or show dialog
                }
            }

            // App Info at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Auto Message Replier", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Version 1.0.0", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String, content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSwitchItem(
    title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    ListItem(headlineContent = { Text(title) }, supportingContent = { Text(description) }, leadingContent = {
        Icon(
            imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }, trailingContent = {
        Switch(
            checked = checked, onCheckedChange = onCheckedChange
        )
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsClickableItem(
    title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit
) {
    ListItem(headlineContent = { Text(title) }, supportingContent = { Text(description) }, leadingContent = {
        Icon(
            imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }, modifier = Modifier.clickable { onClick() })
}