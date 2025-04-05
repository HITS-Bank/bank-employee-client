package com.hits.bankemployee.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hits.bankemployee.data.api.BankAccountApi
import com.hits.bankemployee.data.api.LoanApi
import com.hits.bankemployee.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.hitsbank.bank_common.Constants.KEYCLOAK_BASE_URL
import ru.hitsbank.bank_common.data.api.AuthApi
import ru.hitsbank.bank_common.data.api.ProfileApi
import ru.hitsbank.bank_common.data.interceptor.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuthRetrofit

private const val TIMEOUT_SEC = 20L
private const val BASE_URL = "http://192.168.110.98:9446/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private fun retrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        gson: Gson,
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private fun buildOkHttpClient(
        vararg interceptors: Interceptor,
    ) = OkHttpClient.Builder().apply {
        interceptors.forEach { addInterceptor(it) }
        connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
        readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
    }.build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @NoAuthOkHttp
    @Provides
    fun provideNoAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return buildOkHttpClient(loggingInterceptor)
    }

    @Singleton
    @AuthOkHttp
    @Provides
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return buildOkHttpClient(
            loggingInterceptor,
            authInterceptor,
        )
    }

    @Singleton
    @AuthRetrofit
    @Provides
    fun provideAuthRetrofit(
        @AuthOkHttp okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return retrofit(okHttpClient, BASE_URL, gson)
    }

    @Singleton
    @NoAuthRetrofit
    @Provides
    fun provideNoAuthRetrofit(
        @NoAuthOkHttp okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return retrofit(okHttpClient, KEYCLOAK_BASE_URL, gson)
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        @NoAuthRetrofit retrofit: Retrofit,
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApi(
        @AuthRetrofit retrofit: Retrofit,
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(@AuthRetrofit retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
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