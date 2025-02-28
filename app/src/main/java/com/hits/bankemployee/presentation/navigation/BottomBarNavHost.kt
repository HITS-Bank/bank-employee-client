package com.hits.bankemployee.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.presentation.navigation.destination.Tariffs
import com.hits.bankemployee.presentation.navigation.destination.Users

@Composable
fun BottomBarNavHost() {
    val navController = rememberNavController()
    val selectedItem by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = Users.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(Users.title) },
                    selected = selectedItem?.destination?.route == Users.route,
                    onClick = { navController.navigate(Users.destination) }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = Tariffs.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(Tariffs.title) },
                    selected = selectedItem?.destination?.route == Tariffs.route,
                    onClick = { navController.navigate(Tariffs.destination) }
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Users.route,
        ) {
            composable(route = Users.route) {
                Text(Users.title)
            }
            composable(route = Tariffs.route) {
                Text(Tariffs.title)
            }
        }
    }
}