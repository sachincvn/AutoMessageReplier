package com.chavan.automessagereplier.presentation.settings

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chavan.automessagereplier.notification_service.NotificationUtils
import com.chavan.automessagereplier.presentation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val AUTO_REPLY_KEY = booleanPreferencesKey("auto_reply")
        private val NOTIFICATION_SOUND_KEY = booleanPreferencesKey("notification_sound")
    }

    init {
        loadSettings()
    }

    private fun isSystemInDarkTheme(): Boolean {
        return when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            context.dataStore.data
                .catch {
                    emit(emptyPreferences())
                }
                .collect { preferences ->
                    val darkMode = preferences[DARK_MODE_KEY] ?: isSystemInDarkTheme()
                    _isDarkMode.value = darkMode
                    _state.value = _state.value.copy(
                        isDarkMode = darkMode,
                        isAutoReplyEnabled = preferences[AUTO_REPLY_KEY] ?: false,
                        isNotificationSoundEnabled = preferences[NOTIFICATION_SOUND_KEY] ?: true
                    )
                }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleDarkMode -> {
                updateDarkMode(event.enabled)
            }
            is SettingsEvent.ToggleAutoReply -> {
                toggleAutoReply(event.enabled)
            }
            is SettingsEvent.ToggleNotificationSound -> {
                updateNotificationSound(event.enabled)
            }
        }
    }

    private fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            try {
                context.dataStore.edit { preferences ->
                    preferences[DARK_MODE_KEY] = enabled
                }
                _isDarkMode.value = enabled
                _state.value = _state.value.copy(isDarkMode = enabled)
                _uiEvent.emit(UiEvent.ShowSnackbar("Theme updated"))
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to update theme"))
            }
        }
    }

    private fun toggleAutoReply(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled && !NotificationUtils.isNotificationAccessGranted(context, context.packageName)) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Please grant notification access"))
                return@launch
            }

            try {
                context.dataStore.edit { preferences ->
                    preferences[AUTO_REPLY_KEY] = enabled
                }
                _state.value = _state.value.copy(isAutoReplyEnabled = enabled)
                _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        if (enabled) "Auto reply enabled" else "Auto reply disabled"
                    )
                )
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to update auto reply setting"))
            }
        }
    }

    private fun updateNotificationSound(enabled: Boolean) {
        viewModelScope.launch {
            try {
                context.dataStore.edit { preferences ->
                    preferences[NOTIFICATION_SOUND_KEY] = enabled
                }
                _state.value = _state.value.copy(isNotificationSoundEnabled = enabled)
                _uiEvent.emit(
                    UiEvent.ShowSnackbar(
                        if (enabled) "Notification sound enabled" else "Notification sound disabled"
                    )
                )
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowSnackbar("Failed to update notification sound setting"))
            }
        }
    }
}