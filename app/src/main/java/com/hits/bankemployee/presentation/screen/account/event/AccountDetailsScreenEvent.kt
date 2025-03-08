package com.hits.bankemployee.presentation.screen.account.event

sealed interface AccountDetailsScreenEvent {

    data object NavigateBack : AccountDetailsScreenEvent
}