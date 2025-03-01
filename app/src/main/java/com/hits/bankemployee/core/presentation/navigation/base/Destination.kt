package com.hits.bankemployee.core.presentation.navigation.base

import androidx.annotation.DrawableRes

abstract class Destination {

    val destination = javaClass.simpleName

    val route: String by lazy {
        buildString {
            append(destination)
            arguments.forEach { arg ->
                append("/{$arg}")
            }
            optionalArguments.forEachIndexed { i, arg ->
                append(if (i == 0) "?" else "&")
                append("$arg={$arg}")
            }
        }
    }

    open var arguments: List<String> = emptyList()
    open var optionalArguments: List<String> = emptyList()

    fun destinationWithArgs(vararg args: Any?) = buildString {
        append(destination)
        args.forEach { arg ->
            append("/$arg")
        }
    }

    fun destinationWithArgs(args: List<Any>, optionalArgs: Map<String, Any?> = emptyMap()) =
        buildString {
            append(destination)
            args.forEach { arg ->
                append("/$arg")
            }
            var i = 0
            for ((arg, value) in optionalArgs) {
                if (value == null) continue
                append(if (i == 0) "?" else "&")
                append("$arg=$value")
                i++
            }
        }
}

abstract class BottomBarDestination : Destination() {
    @get:DrawableRes
    abstract val icon: Int
    abstract val title: String
}