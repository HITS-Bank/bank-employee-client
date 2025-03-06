package com.hits.bankemployee.core.data.api

import com.hits.bankemployee.core.data.model.ProfileResponse
import com.hits.bankemployee.core.data.model.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {

    @GET("profile")
    suspend fun getSelfProfile(): Response<ProfileResponse>

    @GET("employee/profile/list")
    suspend fun getProfilesPage(
        @Query("role") role: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<List<ProfileResponse>>

    @POST("employee/profile/{userId}/ban")
    suspend fun banUser(@Path("userId") userId: String): Response<ResponseBody>

    @POST("employee/profile/{userId}/unban")
    suspend fun unbanUser(@Path("userId") userId: String): Response<ResponseBody>

    @POST("employee/auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<ResponseBody>
}