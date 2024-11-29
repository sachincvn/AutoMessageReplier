package com.chavan.automessagereplier.core.utils

sealed class NavigationScreen(val route: String) {
    object HomeScreen : NavigationScreen("HomeScreen")
    object NotificationPermissionScreen : NavigationScreen("NotificationPermissionScreen")
    object UpsertCustomMessageScreen : NavigationScreen("UpsertCustomMessageScreen")
    object SettingsScreen : NavigationScreen("SettingsScreen")
    object ClickToChatScreen : NavigationScreen("ClickToChatScreen")
}