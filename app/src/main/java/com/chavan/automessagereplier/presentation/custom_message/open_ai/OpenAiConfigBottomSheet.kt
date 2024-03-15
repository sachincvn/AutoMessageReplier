package com.chavan.automessagereplier.presentation.custom_message.open_ai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chavan.automessagereplier.data.local.open_ai.OpenAiModelEnum
import com.chavan.automessagereplier.domain.model.openai.OpenAiConfig
import com.chavan.automessagereplier.presentation.UiEvent
import com.chavan.automessagereplier.presentation.custom_message.components.OutlinedTextFieldWithDropdown
import com.chavan.automessagereplier.presentation.custom_message.components.UpsertTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAiConfigBottomSheet(
    onDismiss: (Boolean) -> Unit,
    snackBarHostState: SnackbarHostState,
    openAiConfig: OpenAiConfig? = null,
    openAiConfigViewModel: OpenAiConfigViewModel = hiltViewModel()
) {

    val state = openAiConfigViewModel.state.value

    val modalBottomSheetState = rememberModalBottomSheetState()

    if (openAiConfig!=null){
        state.openAiApiKey.value = openAiConfig.openAiApiKey ?: ""
        state.openAiModel.value = openAiConfig.openAiModel ?: OpenAiModelEnum.GPT_3_5_TURBO
        state.temperature.value = openAiConfig.temperature ?: 0.7
        state.errorMessage.value = openAiConfig.errorMessage ?: ""
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (state.isConfigSuccessFully.value) {
                onDismiss(state.isConfigSuccessFully.value)
            }
            else{
                onDismiss(false)
            }
                           },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        if (openAiConfigViewModel.state.value.isConfigSuccessFully.value) {
            onDismiss(true)
            state.isConfigSuccessFully.value = false
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            UpsertTextField(value = state.openAiApiKey.value,
                onTextChanged = {
                                state.openAiApiKey.value = it
                },
                label = "Api key")
            OutlinedTextFieldWithDropdown(
                selectedOption = OpenAiModelEnum.GPT_3_5_TURBO,
                onOptionSelected = {
                    state.openAiModel.value = it
                },
                label = "Model",
                leadingIcon = Icons.Default.ArrowDropDown
            )
            UpsertTextField(value = state.temperature.value.toString(),
                onTextChanged = {
                    state.temperature.value = it.toDouble()
                                },
                label = "Temperature",
                keyboardType = KeyboardType.Number)
            UpsertTextField(value = state.errorMessage.value,
                onTextChanged = {
                                state.errorMessage.value = it
                },
                label = "Error reply")
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    openAiConfigViewModel.onEvent(OpenAiConfigEvents.UpsertOpenAiConfig)
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }
    }
    LaunchedEffect(true) {
        openAiConfigViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }
    }
}