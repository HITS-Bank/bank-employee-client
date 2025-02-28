package com.hits.bankemployee.presentation.navigation.command

import androidx.activity.ComponentActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

sealed interface NavigationCommand {

    fun execute(navController: NavController, activity: ComponentActivity)

    class Navigate(
        private val destination: String,
        private val builder: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination, builder)
        }
    }

    class Replace(
        private val destination: String,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination) {
                popUpTo(
                    navController.currentBackStackEntry?.destination?.route ?: return@navigate
                ) {
                    inclusive = true
                }
            }
        }
    }

    class Forward(
        private val destination: String,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination)
        }
    }

    object Back : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigateUp().also { navigated ->
                if (!navigated) {
                    activity.finish()
                }
            }
        }
    }
}