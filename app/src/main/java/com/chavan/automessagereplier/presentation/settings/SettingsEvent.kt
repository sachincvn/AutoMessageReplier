package com.chavan.automessagereplier.presentation.settings

sealed class SettingsEvent {
    data class ToggleDarkMode(val enabled: Boolean) : SettingsEvent()
    data class ToggleAutoReply(val enabled: Boolean) : SettingsEvent()
    data class ToggleNotificationSound(val enabled: Boolean) : SettingsEvent()
}
