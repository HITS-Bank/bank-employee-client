package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.loan.LoanPage
import com.hits.bankemployee.data.model.loan.LoanResponse
import com.hits.bankemployee.data.model.loan.LoanTariffCreateRequest
import com.hits.bankemployee.data.model.loan.LoanTariffDeleteRequest
import com.hits.bankemployee.data.model.loan.LoanTariffsPage
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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
    ): Response<LoanTariffsPage>

    @HTTP(method = "DELETE", path = "credit/employee/loan/tariffs/delete", hasBody = true)
    suspend fun deleteLoanTariff(
        @Body loanTariffDeleteRequest: LoanTariffDeleteRequest
    ): Response<ResponseBody>

    @POST("credit/employee/loan/tariffs/create")
    suspend fun createLoanTariff(
        @Body loanTariffCreateRequest: LoanTariffCreateRequest
    ): Response<ResponseBody>

    @GET("credit/employee/loan/{userId}/list")
    suspend fun getClientLoans(
        @Path("userId") userId: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<LoanPage>

    @GET("credit/loan/{loanNumber}")
    suspend fun getLoanByNumber(@Path("loanNumber") loanNumber: String): Response<LoanResponse>
}