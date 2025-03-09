package com.hits.bankemployee.presentation.screen.loan.tariff.event

sealed interface TariffsScreenEffect {

    data object ShowTariffCreateError : TariffsScreenEffect

    data object ShowTariffDeleteError : TariffsScreenEffect
}