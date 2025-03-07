package com.hits.bankemployee.client.event

sealed interface ClientDetailsScreenEffect {

    data object ShowBlockError : ClientDetailsScreenEffect

    data object ShowUnblockError : ClientDetailsScreenEffect
}