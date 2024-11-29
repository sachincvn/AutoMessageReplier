package com.chavan.automessagereplier

import BottomNavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chavan.automessagereplier.core.utils.NavigationScreen
import com.chavan.automessagereplier.presentation.custom_message.UpsertCustomMessageScreen
import com.chavan.automessagereplier.presentation.direct_chat.ClickToChatScreen
import com.chavan.automessagereplier.presentation.home.HomeScreen
import com.chavan.automessagereplier.presentation.permissions.NotificationPermissionScreen
import com.chavan.automessagereplier.presentation.settings.SettingsScreen
import com.chavan.automessagereplier.presentation.settings.SettingsViewModel
import com.chavan.automessagereplier.ui.theme.AutoMessageReplierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SettingsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme by viewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())

            AutoMessageReplierTheme(
                darkTheme = isDarkTheme,
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val bottomNavRoutes = listOf(
                        NavigationScreen.HomeScreen.route,
                        NavigationScreen.SettingsScreen.route,
                        NavigationScreen.ClickToChatScreen.route
                    )

                    val showBottomNav = bottomNavRoutes.contains(currentRoute)

                    Scaffold(
                        bottomBar = {
                            if (showBottomNav) {
                                BottomNavigationBar(navController = navController)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = NavigationScreen.HomeScreen.route,
                            modifier = Modifier
                                .padding(innerPadding)
                                .imePadding()
                        ) {
                            composable(NavigationScreen.HomeScreen.route) {
                                HomeScreen(navigator = navController)
                            }
                            composable(NavigationScreen.SettingsScreen.route) {
                                SettingsScreen(
                                    navigator = navController,
                                    viewModel = viewModel
                                )
                            }
                            composable(NavigationScreen.ClickToChatScreen.route) {
                                ClickToChatScreen(navigator = navController)
                            }
                            composable(NavigationScreen.NotificationPermissionScreen.route) {
                                NotificationPermissionScreen(navigator = navController)
                            }
                            composable(
                                route = "${NavigationScreen.UpsertCustomMessageScreen.route}?customMessageId={customMessageId}",
                                arguments = listOf(navArgument("customMessageId") {
                                    type = NavType.LongType
                                    defaultValue = -1
                                })
                            ) {
                                val customMessageId = it.arguments?.getLong("customMessageId") ?: -1
                                UpsertCustomMessageScreen(
                                    navigator = navController,
                                    customMessageId = customMessageId
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}