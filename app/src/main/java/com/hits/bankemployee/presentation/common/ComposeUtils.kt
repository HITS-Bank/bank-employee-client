package com.hits.bankemployee.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun Dp.verticalSpacer() = Spacer(modifier = Modifier.height(this))

@Composable
fun Dp.horizontalSpacer() = Spacer(modifier = Modifier.width(this))

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

private fun Int.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}

val Int.textDp: TextUnit
    @Composable get() =  this.textDp(density = LocalDensity.current)

@Composable
fun <T> rememberCallback(callback: (T) -> Unit) = remember { callback }

@Composable
inline fun <reified T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
) {
    var job by remember(key1 = this, key2 = lifecycleOwner) { mutableStateOf<Job?>(null) }

    LaunchedEffect(key1 = this, key2 = lifecycleOwner) {
        job = lifecycleOwner.lifecycleScope.launch {
            flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
        }
    }

    DisposableEffect(key1 = this, key2 = lifecycleOwner) {
        onDispose {
            job?.cancel()
        }
    }
}