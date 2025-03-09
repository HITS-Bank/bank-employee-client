package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.account.AccountHistoryModel
import com.hits.bankemployee.data.model.account.AccountNumberRequest
import com.hits.bankemployee.data.model.account.AccountResponse
import com.hits.bankemployee.data.model.account.AccountsPaginationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BankAccountApi {

    @GET("/core/bank_account/{userId}/list")
    suspend fun getAccountsList(
        @Path("userId") clientId: String,
    ): Response<AccountsPaginationResponse>

    @GET("core/bank_account/account")
    suspend fun getAccountByNumber(@Body accountNumberRequest: AccountNumberRequest): Response<AccountResponse>

    @POST("core/bank_account/operation_history")
    suspend fun getAccountOperationHistory(
        @Body accountNumberRequest: AccountNumberRequest,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<List<AccountHistoryModel>>
}