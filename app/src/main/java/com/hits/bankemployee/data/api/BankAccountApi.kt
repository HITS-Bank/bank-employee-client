package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.account.AccountHistoryModel
import com.hits.bankemployee.data.model.account.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BankAccountApi {

    @GET("/core/bank_account/{userId}/list")
    suspend fun getAccountsList(
        @Path("userId") clientId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<List<AccountResponse>>

    @GET("core/bank_account/{accountId}")
    suspend fun getAccountById(@Path("accountId") accountId: String): Response<AccountResponse>

    @POST("core/bank_account/{accountId}/operation_history")
    suspend fun getAccountOperationHistory(
        @Path("accountId") accountId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<List<AccountHistoryModel>>
}