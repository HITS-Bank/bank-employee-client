package com.hits.bankemployee.presentation.screen.client.event

sealed interface ClientDetailsScreenEffect {

    data object ShowBlockError : ClientDetailsScreenEffect

    data object ShowUnblockError : ClientDetailsScreenEffect
}