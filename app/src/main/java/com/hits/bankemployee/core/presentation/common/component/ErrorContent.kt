package com.hits.bankemployee.core.presentation.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.noRippleClickable
import com.hits.bankemployee.core.presentation.common.verticalSpacer
import com.hits.bankemployee.core.presentation.theme.S14_W500
import com.hits.bankemployee.core.presentation.theme.S24_W400

@Composable
fun ErrorContent(
    onReload: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        16.dp.verticalSpacer()
        Text(
            text = "Что-то пошло не так…",
            style = S24_W400,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        36.dp.verticalSpacer()
        onBack?.let {
            Button(
                modifier = Modifier.size(width = 256.dp, height = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
                onClick = onBack,
            ) {
                Text(
                    text = "Назад",
                    style = S14_W500,
                    textAlign = TextAlign.Center,
                )
            }
        }
        onReload?.let {
            if (onBack != null) {
                16.dp.verticalSpacer()
            }
            Button(
                modifier = Modifier.size(width = 256.dp, height = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                onClick = onReload,
            ) {
                Text(
                    text = "Перезагрузить",
                    style = S14_W500,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun PaginationErrorContent(onRetry: () -> Unit) {
    ListItem(
        icon = ListItemIcon.Vector(
            iconResId = R.drawable.ic_error,
            backgroundColor = MaterialTheme.colorScheme.errorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
        title = "Что-то пошло не так…",
        subtitle = "Нажмите, чтобы попробовать еще раз",
        modifier = Modifier.noRippleClickable(onRetry),
        divider = Divider.None,
    )
}