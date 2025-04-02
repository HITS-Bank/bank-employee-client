package com.hits.bankemployee.data.api

import com.hits.bankemployee.data.model.ProfileResponse
import com.hits.bankemployee.data.model.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {

    @GET("users/profile")
    suspend fun getSelfProfile(): Response<ProfileResponse>

    @GET("users/employee/profile/list")
    suspend fun getProfilesPage(
        @Query("role") role: String? = null,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<List<ProfileResponse>>

    @POST("users/employee/profile/{userId}/ban")
    suspend fun banUser(@Path("userId") userId: String): Response<ResponseBody>

    @POST("users/employee/profile/{userId}/unban")
    suspend fun unbanUser(@Path("userId") userId: String): Response<ResponseBody>

    @POST("users/employee/auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<ResponseBody>
}