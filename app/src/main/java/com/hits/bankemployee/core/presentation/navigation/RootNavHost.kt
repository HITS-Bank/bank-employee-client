package com.hits.bankemployee.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hits.bankemployee.login.compose.LoginScreenWrapper

@Composable
fun RootNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomBarRoot.destination,
        modifier = modifier,
    ) {
        composable(route = Auth.route) {
            LoginScreenWrapper()
        }
        composable(route = BottomBarRoot.route) {
            BottomBarNavHost()
        }
        composable(
            route = UserDetails.route,
            arguments = listOf(
                navArgument(UserDetails.ARG_USER_ID) {
                    type = NavType.StringType
                },
                navArgument(UserDetails.ARG_USER_FULLNAME) {
                    type = NavType.StringType
                },
                navArgument(UserDetails.ARG_IS_USER_BLOCKED) {
                    type = NavType.BoolType
                }
            ),
        ) {

        }
        composable(
            route = BankAccountDetails.route,
            arguments = listOf(
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_NUMBER) {
                    type = NavType.StringType
                }
            ),
        ) {

        }
        composable(
            route = LoanDetails.route,
            arguments = listOf(
                navArgument(LoanDetails.ARG_LOAN_NUMBER) {
                    type = NavType.StringType
                }
            ),
        ) {

        }
    }
}