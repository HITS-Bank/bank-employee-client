package com.hits.bankemployee.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.presentation.navigation.NavigationManager
import com.hits.bankemployee.presentation.navigation.RootNavHost
import com.hits.bankemployee.presentation.ui.theme.BankEmployeeTheme

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