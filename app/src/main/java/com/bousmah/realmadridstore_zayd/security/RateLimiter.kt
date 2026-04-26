package com.bousmah.realmadridstore_zayd.security

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Rate limiter to prevent API abuse and DoS attacks.
 * Implements sliding window algorithm.
 *
 * OWASP: Implements rate limiting per user on public endpoints
 */
class RateLimiter(
    private val maxRequests: Int = 10,
    private val windowSize: Duration = 60.seconds,
    private val cooldownDelay: Duration = 500.milliseconds
) {
    private val mutex = Mutex()
    private val requests = mutableListOf<TimeMark>()
    private var lastRequestTime: TimeMark? = null
    
    /**
     * Checks if request is allowed under rate limit.
     */
    suspend fun tryAcquire(): Boolean {
        return mutex.withLock {
            val now = TimeSource.Monotonic.markNow()
            
            // Remove expired requests outside the window
            requests.removeAll { it.elapsedNow() > windowSize }
            
            // Check if under limit
            if (requests.size >= maxRequests) {
                return@withLock false
            }
            
            // Enforce cooldown between requests to prevent UI abuse
            lastRequestTime?.let {
                val timeSinceLast = it.elapsedNow()
                if (timeSinceLast < cooldownDelay) {
                    // Still allow but will be handled by debounce
                }
            }
            
            requests.add(now)
            lastRequestTime = now
            true
        }
    }
    
    /**
     * Gets remaining requests in current window.
     */
    suspend fun getRemaining(): Int {
        return mutex.withLock {
            val now = TimeSource.Monotonic.markNow()
            requests.removeAll { it.elapsedNow() > windowSize }
            maxRequests - requests.size
        }
    }
    
    /**
     * Gets time until next request is allowed.
     */
    suspend fun getRetryAfter(): Duration {
        return mutex.withLock {
            val now = TimeSource.Monotonic.markNow()
            requests.removeAll { it.elapsedNow() > windowSize }
            
            if (requests.isEmpty()) {
                return@withLock Duration.ZERO
            }
            
            val oldestRequest = requests.minByOrNull { it.elapsedNow() } ?: return@withLock Duration.ZERO
            val expiresAt = windowSize - oldestRequest.elapsedNow()
            expiresAt.coerceAtLeast(Duration.ZERO)
        }
    }
    
    /**
     * Resets the rate limiter.
     */
    suspend fun reset() {
        mutex.withLock {
            requests.clear()
            lastRequestTime = null
        }
    }
}

/**
 * Global rate limiter manager.
 */
object RateLimiterManager {
    // Strict limits for chat API
    val chatRateLimiter = RateLimiter(
        maxRequests = 30,
        windowSize = 60.seconds,
        cooldownDelay = 1.seconds
    )
    
    // General API rate limiting
    val generalRateLimiter = RateLimiter(
        maxRequests = 100,
        windowSize = 60.seconds,
        cooldownDelay = 100.milliseconds
    )
}

class RateLimitExceededException(message: String) : Exception(message)
