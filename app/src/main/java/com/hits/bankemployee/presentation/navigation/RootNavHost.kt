package com.hits.bankemployee.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hits.bankemployee.domain.entity.bankaccount.BankAccountStatusEntity
import com.hits.bankemployee.presentation.screen.account.compose.AccountDetailsScreen
import com.hits.bankemployee.presentation.screen.account.viewmodel.AccountDetailsScreenViewModel
import com.hits.bankemployee.presentation.screen.client.compose.ClientDetailsScreen
import com.hits.bankemployee.presentation.screen.client.model.ClientModel
import com.hits.bankemployee.presentation.screen.client.viewmodel.ClientDetailsScreenViewModel
import com.hits.bankemployee.presentation.screen.loan.details.compose.LoanDetailsScreen
import com.hits.bankemployee.presentation.screen.loan.details.viewmodel.LoanDetailsViewModel
import com.hits.bankemployee.presentation.screen.loan.payments.compose.LoanPaymentsScreen
import com.hits.bankemployee.presentation.screen.loan.payments.viewmodel.LoanPaymentsViewModel
import com.hits.bankemployee.presentation.screen.login.compose.LoginScreenWrapper
import com.hits.bankemployee.presentation.screen.login.viewmodel.LoginViewModel
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.domain.entity.RoleType

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
        composable(
            route = Auth.route,
            arguments = listOf(
                navArgument(Auth.OPTIONAL_AUTH_CODE_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            )
        ) { backStackEntry ->
            val authCode = backStackEntry.arguments?.getString(
                Auth.OPTIONAL_AUTH_CODE_ARG
            )
            val viewModel: LoginViewModel = hiltViewModel<LoginViewModel, LoginViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(authCode = authCode)
                }
            )

            LoginScreenWrapper(viewModel)
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
                },
                navArgument(UserDetails.ARG_IS_CLIENT) {
                    type = NavType.BoolType
                },
                navArgument(UserDetails.ARG_IS_EMPLOYEE) {
                    type = NavType.BoolType
                },
            ),
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(UserDetails.ARG_USER_ID)
            val userFullname = backStackEntry.arguments?.getString(UserDetails.ARG_USER_FULLNAME)
            val isUserBlocked =
                backStackEntry.arguments?.getBoolean(UserDetails.ARG_IS_USER_BLOCKED)
            val isClient =
                backStackEntry.arguments?.getBoolean(UserDetails.ARG_IS_CLIENT) ?: false
            val isEmployee =
                backStackEntry.arguments?.getBoolean(UserDetails.ARG_IS_EMPLOYEE) ?: false

            val userRoles = buildList {
                if (isClient) {
                    add(RoleType.CLIENT)
                }
                if (isEmployee) {
                    add(RoleType.EMPLOYEE)
                }
            }

            if (userId != null && userFullname != null && isUserBlocked != null) {
                val clientInfo = ClientModel(
                    userId,
                    userFullname,
                    isUserBlocked,
                    userRoles,
                )
                val viewModel: ClientDetailsScreenViewModel =
                    hiltViewModel<ClientDetailsScreenViewModel, ClientDetailsScreenViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(clientInfo)
                        }
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
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_ID) {
                    type = NavType.StringType
                },
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_NUMBER) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_BALANCE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(BankAccountDetails.ARG_CURRENCY_CODE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(BankAccountDetails.ARG_BANK_ACCOUNT_STATUS) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) { backStackEntry ->
            val bankAccountId =
                backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_ID)
            val bankAccountNumber =
                backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_NUMBER)
            val bankAccountBalance =
                backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_BALANCE)
            val currencyCode =
                backStackEntry.arguments?.getString(BankAccountDetails.ARG_CURRENCY_CODE)?.run {
                    CurrencyCode.valueOf(this)
                }
            val bankAccountStatus =
                backStackEntry.arguments?.getString(BankAccountDetails.ARG_BANK_ACCOUNT_STATUS)
                    ?.run {
                        BankAccountStatusEntity.valueOf(this)
                    }

            if (bankAccountId != null) {
                val viewModel: AccountDetailsScreenViewModel =
                    hiltViewModel<AccountDetailsScreenViewModel, AccountDetailsScreenViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(
                                bankAccountId,
                                bankAccountNumber,
                                bankAccountBalance,
                                currencyCode,
                                bankAccountStatus,
                            )
                        }
                    )
                AccountDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
        composable(
            route = LoanPayments.route,
            arguments = listOf(
                navArgument(LoanPayments.ARG_LOAN_ID) {
                    type = NavType.StringType
                }
            ),
        ) {
            val loanId = it.arguments?.getString(LoanPayments.ARG_LOAN_ID)

            if (loanId != null) {
                val viewModel: LoanPaymentsViewModel =
                    hiltViewModel<LoanPaymentsViewModel, LoanPaymentsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(loanId)
                        }
                    )
                LoanPaymentsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
        composable(
            route = LoanDetails.route,
            arguments = listOf(
                navArgument(LoanDetails.ARG_LOAN_ID) {
                    type = NavType.StringType
                }
            ),
        ) {
            val loanId = it.arguments?.getString(LoanDetails.ARG_LOAN_ID)

            if (loanId != null) {
                val viewModel: LoanDetailsViewModel =
                    hiltViewModel<LoanDetailsViewModel, LoanDetailsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(loanId)
                        }
                    )
                LoanDetailsScreen(viewModel)
            } else {
                LaunchedEffect(Unit) {
                    navHostController.popBackStack()
                }
            }
        }
    }
}