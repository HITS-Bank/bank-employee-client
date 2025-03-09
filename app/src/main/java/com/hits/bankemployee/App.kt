package com.hits.bankemployee

import android.app.Application
import com.hits.bankemployee.di.dataModule
import com.hits.bankemployee.di.domainModule
import com.hits.bankemployee.di.navigationModule
import com.hits.bankemployee.di.networkModule
import com.hits.bankemployee.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                presentationModule(),
                navigationModule(),
                domainModule(),
                dataModule(),
                networkModule(),
            )
        }
    }
}