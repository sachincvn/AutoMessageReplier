package com.chavan.automessagereplier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chavan.automessagereplier.notification_service.NotificationUtils
import com.chavan.automessagereplier.presentation.custom_message.UpsertCustomMessageScreen
import com.chavan.automessagereplier.presentation.home.HomeScreen
import com.chavan.automessagereplier.ui.theme.AutoMessageReplierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationUtils.requestNotificationPermission(this, packageName)
        super.onCreate(savedInstanceState)
        setContent {
            AutoMessageReplierTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "HomeScreen") {
                        composable("HomeScreen") {
                            HomeScreen(navigator = navController)
                        }
                        composable("AddCustomMessage") {
                            UpsertCustomMessageScreen(navigator = navController)
                        }
                    }
                }
            }
        }
    }


}
