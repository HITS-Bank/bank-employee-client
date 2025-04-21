package com.hits.bankemployee

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hits.bankemployee.presentation.navigation.Auth
import com.hits.bankemployee.presentation.navigation.RootNavHost
import dagger.hilt.android.AndroidEntryPoint
import ru.hitsbank.bank_common.Constants.DEEPLINK_APP_SCHEME
import ru.hitsbank.bank_common.domain.entity.RoleType
import ru.hitsbank.bank_common.presentation.common.LocalSnackbarController
import ru.hitsbank.bank_common.presentation.common.SnackbarController
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.replace
import ru.hitsbank.bank_common.presentation.theme.AppTheme
import ru.hitsbank.bank_common.presentation.theme.ThemeViewModel
import ru.hitsbank.bank_common.requestNotificationPermission
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel, ThemeViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(RoleType.EMPLOYEE)
                }
            )
            val theme by themeViewModel.themeFlow.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navigationManager.commands.collect { command ->
                    command.execute(navController, this@MainActivity)
                }
            }
            AppTheme(theme) {
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

                        LaunchedEffect(Unit) {
                            handleDeeplink()
                        }
                    }
                }
            }
        }
    }

    private fun handleDeeplink() {
        intent?.data?.let { uri ->
            if (
                uri.scheme == DEEPLINK_APP_SCHEME &&
                uri.host == "employee_authorized"
            ) {
                val code = uri.getQueryParameter("code")

                Handler(
                    Looper.getMainLooper()
                ).postDelayed(
                    {
                        navigationManager.replace(Auth.withArgs(code))
                    },
                    700
                )
            }
        }
    }
}