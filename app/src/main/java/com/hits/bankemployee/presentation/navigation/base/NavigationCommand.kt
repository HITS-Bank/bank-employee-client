package com.hits.bankemployee.presentation.navigation.base

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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

    /* Не тестировалось */
    class ForwardWithCallback(
        private val destination: String,
        private val callback: () -> Unit,
    ) : NavigationCommand {

        override fun execute(navController: NavController, activity: ComponentActivity) {
            navController.navigate(destination)
            val backstackEntry = navController.getBackStackEntry(destination)

            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    callback()
                }
            }
            backstackEntry.lifecycle.addObserver(observer)
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