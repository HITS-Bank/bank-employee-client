package com.hits.bankemployee.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideNavigationManager(): NavigationManager {
        return NavigationManager()
    }
}