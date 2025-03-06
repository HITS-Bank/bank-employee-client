package com.hits.bankemployee.loan.tariff.event

sealed interface TariffsScreenEffect {

    data object ShowTariffCreateError : TariffsScreenEffect

    data object ShowTariffDeleteError : TariffsScreenEffect
}