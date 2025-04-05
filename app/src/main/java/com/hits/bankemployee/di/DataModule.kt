package com.hits.bankemployee.di

import com.hits.bankemployee.data.repository.BankAccountRepository
import com.hits.bankemployee.data.repository.LoanRepository
import com.hits.bankemployee.data.repository.UserRepository
import com.hits.bankemployee.domain.repository.IBankAccountRepository
import com.hits.bankemployee.domain.repository.ILoanRepository
import com.hits.bankemployee.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.hitsbank.bank_common.data.repository.AuthRepository
import ru.hitsbank.bank_common.data.repository.ProfileRepository
import ru.hitsbank.bank_common.domain.repository.IAuthRepository
import ru.hitsbank.bank_common.domain.repository.IProfileRepository

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
    abstract fun bindUserRepository(
        profileRepository: UserRepository
    ): IUserRepository

    @Binds
    abstract fun bindLoanRepository(
        loanRepository: LoanRepository
    ): ILoanRepository

    @Binds
    abstract fun bindBankAccountRepository(
        bankAccountRepository: BankAccountRepository
    ): IBankAccountRepository
}