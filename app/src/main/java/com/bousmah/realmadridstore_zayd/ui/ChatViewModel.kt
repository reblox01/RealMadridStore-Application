package com.bousmah.realmadridstore_zayd.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bousmah.realmadridstore_zayd.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage("Hala Madrid! I'm your Real Madrid Store assistant. How can I help you today?", false)
    ),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val selectedImage: Bitmap? = null
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:11434/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val ollamaApi = retrofit.create(OllamaApi::class.java)

    private val systemPrompt = """
        You are a Real Madrid Store expert assistant. Here are our products:
        - Home Jersey 2024/25: €90
        - Away Jersey 2024/25: €90
        - Training Kit: €65
        - Jacket: €110
        - Scarf: €25
        - Kids Home Jersey: €70
        - Boots: €150
        You know store locations in Madrid, Dubai, New York and Tokyo. Shipping is €4.99. 

        Real Madrid squad numbers 2025/26:
        #1 Courtois, #2 Carvajal, #3 Militão, #4 Alaba, #5 Bellingham,
        #6 Camavinga, #7 Vinicius Jr, #8 Valverde, #9 Endrick,
        #10 Mbappé, #11 Rodrygo, #12 Trent Alexander-Arnold,
        #13 Lunin, #14 Tchouaméni, #15 Arda Güler, #16 Gonzalo García,
        #17 Asencio, #18 Carreras, #19 Ceballos, #20 Fran García,
        #21 Brahim Díaz, #22 Rüdiger, #23 Mendy, #24 Huijsen
        Coach: Alvaro Arbeloa

        Answer only about Real Madrid store topics.
    """.trimIndent()

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onImageCaptured(bitmap: Bitmap) {
        _uiState.update { it.copy(selectedImage = bitmap) }
    }

    fun removeSelectedImage() {
        _uiState.update { it.copy(selectedImage = null) }
    }

    fun sendMessage() {
        val currentState = _uiState.value
        val messageText = currentState.inputText
        val image = currentState.selectedImage
        
        if (messageText.isBlank() && image == null) return

        val userMessage = ChatMessage(messageText, true, image)
        _uiState.update { it.copy(
            messages = it.messages + userMessage,
            inputText = "",
            selectedImage = null,
            isLoading = true
        ) }

        viewModelScope.launch {
            try {
                val ollamaMessages = listOf(
                    OllamaMessage(role = "system", content = systemPrompt),
                    OllamaMessage(role = "user", content = messageText)
                )
                
                val request = OllamaRequest(
                    model = "deepseek-r1:8b",
                    messages = ollamaMessages,
                    stream = false
                )
                
                val response = ollamaApi.chat(request)
                
                val botReply = response.message.content
                // DeepSeek-R1 often includes <think> blocks, you might want to strip them for the final UI
                val cleanReply = botReply.replace(Regex("<think>.*?</think>", RegexOption.DOT_MATCHES_ALL), "").trim()
                
                val botMessage = ChatMessage(cleanReply.ifBlank { botReply }, false)
                _uiState.update { it.copy(messages = it.messages + botMessage, isLoading = false) }
            } catch (e: Exception) {
                val errorMessage = ChatMessage("Error: ${e.localizedMessage}. Make sure Ollama is running at http://localhost:11434", false)
                _uiState.update { it.copy(messages = it.messages + errorMessage, isLoading = false) }
            }
        }
    }
}
