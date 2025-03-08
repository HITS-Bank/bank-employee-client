package com.hits.bankemployee.account.event

sealed interface AccountDetailsScreenEvent {

    data object NavigateBack : AccountDetailsScreenEvent
}