package com.hits.bankemployee.di

import com.hits.bankemployee.data.api.BankAccountApi
import com.hits.bankemployee.data.api.LoanApi
import com.hits.bankemployee.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.hitsbank.bank_common.di.AuthRetrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideUserApi(
        @AuthRetrofit retrofit: Retrofit,
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoanApi(
        @AuthRetrofit retrofit: Retrofit,
    ): LoanApi {
        return retrofit.create(LoanApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBankAccountApi(
        @AuthRetrofit retrofit: Retrofit,
    ): BankAccountApi {
        return retrofit.create(BankAccountApi::class.java)
    }
}