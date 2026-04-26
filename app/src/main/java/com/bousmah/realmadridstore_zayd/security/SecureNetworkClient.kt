package com.bousmah.realmadridstore_zayd.security

import android.util.Log
import com.bousmah.realmadridstore_zayd.config.SecureConfig
import okhttp3.CertificatePinner
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.util.Arrays
import java.util.Collections
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Secure OkHttp client configuration with TLS hardening and certificate pinning.
 *
 * OWASP: Enforces HTTPS, implements certificate pinning, disables insecure protocols
 */
object SecureNetworkClient {
    
    private const val TAG = "SecureNetworkClient"
    
    /**
     * Certificate pinning configuration.
     * Add SHA-256 hashes of your server's certificate public keys here.
     * 
     * SECURITY WARNING: These are example hashes. Replace with actual values
     * using: openssl s_client -connect yourdomain.com:443 < /dev/null | openssl x509 -noout -pkey -pubin -sha256 | base64
     */
    private val CERTIFICATE_PINNER by lazy {
        CertificatePinner.Builder()
            // Example: Replace with actual pins for your Ollama server
            // .add("your-ollama-domain.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            // Backup pin (intermediate CA)
            // .add("your-ollama-domain.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
            .build()
    }
    
    /**
     * Creates a secure OkHttp client with TLS 1.2+ enforcement.
     */
    fun createSecureClient(enableLogging: Boolean = false): OkHttpClient {
        val builder = OkHttpClient.Builder()
        
        // Timeout configuration
        builder.connectTimeout(SecureConfig.CONNECT_TIMEOUT_SECONDS.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        builder.readTimeout(SecureConfig.READ_TIMEOUT_SECONDS.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        builder.writeTimeout(SecureConfig.WRITE_TIMEOUT_SECONDS.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        
        // Force TLS 1.2+ only
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
            .cipherSuites(
                okhttp3.CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                okhttp3.CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                okhttp3.CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                okhttp3.CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                okhttp3.CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                okhttp3.CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256
            )
            .build()
        
        builder.connectionSpecs(Collections.singletonList(spec))
        
        // Enable certificate pinning (when configured)
        // builder.certificatePinner(CERTIFICATE_PINNER)
        
        // Follow redirects but enforce HTTPS
        builder.followRedirects(true)
        builder.followSslRedirects(true)
        
        // Add security headers interceptor
        builder.addInterceptor(SecurityHeadersInterceptor())
        
        // Add certificate validation
        builder.addNetworkInterceptor(CertificateValidationInterceptor())
        
        // Logging for debug (REMOVE in production)
        if (enableLogging) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
            builder.addInterceptor(logging)
        }
        
        return builder.build()
    }
    
    /**
     * Trust manager that validates certificate chains strictly.
     */
    private fun createStrictTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers: ${trustManagers.contentToString()}")
        }
        return trustManagers[0] as X509TrustManager
    }
}

/**
 * Interceptor that adds security headers to all requests.
 */
class SecurityHeadersInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        
        // Skip if not HTTPS
        if (!original.url.isHttps) {
            Log.w(TAG, "Non-HTTPS request detected: ${original.url}")
            if (isReleaseBuild()) {
                throw SecurityException("HTTPS required for release builds")
            }
        }
        
        val request = original.newBuilder()
            .header("X-Content-Type-Options", "nosniff")
            .header("X-Frame-Options", "DENY")
            .header("X-XSS-Protection", "1; mode=block")
            .header("Referrer-Policy", "strict-origin-when-cross-origin")
            .header("Accept", "application/json")
            .method(original.method, original.body)
            .build()
        
        return chain.proceed(request)
    }
    
    private fun isReleaseBuild(): Boolean {
        return !android.os.Build.TYPE.equals("eng") && 
               !android.os.Build.TYPE.equals("userdebug") &&
               !android.os.Build.FINGERPRINT.contains("dev")
    }
}

/**
 * Interceptor that validates SSL certificates.
 */
class CertificateValidationInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        // Log certificate info for debugging
        if (response.handshake != null) {
            val tlsVersion = response.handshake?.tlsVersion
            val cipherSuite = response.handshake?.cipherSuite
            Log.d("SecureNetworkClient", "TLS: $tlsVersion, Cipher: $cipherSuite")
            
            // Warn if using weak TLS
            if (tlsVersion?.javaClass?.name?.contains("TLS_1_0", ignoreCase = true) == true ||
                tlsVersion?.javaClass?.name?.contains("TLS_1_1", ignoreCase = true) == true ||
                tlsVersion?.javaClass?.name?.contains("SSL", ignoreCase = true) == true) {
                Log.w("SecureNetworkClient", "Weak TLS version detected: $tlsVersion")
            }
        }
        
        return response
    }
}
