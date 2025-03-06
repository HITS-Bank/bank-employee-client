package com.hits.bankemployee.loan.tariff.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.component.Divider
import com.hits.bankemployee.core.presentation.common.component.ListItemIcon
import com.hits.bankemployee.core.presentation.common.component.SwipeableInfo
import com.hits.bankemployee.core.presentation.common.component.SwipeableListItem
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.loan.tariff.model.TariffModel

@Composable
fun TariffListItem(item: TariffModel, onEvent: (TariffsScreenEvent) -> Unit) {
    SwipeableListItem(
        icon = ListItemIcon.None,
        title = item.name,
        subtitle = item.interestRate,
        divider = Divider.Default(padding = PaddingValues(horizontal = 16.dp)),
        swipeableInfo = SwipeableInfo(
            iconResId = R.drawable.ic_delete,
            onIconClick = {
                onEvent(TariffsScreenEvent.TariffDeleteClicked(item.id, item.name))
            }
        )
    )
}