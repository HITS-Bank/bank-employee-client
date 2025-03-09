package com.hits.bankemployee.di

import com.hits.bankemployee.presentation.navigation.base.NavigationManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::NavigationManager)
}