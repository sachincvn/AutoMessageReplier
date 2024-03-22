package com.chavan.automessagereplier.presentation.home.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DrawerModalDrawerSheet(scope: CoroutineScope, navigationState: DrawerState) {

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val items = listOf(
        DrawerItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        DrawerItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        DrawerItem(
            title = "Privacy Policy",
            selectedIcon = Icons.Filled.Security,
            unselectedIcon = Icons.Outlined.Security,
        ),
        DrawerItem(
            title = "Share app",
            selectedIcon = Icons.Filled.Share,
            unselectedIcon = Icons.Outlined.Share,
        ),
    )

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(26.dp))
        Image(
            imageVector = Icons.Default.Abc,
            contentDescription = "",
            modifier = Modifier
                .size(150.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(26.dp))
        items.forEachIndexed { index, drawerItem ->
            NavigationDrawerItem(label = {
                Text(text = drawerItem.title)
            }, selected = index == selectedItemIndex, onClick = {
                selectedItemIndex = index
                scope.launch {
                    navigationState.close()
                }
            }, icon = {
                Icon(
                    imageVector = if (index == selectedItemIndex) {
                        drawerItem.selectedIcon
                    } else drawerItem.unselectedIcon,
                    contentDescription = drawerItem.title
                )
            }, badge = {
                drawerItem.badgeCount?.let {
                    Text(text = drawerItem.badgeCount.toString())
                }
            }, modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )

        }
        DrawerFooter()
    }
}

data class DrawerItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)