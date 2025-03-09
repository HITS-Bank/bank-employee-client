package com.hits.bankemployee.di

import android.util.Patterns
import com.hits.bankemployee.domain.interactor.AuthInteractor
import com.hits.bankemployee.domain.interactor.BankAccountInteractor
import com.hits.bankemployee.domain.interactor.LoanInteractor
import com.hits.bankemployee.domain.interactor.ProfileInteractor
import com.hits.bankemployee.domain.interactor.ValidationInteractor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun domainModule() = module {
    singleOf(::AuthInteractor)
    singleOf(::ProfileInteractor)
    single { ValidationInteractor(emailPattern = Patterns.EMAIL_ADDRESS) }
    singleOf(::LoanInteractor)
    singleOf(::BankAccountInteractor)
}