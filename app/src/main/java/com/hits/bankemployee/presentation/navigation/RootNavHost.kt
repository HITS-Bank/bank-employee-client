package com.hits.bankemployee.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hits.bankemployee.presentation.screen.account.compose.AccountDetailsScreen
import com.hits.bankemployee.presentation.screen.account.viewmodel.AccountDetailsScreenViewModel
import com.hits.bankemployee.presentation.screen.client.compose.ClientDetailsScreen
import com.hits.bankemployee.presentation.screen.client.model.ClientModel
import com.hits.bankemployee.presentation.screen.client.viewmodel.ClientDetailsScreenViewModel
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.presentation.screen.login.compose.LoginScreenWrapper
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = Auth.destination,
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
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(UserDetails.ARG_USER_ID)
            val userFullname = backStackEntry.arguments?.getString(UserDetails.ARG_USER_FULLNAME)
            val isUserBlocked = backStackEntry.arguments?.getBoolean(UserDetails.ARG_IS_USER_BLOCKED)

            if (userId != null && userFullname != null && isUserBlocked != null) {
                val clientInfo = ClientModel(
                    userId,
                    userFullname,
                    isUserBlocked,
                )
                val viewModel: ClientDetailsScreenViewModel = koinViewModel(
                    parameters = { parametersOf(clientInfo) },
                )
                ClientDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
        composable(
            route = BankAccountDetails.route,
            arguments = listOf(
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_NUMBER) {
                    type = NavType.StringType
                },
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_BALANCE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_STATUS) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) { backStackEntry ->
            val bankAccountNumber = backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_NUMBER)
            val bankAccountBalance = backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_BALANCE)
            val bankAccountStatus = backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_STATUS)?.run {
                BankAccountStatusEntity.valueOf(this)
            }

            if (bankAccountNumber != null) {
                val viewModel: AccountDetailsScreenViewModel = koinViewModel(
                    parameters = { parametersOf(bankAccountNumber, bankAccountBalance, bankAccountStatus) },
                )
                AccountDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
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