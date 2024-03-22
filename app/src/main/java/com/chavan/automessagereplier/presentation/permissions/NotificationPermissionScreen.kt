package com.chavan.automessagereplier.presentation.permissions

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chavan.automessagereplier.R
import com.chavan.automessagereplier.core.utils.NavigationScreen
import com.chavan.automessagereplier.notification_service.NotificationUtils


@Composable
fun NotificationPermissionScreen(
    navigator: NavController,
) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (NotificationUtils.isNotificationAccessGranted(context,"com.chavan.automessagereplier")) {
            navigator.navigate(NavigationScreen.HomeScreen.route) {
                popUpTo(NavigationScreen.NotificationPermissionScreen.route) { inclusive = true }
            }
        } else {
            // Handle permission denial
        }
    }

    if (NotificationUtils.isNotificationAccessGranted(context,"com.chavan.automessagereplier")) {
        navigator.navigate(NavigationScreen.HomeScreen.route) {
            popUpTo(NavigationScreen.NotificationPermissionScreen.route) { inclusive = true }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            MaterialTheme.colorScheme.background,
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50))
                    .background(color = MaterialTheme.colorScheme.onError.copy(alpha = .1f))
                    .padding(top = 30.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notification_image),
                    contentDescription = "Notification Permission Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(350.dp),
                    alignment = Alignment.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Enable Notifications",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "The app requires notification permission access. please allow the permission to activate the auto message replier. ",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 30.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        requestPermissionLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
                    modifier = Modifier
                        .padding(10.dp)
                        .height(50.dp)
                        .width(160.dp)
                ) {
                    Text(text = "Allow")
                }
                Text(
                    text = "May be later",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .clickable {
                            navigator.navigate(NavigationScreen.HomeScreen.route) {
                                popUpTo(NavigationScreen.NotificationPermissionScreen.route) { inclusive = true }
                            }
                        }
                )
            }
        }
    }
}
