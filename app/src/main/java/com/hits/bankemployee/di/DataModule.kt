package com.hits.bankemployee.di

import com.hits.bankemployee.core.data.datasource.SessionManager
import com.hits.bankemployee.core.data.mapper.AuthMapper
import com.hits.bankemployee.core.data.mapper.ProfileMapper
import com.hits.bankemployee.core.data.repository.AuthRepository
import com.hits.bankemployee.core.data.repository.ProfileRepository
import com.hits.bankemployee.core.domain.repository.IAuthRepository
import com.hits.bankemployee.core.domain.repository.IProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf(::AuthMapper)
    singleOf(::ProfileMapper)
    singleOf(::AuthRepository) bind IAuthRepository::class
    singleOf(::ProfileRepository) bind IProfileRepository::class
    singleOf(::SessionManager)
}