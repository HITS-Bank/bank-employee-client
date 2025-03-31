package com.hits.bankemployee.presentation.screen.loan.tariff.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.presentation.screen.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.presentation.screen.loan.tariff.model.TariffModel
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.SwipeableInfo
import ru.hitsbank.bank_common.presentation.common.component.SwipeableListItem

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
        ),
        padding = PaddingValues(vertical = 12.dp),
    )
}