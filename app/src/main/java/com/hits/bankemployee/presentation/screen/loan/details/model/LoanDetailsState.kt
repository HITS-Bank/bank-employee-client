package com.hits.bankemployee.presentation.screen.loan.details.model

data class LoanDetailsState(
    val detailItems: List<LoanDetailsListItem>,
) {
    companion object {
        fun default(detailItems: List<LoanDetailsListItem>) = LoanDetailsState(
            detailItems = detailItems,
        )
    }
}
