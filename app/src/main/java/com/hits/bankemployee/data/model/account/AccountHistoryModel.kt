package com.hits.bankemployee.data.model.account

data class AccountHistoryModel(
    val id: String,
    val type: OperationType,
    val amount: String,
    val executedAt: String,
)
