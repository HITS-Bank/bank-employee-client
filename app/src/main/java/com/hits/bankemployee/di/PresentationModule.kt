package com.hits.bankemployee.di

import com.hits.bankemployee.loan.tariff.mapper.TariffsScreenModelMapper
import com.hits.bankemployee.loan.tariff.viewmodel.TariffsScreenViewModel
import com.hits.bankemployee.login.mapper.LoginScreenModelMapper
import com.hits.bankemployee.login.viewmodel.LoginViewModel
import com.hits.bankemployee.users.mapper.UsersScreenModelMapper
import com.hits.bankemployee.users.model.UserRole
import com.hits.bankemployee.users.viewmodel.UserListViewModel
import com.hits.bankemployee.users.viewmodel.UsersScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun presentationModule() = module {
    singleOf(::LoginScreenModelMapper)
    singleOf(::UsersScreenModelMapper)
    singleOf(::TariffsScreenModelMapper)

    viewModelOf(::LoginViewModel)
    viewModelOf(::UsersScreenViewModel)
    viewModel(named(UserRole.CLIENT.name)) {
        UserListViewModel(UserRole.CLIENT, get(), get(), get())
    }
    viewModel(named(UserRole.EMPLOYEE.name)) {
        UserListViewModel(UserRole.EMPLOYEE, get(), get(), get())
    }
    viewModelOf(::TariffsScreenViewModel)
}