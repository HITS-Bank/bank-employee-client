package com.hits.bankemployee.core.domain.entity.bankaccount

import java.time.LocalDateTime

data class OperationHistoryEntity(
    val id: String,
    val date: LocalDateTime,
    val amount: String,
    val type: OperationTypeEntity
)
