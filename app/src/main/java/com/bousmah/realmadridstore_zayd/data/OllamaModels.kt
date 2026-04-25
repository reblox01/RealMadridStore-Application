package com.bousmah.realmadridstore_zayd.data

import com.google.gson.annotations.SerializedName

data class OllamaRequest(
    val model: String = "deepseek-r1:8b",
    val messages: List<OllamaMessage>,
    val stream: Boolean = false
)

data class OllamaMessage(
    val role: String,
    val content: String
)

data class OllamaResponse(
    val model: String,
    val message: OllamaMessage,
    @SerializedName("done") val done: Boolean
)
