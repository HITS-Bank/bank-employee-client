package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.loan.LoanResponse
import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoanApi {

    @GET("credit/loan/tariffs")
    suspend fun getLoanTariffs(
        @Query("sortingProperty") sortingProperty: String,
        @Query("sortingOrder") sortingOrder: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<List<LoanTariffResponse>>

    @DELETE("credit/employee/loan/tariffs/{tariffId}/delete")
    suspend fun deleteLoanTariff(
        @Path("tariffId") tariffId: String,
    ): Response<ResponseBody>

    @POST("credit/employee/loan/tariffs/create")
    suspend fun createLoanTariff(
        @Body loanTariffCreateRequest: LoanTariffCreateRequest
    ): Response<ResponseBody>

    @GET("credit/employee/loan/{userId}/list")
    suspend fun getClientLoans(
        @Path("userId") userId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
    ): Response<List<LoanResponse>>

    @GET("credit/loan/{loanId}")
    suspend fun getLoanById(@Path("loanid") loanId: String): Response<LoanResponse>
}