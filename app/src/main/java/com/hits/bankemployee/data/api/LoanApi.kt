package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffDeleteRequest
import com.hits.bankemployee.data.model.loan.LoanTariffsPage
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoanApi {

    @GET("loan/tariffs")
    suspend fun getLoanTariffs(
        @Query("sortingProperty") sortingProperty: String,
        @Query("sortingOrder") sortingOrder: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<LoanTariffsPage>

    @POST("employee/loan/tariffs/delete")
    suspend fun deleteLoanTariff(
        @Body loanTariffDeleteRequest: LoanTariffDeleteRequest
    ): Response<ResponseBody>

    @POST("employee/loan/tariffs/create")
    suspend fun createLoanTariff(
        @Body loanTariffCreateRequest: LoanTariffCreateRequest
    ): Response<ResponseBody>
}