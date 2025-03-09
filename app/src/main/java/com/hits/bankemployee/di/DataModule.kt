package com.hits.bankemployee.di

import com.hits.bankemployee.data.datasource.SessionManager
import com.hits.bankemployee.data.mapper.AuthMapper
import com.hits.bankemployee.data.mapper.BankAccountMapper
import com.hits.bankemployee.data.mapper.LoanMapper
import com.hits.bankemployee.data.mapper.ProfileMapper
import com.hits.bankemployee.data.repository.AuthRepository
import com.hits.bankemployee.data.repository.BankAccountRepository
import com.hits.bankemployee.data.repository.LoanRepository
import com.hits.bankemployee.data.repository.ProfileRepository
import com.hits.bankemployee.domain.repository.IAuthRepository
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import com.hits.bankemployee.domain.repository.ILoanRepository
import com.hits.bankemployee.domain.repository.IProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf(::AuthMapper)
    singleOf(::ProfileMapper)
    singleOf(::LoanMapper)
    singleOf(::BankAccountMapper)
    singleOf(::AuthRepository) bind IAuthRepository::class
    singleOf(::ProfileRepository) bind IProfileRepository::class
    singleOf(::LoanRepository) bind ILoanRepository::class
    singleOf(::BankAccountRepository) bind IBankAccountRepository::class
    singleOf(::SessionManager)
}