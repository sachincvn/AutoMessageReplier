package com.chavan.automessagereplier.presentation.settings

data class SettingsState(
    val isDarkMode: Boolean = false,
    val isAutoReplyEnabled: Boolean = false,
    val isNotificationSoundEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)