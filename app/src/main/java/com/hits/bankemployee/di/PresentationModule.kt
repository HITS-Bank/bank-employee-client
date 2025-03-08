package com.hits.bankemployee.di

import com.hits.bankemployee.presentation.screen.account.mapper.AccountDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.account.viewmodel.AccountDetailsScreenViewModel
import com.hits.bankemployee.presentation.screen.client.mapper.ClientDetailsScreenModelMapper
import com.hits.bankemployee.presentation.screen.loan.tariff.mapper.TariffsScreenModelMapper
import com.hits.bankemployee.presentation.screen.loan.tariff.viewmodel.TariffsScreenViewModel
import com.hits.bankemployee.presentation.screen.login.mapper.LoginScreenModelMapper
import com.hits.bankemployee.presentation.screen.login.viewmodel.LoginViewModel
import com.hits.bankemployee.presentation.screen.client.viewmodel.ClientDetailsScreenViewModel
import com.hits.bankemployee.presentation.screen.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.presentation.screen.users.model.UserRole
import com.hits.bankemployee.presentation.screen.users.viewmodel.UserListViewModel
import com.hits.bankemployee.presentation.screen.users.viewmodel.UsersScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun presentationModule() = module {
    singleOf(::LoginScreenModelMapper)
    singleOf(::UsersScreenModelMapper)
    singleOf(::TariffsScreenModelMapper)
    singleOf(::ClientDetailsScreenModelMapper)
    singleOf(::AccountDetailsScreenModelMapper)

    viewModelOf(::LoginViewModel)
    viewModelOf(::UsersScreenViewModel)
    viewModel(named(UserRole.CLIENT.name)) {
        UserListViewModel(UserRole.CLIENT, get(), get(), get())
    }
    viewModel(named(UserRole.EMPLOYEE.name)) {
        UserListViewModel(UserRole.EMPLOYEE, get(), get(), get())
    }
    viewModelOf(::TariffsScreenViewModel)
    viewModel { parameters ->
        ClientDetailsScreenViewModel(parameters.get(), get(), get(), get(), get(), get())
    }
    viewModel { parameters ->
        AccountDetailsScreenViewModel(
            parameters.get(),
            parameters.get(),
            parameters.get(),
            get(),
            get(),
            get(),
        )
    }
}