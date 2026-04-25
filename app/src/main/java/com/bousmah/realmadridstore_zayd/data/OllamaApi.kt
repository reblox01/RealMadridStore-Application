package com.bousmah.realmadridstore_zayd.data

import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApi {
    @POST("api/chat")
    suspend fun chat(@Body request: OllamaRequest): OllamaResponse
}
