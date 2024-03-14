package com.chavan.automessagereplier.presentation.custom_message

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.custom_message.components.FieldWrapper
import com.chavan.automessagereplier.presentation.custom_message.components.MultipleContactPicker
import com.chavan.automessagereplier.presentation.custom_message.components.OpenAiSettingBottomSheet
import com.chavan.automessagereplier.presentation.custom_message.components.UpsertTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpsertCustomMessageScreen(
    navigator: NavController,
    upsertCustomMessageViewModel: UpsertCustomMessageViewModel = hiltViewModel()
) {
    val state = upsertCustomMessageViewModel.state.value
    val snackBarHostState = remember { SnackbarHostState() }

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        OpenAiSettingBottomSheet() {
            showSheet = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp)
            )
        },
        topBar = { TopAppBar(navigator = navigator, viewModel = upsertCustomMessageViewModel) },
        bottomBar = {
            Button(
                onClick = {
                    upsertCustomMessageViewModel.onEvent(
                        UpsertCustomMessageEvents.UpsertCustomMessage(
                            customMessage = CustomMessage(
                                receivedMessage = state.receivedMessage.value.trim(),
                                replyMessage = state.replyMessage.value.trim(),
                                isActive = state.isActive.value,
                                replyToOption = state.replyToOption.value,
                                receivedPattern = state.receivedPattern.value,
                                selectedContacts = state.selectedContacts.value,
                                replyWithChatGptApi = state.replyWithChatGPT.value
                            )
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp, top = 5.dp)
            ) {
                Text(text = "Save")
            }
        },
    ) {
        if (state.isCustomMessageAdded) {
            navigator.navigateUp()
        }

        Box(
            Modifier
                .padding(top = 50.dp, bottom = 70.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .padding(top = 10.dp)
                    .fillMaxSize()
            ) {
                item {
                    Column {
                        FieldWrapper {
                            UpsertTextField(
                                value = state.receivedMessage.value,
                                onTextChanged = {
                                    state.receivedMessage.value = it
                                    upsertCustomMessageViewModel.onEvent(
                                        UpsertCustomMessageEvents.OnReceivedMessageChange(
                                            it
                                        )
                                    )
                                },
                                label = "Received Message",
                                placeholder = "Enter received message",
                            )


                            upsertCustomMessageViewModel.receivedPattern.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = (option == state.receivedPattern.value),
                                            onClick = {
                                                upsertCustomMessageViewModel.onEvent(
                                                    UpsertCustomMessageEvents.OnReceivedPatterChange(
                                                        option
                                                    )
                                                )
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (option == state.receivedPattern.value),
                                        onClick = {
                                            state.receivedPattern.value = option
                                            upsertCustomMessageViewModel.onEvent(
                                                UpsertCustomMessageEvents.OnReceivedPatterChange(
                                                    option
                                                )
                                            )
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                    )

                                    Text(
                                        text = option.value!!,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        FieldWrapper {
                            UpsertTextField(
                                value = state.replyMessage.value,
                                onTextChanged = {
                                    state.replyMessage.value = it
                                },
                                label = "Reply Message",
                                placeholder = if (!state.replyWithChatGPT.value) "Enter reply message" else "Message will be auto generated",
                                isEnabled = !state.replyWithChatGPT.value
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        state.replyMessage.value = ""
                                        state.replyWithChatGPT.value = !state.replyWithChatGPT.value
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Checkbox(
                                    checked = state.replyWithChatGPT.value,
                                    onCheckedChange = {
                                        state.replyWithChatGPT.value = it
                                    }
                                )
                                Text(
                                    text = "Reply with chat gpt",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp).weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        showSheet = true
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Settings,
                                        contentDescription = "open ai setting"
                                    )
                                }
                            }
                            upsertCustomMessageViewModel.replyToOption.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = (option == state.replyToOption.value),
                                            onClick = { state.replyToOption.value = option }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (option == state.replyToOption.value),
                                        onClick = { state.replyToOption.value = option },
                                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                    )

                                    Text(
                                        text = option.value!!,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }

                        if (state.replyToOption.value == ReplyToOption.SpecificContacts) {
                            Spacer(modifier = Modifier.height(20.dp))
                            FieldWrapper {
                                var selectedContacts by remember {
                                    mutableStateOf<List<String>>(
                                        emptyList()
                                    )
                                }

                                UpsertTextField(
                                    value = "",
                                    onTextChanged = {
                                        state.replyMessage.value = ""
                                    },
                                    label = "Select contacts",
                                    placeholder = "Add contact names",
                                )

                                MultipleContactPicker(
                                    onContactsPicked = { contacts ->
                                        selectedContacts = contacts
                                        state.selectedContacts.value = selectedContacts
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        upsertCustomMessageViewModel.uiEvent.collectLatest { event ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    navigator: NavController,
    viewModel: UpsertCustomMessageViewModel
) {
    val state = viewModel.state.value
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Add custom reply",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            Switch(
                checked = state.isActive.value,
                onCheckedChange = {
                    state.isActive.value = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                thumbContent = if (state.isActive.value) {
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