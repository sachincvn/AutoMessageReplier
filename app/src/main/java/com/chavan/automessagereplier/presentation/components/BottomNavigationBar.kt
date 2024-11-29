import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chavan.automessagereplier.core.utils.NavigationScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            route = NavigationScreen.HomeScreen.route,
            label = "Home",
            icon = Icons.Filled.Home
        ),
        BottomNavItem(
            route = NavigationScreen.ClickToChatScreen.route,
            label = "Click to Chat",
            icon = Icons.Filled.Chat
        ),
        BottomNavItem(
            route = NavigationScreen.SettingsScreen.route,
            label = "Settings",
            icon = Icons.Filled.Settings
        )
    )

    val currentRoute by navController.currentBackStackEntryAsState()
    val selectedRoute = currentRoute?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedRoute == item.route,
                onClick = {
                    if (selectedRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
