package com.hits.bankemployee.di

import android.util.Patterns
import com.hits.bankemployee.domain.interactor.ValidationInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideValidationInteractor(): ValidationInteractor {
        return ValidationInteractor(emailPattern = Patterns.EMAIL_ADDRESS)
    }
}