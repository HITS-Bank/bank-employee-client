package com.hits.bankemployee.presentation.screen.loan.payments.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.R
import com.hits.bankemployee.presentation.screen.loan.payments.event.LoanPaymentsEvent
import com.hits.bankemployee.presentation.screen.loan.payments.model.LoanPaymentsState
import com.hits.bankemployee.presentation.screen.loan.payments.viewmodel.LoanPaymentsViewModel
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.component.BankButton
import ru.hitsbank.bank_common.presentation.common.component.Divider
import ru.hitsbank.bank_common.presentation.common.component.ErrorContent
import ru.hitsbank.bank_common.presentation.common.component.ListItem
import ru.hitsbank.bank_common.presentation.common.component.ListItemEnd
import ru.hitsbank.bank_common.presentation.common.component.ListItemIcon
import ru.hitsbank.bank_common.presentation.common.component.LoadingContent
import ru.hitsbank.bank_common.presentation.common.rememberCallback
import ru.hitsbank.bank_common.presentation.common.verticalSpacer
import ru.hitsbank.bank_common.presentation.theme.S16_W400
import ru.hitsbank.bank_common.presentation.theme.S22_W400
import ru.hitsbank.bank_common.presentation.theme.S24_W600

@Composable
fun LoanPaymentsScreen(viewModel: LoanPaymentsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)

    when (val cachedState = state) {
        BankUiState.Loading -> {
            LoadingContent()
        }

        is BankUiState.Error -> {
            ErrorContent(
                onBack = {
                    onEvent.invoke(LoanPaymentsEvent.Back)
                },
                onReload = {
                    onEvent.invoke(LoanPaymentsEvent.Refresh)
                }
            )
        }

        is BankUiState.Ready -> {
            LoanPaymentsScreenReady(
                model = cachedState.model,
                onEvent = onEvent,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanPaymentsScreenReady(
    model: LoanPaymentsState,
    onEvent: (LoanPaymentsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Кредит",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                onEvent.invoke(LoanPaymentsEvent.Back)
                            }
                        ),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back_48),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
        floatingActionButton = {
            BankButton.Outlined(
                text = "Информация о кредите",
                onClick = { onEvent.invoke(LoanPaymentsEvent.LoanInfoClick) },
                icon = ImageVector.vectorResource(id = R.drawable.ic_info),
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .padding(horizontal = 16.dp),
            state = rememberLazyListState(),
        ) {
            item {
                16.dp.verticalSpacer()
                Text(
                    text = "Платежи по кредиту",
                    style = S24_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                16.dp.verticalSpacer()
            }

            if (model.payments.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Пока тут ничего нет",
                        style = S16_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            items(model.payments, key = { it.id }) { item ->
                ListItem(
                    padding = PaddingValues(horizontal = 0.dp, vertical = 12.dp),
                    divider = Divider.None,
                    icon = ListItemIcon.SingleChar(
                        char = item.currencyChar,
                        backgroundColor = item.backgroundColor,
                        charColor = item.foregroundColor,
                    ),
                    title = item.title,
                    subtitle = item.description,
                    end = ListItemEnd.Text(
                        text = item.status,
                        textColor = item.foregroundColor,
                    ),
                )
            }
        }
    }
}