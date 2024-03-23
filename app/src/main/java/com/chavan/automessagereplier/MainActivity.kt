package com.chavan.automessagereplier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chavan.automessagereplier.core.utils.NavigationScreen
import com.chavan.automessagereplier.notification_service.NotificationUtils
import com.chavan.automessagereplier.presentation.custom_message.UpsertCustomMessageScreen
import com.chavan.automessagereplier.presentation.home.HomeScreen
import com.chavan.automessagereplier.presentation.permissions.NotificationPermissionScreen
import com.chavan.automessagereplier.ui.theme.AutoMessageReplierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = if (NotificationUtils.isNotificationAccessGranted(
                this,
                this.packageName
            )
        ) NavigationScreen.HomeScreen.route else NavigationScreen.NotificationPermissionScreen.route

        setContent {
            AutoMessageReplierTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(NavigationScreen.HomeScreen.route) {
                            HomeScreen(navigator = navController)
                        }
                        composable(NavigationScreen.NotificationPermissionScreen.route) {
                            NotificationPermissionScreen(navigator = navController)
                        }
                        composable(
                            route = "${NavigationScreen.UpsertCustomMessageScreen.route}?customMessageId={customMessageId}",
                            arguments = listOf(
                                navArgument(
                                    name = "customMessageId"
                                ) {
                                    type = NavType.LongType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            val customMessageId = it.arguments?.getLong("customMessageId") ?: -1
                            UpsertCustomMessageScreen(
                                navigator = navController, customMessageId = customMessageId
                            )
                        }
                    }
                }
            }
        }
    }
}
