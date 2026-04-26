package com.bousmah.realmadridstore_zayd.security

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Debounced click handler to prevent accidental double-clicks and API spam.
 * 
 * OWASP: Prevents rapid state-changing operations
 */
@Composable
fun <T> debounced(delayMillis: Long = 500, onClick: (T) -> Unit): (T) -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
    
    return { param: T ->
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= delayMillis) {
            lastClickTime = now
            onClick(param)
        }
    }
}

@Composable
fun debouncedUnit(delayMillis: Long = 500, onClick: () -> Unit): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    return {
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= delayMillis) {
            lastClickTime = now
            onClick()
        }
    }
}

/**
 * Modifier extension for debounced clickable.
 * Prevents multiple rapid click events.
 */
fun Modifier.debouncedClickable(
    debounceDuration: Duration = 500.milliseconds,
    onClick: () -> Unit
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    var isClickable by remember { mutableStateOf(true) }
    
    clickable(enabled = isClickable) {
        if (isClickable) {
            isClickable = false
            onClick()
            coroutineScope.launch {
                delay(debounceDuration)
                isClickable = true
            }
        }
    }
}

/**
 * Button composable wrapper with built-in debouncing.
 */
@Composable
fun DebouncedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    debounceDuration: Duration = 500.milliseconds,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }
    val canClick = enabled && !isProcessing
    val coroutineScope = rememberCoroutineScope()
    
    Button(
        onClick = {
            if (isProcessing) return@Button
            isProcessing = true
            onClick()
            coroutineScope.launch {
                delay(debounceDuration)
                isProcessing = false
            }
        },
        modifier = modifier,
        enabled = canClick
    ) {
        content()
    }
}

@Composable
private inline fun Modifier.composed(
    factory: @Composable Modifier.() -> Modifier
): Modifier = this.then(factory(Modifier))
