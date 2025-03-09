package com.hits.bankemployee.presentation.screen.loan.tariff.compose.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hits.bankemployee.presentation.screen.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.presentation.screen.loan.tariff.model.TariffsScreenDialogState

@Composable
fun TariffDeleteDialog(
    dialogState: TariffsScreenDialogState.DeleteTariff,
    onEvent: (TariffsScreenEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(TariffsScreenEvent.TariffDialogClosed) },
        confirmButton = {
            Button(onClick = { onEvent(TariffsScreenEvent.TariffDeleteConfirmed) }) {
                Text(text = "Удалить")
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(TariffsScreenEvent.TariffDialogClosed) }) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(text = "Удаление тарифа")
        },
        text = {
            Text(text = "Вы уверены, что хотите удалить кредитный тариф \"${dialogState.tariffName}\"? Отменить это действие невозможно.")
        }
    )
}