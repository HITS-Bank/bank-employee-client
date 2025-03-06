package com.hits.bankemployee.loan.tariff.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.core.presentation.common.horizontalSpacer
import com.hits.bankemployee.core.presentation.common.verticalSpacer
import com.hits.bankemployee.core.presentation.theme.S16_W400
import com.hits.bankemployee.core.presentation.theme.S16_W500
import com.hits.bankemployee.loan.tariff.event.TariffsScreenEvent
import com.hits.bankemployee.loan.tariff.model.TariffsScreenDialogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TariffCreateDialog(
    state: TariffsScreenDialogState.CreateTariff,
    onEvent: (TariffsScreenEvent) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { onEvent(TariffsScreenEvent.TariffDialogClosed) },
    ) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Создание тарифа",
                    style = S16_W500,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.name,
                    onValueChange = { onEvent(TariffsScreenEvent.TariffCreateNameChanged(it)) },
                    label = {
                        Text(text = "Название")
                    },
                    singleLine = true,
                )
                16.dp.verticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.interestRate,
                    onValueChange = { onEvent(TariffsScreenEvent.TariffCreateInterestRateChanged(it)) },
                    label = {
                        Text(text = "Процентная ставка")
                    },
                    singleLine = true,
                )
                16.dp.verticalSpacer()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { onEvent(TariffsScreenEvent.TariffDialogClosed) },
                    ) {
                        Text(
                            text = "Отмена",
                            style = S16_W400,
                        )
                    }
                    8.dp.horizontalSpacer()
                    Button(
                        onClick = { onEvent(TariffsScreenEvent.TariffCreateConfirmed) },
                        enabled = state.isDataValid,
                    ) {
                        Text(
                            text = "Создать",
                            style = S16_W400,
                        )
                    }
                }
            }
        }
    }
}