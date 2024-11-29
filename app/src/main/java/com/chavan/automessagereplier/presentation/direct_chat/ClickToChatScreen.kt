package com.chavan.automessagereplier.presentation.direct_chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chavan.automessagereplier.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClickToChatScreen(navigator: NavController) {
    val context = LocalContext.current
    val emptyLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chat_lottie_1))

    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("91") }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Click To Chat", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .navigationBarsPadding()
                .imePadding(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .statusBarsPadding()
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    composition = emptyLottie,
                    iterations = LottieConstants.IterateForever,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Send WhatsApp Messages Without Saving Numbers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enter any phone number and message to start a WhatsApp chat instantly, without having to save the contact first. Perfect for quick conversations and business communications.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Number Row with Country Code
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Editable Country Code Field
                OutlinedTextField(value = countryCode, onValueChange = { newValue ->
                    // Only allow numbers and limit length
                    if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                        countryCode = newValue
                    }
                }, modifier = Modifier.width(100.dp), label = { Text("Code") }, prefix = { Text("+") }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ), singleLine = true
                )

                // Phone Number Field
                OutlinedTextField(value = phoneNumber, onValueChange = { if (it.length <= 10) phoneNumber = it }, modifier = Modifier.weight(1f), label = { Text("Phone Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Message Field
            OutlinedTextField(value = message, onValueChange = { message = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Message") }, minLines = 3, maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Send Button
            Button(
                onClick = {
                    if (phoneNumber.isBlank()) {
                        Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (countryCode.isBlank()) {
                        Toast.makeText(context, "Please enter country code", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    try {
                        val url = "https://api.whatsapp.com/send?phone=$countryCode$phoneNumber&text=${Uri.encode(message)}"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send on WhatsApp")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}