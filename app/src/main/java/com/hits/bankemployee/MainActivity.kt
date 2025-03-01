package com.hits.bankemployee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.navigation.RootNavHost
import com.hits.bankemployee.core.presentation.theme.BankEmployeeTheme

class MainActivity : ComponentActivity() {

    /* TODO DI */
    private val navigationManager = NavigationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navigationManager.commands.collect { command ->
                    command.execute(navController, this@MainActivity)
                }
            }
            BankEmployeeTheme {
                RootNavHost(
                    navHostController = navController,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}