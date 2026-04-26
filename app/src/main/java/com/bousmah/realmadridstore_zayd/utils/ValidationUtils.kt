package com.bousmah.realmadridstore_zayd.utils

import java.util.Calendar

object ValidationUtils {
    
    /**
     * Validates credit card number using Luhn algorithm
     * Supports cards with 13-19 digits (Visa, Mastercard, Amex, etc.)
     */
    fun isValidCardNumber(cardNumber: String): Boolean {
        val sanitized = cardNumber.replace(Regex("\\s"), "")
        if (!sanitized.matches(Regex("^\\d{13,19}$"))) return false
        
        var sum = 0
        var alternate = false
        for (i in sanitized.length - 1 downTo 0) {
            var n = sanitized[i].digitToInt()
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
    
    /**
     * Validates cardholder name using allowlist approach
     * Allows: letters, spaces, hyphens, apostrophes, periods
     * Length: 2-50 characters
     */
    fun isValidCardholderName(name: String): Boolean {
        return name.matches(Regex("^[a-zA-Z\\s\\-'\\.]{2,50}$"))
    }
    
    /**
     * Validates expiry date in MM/YY format
     * Checks format and ensures card is not expired
     */
    fun isValidExpiry(expiry: String): Boolean {
        if (!expiry.matches(Regex("^(0[1-9]|1[0-2])/(\\d{2})$"))) return false
        
        val parts = expiry.split("/")
        val month = parts[0].toInt()
        val year = parts[1].toInt() + 2000
        
        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH) + 1
        
        return when {
            year < currentYear -> false
            year == currentYear && month < currentMonth -> false
            year > currentYear + 20 -> false // Max 20 years ahead
            else -> true
        }
    }
    
    /**
     * Validates CVV (3-4 digits)
     */
    fun isValidCVV(cvv: String): Boolean {
        return cvv.matches(Regex("^\\d{3,4}$"))
    }
    
    /**
     * Validates all payment fields and returns specific error message
     */
    fun validatePayment(cardNumber: String, holderName: String, expiry: String, cvv: String): ValidationResult {
        return when {
            cardNumber.isBlank() -> ValidationResult.Error("Card number is required")
            !isValidCardNumber(cardNumber) -> ValidationResult.Error("Invalid card number")
            holderName.isBlank() -> ValidationResult.Error("Cardholder name is required")
            !isValidCardholderName(holderName) -> ValidationResult.Error("Invalid cardholder name")
            expiry.isBlank() -> ValidationResult.Error("Expiry date is required")
            !isValidExpiry(expiry) -> ValidationResult.Error("Invalid or expired date (MM/YY)")
            cvv.isBlank() -> ValidationResult.Error("CVV is required")
            !isValidCVV(cvv) -> ValidationResult.Error("Invalid CVV (3-4 digits)")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Sanitizes chat input to prevent prompt injection attacks
     * Removes HTML, scripts, and limits length
     */
    fun sanitizeChatInput(input: String): String {
        var sanitized = input
            .replace(Regex("<script[^>]*>.*?</script>", RegexOption.DOT_MATCHES_ALL or RegexOption.IGNORE_CASE), "")
            .replace(Regex("javascript:", RegexOption.IGNORE_CASE), "")
            .replace(Regex("[<>]"), "")
            .replace(Regex("[\x00-\x1F]"), "") // Control characters
            .trim()
        
        // Prevent prompt injection patterns
        val forbiddenPatterns = listOf(
            "ignore previous", "disregard", "forget",
            "system:", "assistant:", "user:",
            "{{", "{%", "${'$'}{", // Template injection
            "<|", "|>", // Token patterns
        )
        
        for (pattern in forbiddenPatterns) {
            if (sanitized.contains(pattern, ignoreCase = true)) {
                sanitized = sanitized.replace(pattern, "", ignoreCase = true)
            }
        }
        
        return sanitized.take(500) // Limit to 500 chars
    }
    
    /**
     * Validates HTTPS URL to prevent open redirects and mixed content
     */
    fun isValidHttpsUrl(url: String): Boolean {
        return url.startsWith("https://") && 
               url.matches(Regex("^https://[\\w\\-]+(\\.[\\w\\-]+)+(/[\\w\\-./?%&=@]*)?$"))
    }
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
