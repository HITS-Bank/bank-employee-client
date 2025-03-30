package com.hits.bankemployee.di

import com.hits.bankemployee.data.repository.AuthRepository
import com.hits.bankemployee.data.repository.BankAccountRepository
import com.hits.bankemployee.data.repository.LoanRepository
import com.hits.bankemployee.data.repository.ProfileRepository
import com.hits.bankemployee.domain.repository.IAuthRepository
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import com.hits.bankemployee.domain.repository.ILoanRepository
import com.hits.bankemployee.domain.repository.IProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepository: AuthRepository
    ): IAuthRepository

    @Binds
    abstract fun bindProfileRepository(
        profileRepository: ProfileRepository
    ): IProfileRepository

    @Binds
    abstract fun bindLoanRepository(
        loanRepository: LoanRepository
    ): ILoanRepository

    @Binds
    abstract fun bindBankAccountRepository(
        bankAccountRepository: BankAccountRepository
    ): IBankAccountRepository
}