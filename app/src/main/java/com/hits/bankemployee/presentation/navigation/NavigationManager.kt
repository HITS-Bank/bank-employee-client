package com.hits.bankemployee.presentation.navigation

import com.hits.bankemployee.presentation.navigation.command.NavigationCommand
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