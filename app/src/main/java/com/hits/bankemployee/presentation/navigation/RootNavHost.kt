package com.hits.bankemployee.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hits.bankemployee.presentation.navigation.destination.Auth
import com.hits.bankemployee.presentation.navigation.destination.BankAccountDetails
import com.hits.bankemployee.presentation.navigation.destination.BottomBarRoot
import com.hits.bankemployee.presentation.navigation.destination.LoanDetails
import com.hits.bankemployee.presentation.navigation.destination.UserDetails

@Composable
fun RootNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomBarRoot.route,
        modifier = modifier,
    ) {
        composable(route = Auth.route) {

        }
        composable(route = BottomBarRoot.route) {
            BottomBarNavHost()
        }
        composable(
            route = UserDetails.route,
            arguments = listOf(
                navArgument(UserDetails.ARG_USER_ID) {
                    type = NavType.StringType
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