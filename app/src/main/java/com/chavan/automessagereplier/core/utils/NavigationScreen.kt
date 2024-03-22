package com.chavan.automessagereplier.core.utils

sealed class NavigationScreen(val route: String) {
    object UpsertCustomMessageScreen: NavigationScreen("UpsertCustomMessageScreen")
    object HomeScreen: NavigationScreen("HomeScreen")
    object NotificationPermissionScreen: NavigationScreen("NotificationPermissionScreen")
}