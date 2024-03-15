package com.chavan.automessagereplier.presentation.custom_message.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chavan.automessagereplier.data.local.open_ai.OpenAiModelEnum

@Composable
fun OutlinedTextFieldWithDropdown(
    selectedOption: OpenAiModelEnum,
    onOptionSelected: (OpenAiModelEnum) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
    )
    OutlinedTextField(
        value = selectedOption.value!!,
        onValueChange = { /* Handle value change if needed */ },
        trailingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Dropdown Icon",
                modifier = Modifier.clickable { expanded = !expanded }
            )
        },
        modifier = modifier.clickable(
            onClick = { expanded = !expanded }
        )
            .fillMaxWidth(),
        readOnly = true,
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()

    ) {
        OpenAiModelEnum.values().toList().forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                },
                text = {
                Text(text = option.value!!, fontSize = 16.sp)
            })
        }
    }
}