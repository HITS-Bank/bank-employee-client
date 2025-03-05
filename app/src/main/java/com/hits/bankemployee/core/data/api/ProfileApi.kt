package com.hits.bankemployee.core.data.api

import com.hits.bankemployee.core.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {

    @GET("/profile")
    suspend fun getSelfProfile(): Response<ProfileResponse>
}