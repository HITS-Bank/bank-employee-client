package com.hits.bankemployee

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.core.presentation.common.LocalSnackbarController
import com.hits.bankemployee.core.presentation.common.SnackbarController
import com.hits.bankemployee.core.presentation.navigation.RootNavHost
import com.hits.bankemployee.core.presentation.navigation.base.NavigationManager
import com.hits.bankemployee.core.presentation.theme.BankEmployeeTheme

class MainActivity : ComponentActivity() {

    /* TODO DI */
    private val navigationManager = NavigationManager()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navigationManager.commands.collect { command ->
                    command.execute(navController, this@MainActivity)
                }
            }
            BankEmployeeTheme {
                CompositionLocalProvider(
                    LocalSnackbarController provides SnackbarController(
                        snackbarHostState = snackbarHostState,
                        coroutineScope = rememberCoroutineScope(),
                    )
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) {
                        RootNavHost(
                            navHostController = navController,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}