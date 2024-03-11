package com.chavan.automessagereplier.presentation.custom_message.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chavan.automessagereplier.ui.theme.MontserratFontFamily

@Composable
fun UpsertTextField(
    value: String,
    onTextChanged : (String) -> Unit,
    modifier: Modifier = Modifier,
    label : String = "Name",
    placeholder: String = "Enter $label"
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = {onTextChanged(it)},
        singleLine = true,
        visualTransformation = VisualTransformation.None,
        modifier = Modifier.height(60.dp)
            .fillMaxWidth()
            .padding(top = 3.dp, bottom = 5.dp),
        textStyle = TextStyle(
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}