package com.hits.bankemployee.presentation.screen.loan.details.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hits.bankemployee.R
import com.hits.bankemployee.presentation.common.BankUiState
import com.hits.bankemployee.presentation.common.component.Divider
import com.hits.bankemployee.presentation.common.component.ErrorContent
import com.hits.bankemployee.presentation.common.component.ListItem
import com.hits.bankemployee.presentation.common.component.ListItemEnd
import com.hits.bankemployee.presentation.common.component.ListItemIcon
import com.hits.bankemployee.presentation.common.component.LoadingContent
import com.hits.bankemployee.presentation.common.noRippleClickable
import com.hits.bankemployee.presentation.common.rememberCallback
import com.hits.bankemployee.presentation.screen.loan.details.event.LoanDetailsEvent
import com.hits.bankemployee.presentation.screen.loan.details.model.LoanDetailsListItem
import com.hits.bankemployee.presentation.screen.loan.details.model.LoanDetailsState
import com.hits.bankemployee.presentation.screen.loan.details.viewmodel.LoanDetailsViewModel
import com.hits.bankemployee.presentation.theme.S22_W400
import com.hits.bankemployee.presentation.theme.S24_W600

@Composable
fun LoanDetailsScreen(viewModel: LoanDetailsViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = rememberCallback(viewModel::onEvent)

    when (val state = uiState) {
        BankUiState.Loading -> {
            LoadingContent()
        }

        is BankUiState.Error -> {
            ErrorContent(
                onBack = {
                    onEvent.invoke(LoanDetailsEvent.Back)
                }
            )
        }

        is BankUiState.Ready -> {
            LoanDetailsScreenContent(state.model, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoanDetailsScreenContent(
    model: LoanDetailsState,
    onEvent: (LoanDetailsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Кредит",
                        style = S22_W400,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp)
                            .noRippleClickable { onEvent(LoanDetailsEvent.Back) },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            )
        },
    ) { paddings ->
        LazyColumn(
            modifier = Modifier.padding(paddings)
        ) {
            items(model.detailItems) { item ->
                when (item) {
                    LoanDetailsListItem.LoanInfoHeader -> Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Информация о кредите",
                        style = S24_W600,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    is LoanDetailsListItem.LoanDetailsProperty -> ListItem(
                        icon = ListItemIcon.None,
                        divider = Divider.None,
                        padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 16.dp),
                        title = item.value,
                        subtitle = item.name,
                    )

                    is LoanDetailsListItem.LoanBankAccount -> ListItem(
                        modifier = Modifier.clickable { onEvent(LoanDetailsEvent.OpenBankAccount(item.accountId)) },
                        icon = ListItemIcon.None,
                        divider = Divider.None,
                        padding = PaddingValues(top = 12.dp, bottom = 12.dp, end = 16.dp),
                        title = item.value,
                        subtitle = item.name,
                        end = ListItemEnd.Chevron,
                    )
                }
            }
        }
    }
}