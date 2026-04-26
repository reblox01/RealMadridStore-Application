package com.bousmah.realmadridstore_zayd.config

import java.net.URL

/**
 * Secure configuration for API endpoints and sensitive settings.
 * SECURITY NOTE: Never commit actual API keys or secrets to version control.
 */
object SecureConfig {
    
    // API URLs - configurable per build type via BuildConfig
    val OLLAMA_BASE_URL: String = BuildConfig.OLLAMA_BASE_URL
    
    // Timeout settings for network operations (in seconds)
    const val CONNECT_TIMEOUT_SECONDS = 30
    const val READ_TIMEOUT_SECONDS = 60
    const val WRITE_TIMEOUT_SECONDS = 60
    
    /**
     * Validates that the configured URL uses HTTPS in production builds
     */
    fun isSecureUrlConfigured(): Boolean {
        return try {
            val url = URL(OLLAMA_BASE_URL)
            when (BuildConfig.BUILD_TYPE) {
                "release" -> url.protocol == "https"
                else -> true // Allow HTTP in debug
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if running on emulator
     */
    fun isEmulator(): Boolean {
        return (android.os.Build.FINGERPRINT.startsWith("generic") ||
                android.os.Build.FINGERPRINT.startsWith("unknown") ||
                android.os.Build.MODEL.contains("google_sdk") ||
                android.os.Build.MODEL.contains("Emulator") ||
                android.os.Build.MODEL.contains("Android SDK"))
    }
}
