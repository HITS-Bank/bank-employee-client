package com.hits.bankemployee.core.presentation.navigation.base

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {

    private val _commands = MutableSharedFlow<NavigationCommand>(replay = 1)
    val commands = _commands.asSharedFlow()

    suspend fun addCommand(command: NavigationCommand) {
        _commands.emit(command)
    }

    fun tryAddCommand(command: NavigationCommand): Boolean {
        return _commands.tryEmit(command)
    }
}