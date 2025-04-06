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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.presentation.screen.loan.tariff.compose.TariffsScreen
import com.hits.bankemployee.presentation.screen.users.compose.UsersScreen
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.presentation.theme.settings.compose.ThemeSettingsScreen
import ru.hitsbank.bank_common.presentation.theme.settings.viewmodel.ThemeSettingsViewModel

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
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = Personalization.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(Personalization.title) },
                    selected = selectedItem?.destination?.route == Personalization.route,
                    onClick = { navController.navigate(Personalization.destination) }
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
                UsersScreen()
            }
            composable(route = Tariffs.route) {
                TariffsScreen()
            }
            composable(route = Personalization.route) {
                val viewModel = hiltViewModel<ThemeSettingsViewModel, ThemeSettingsViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(RoleType.EMPLOYEE)
                    }
                )
                ThemeSettingsScreen(viewModel)
            }
        }
    }
}