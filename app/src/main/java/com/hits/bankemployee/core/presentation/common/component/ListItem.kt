package com.hits.bankemployee.core.presentation.common.component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.horizontalSpacer
import com.hits.bankemployee.core.presentation.common.textDp
import com.hits.bankemployee.core.presentation.theme.S14_W400
import com.hits.bankemployee.core.presentation.theme.S16_W400
import com.hits.bankemployee.core.presentation.theme.S16_W500
import androidx.compose.material3.Icon as MaterialIcon

sealed interface ListItemIcon {

    @Composable
    fun RowScope.Icon()

    object None : ListItemIcon {

        @Composable
        override fun RowScope.Icon() {}
    }

    class SingleChar(
        private val char: Char,
        private val backgroundColor: Color? = null,
        private val charColor: Color? = null,
    ) : ListItemIcon {

        @Composable
        override fun RowScope.Icon() {
            val backgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primaryContainer
            val charColor = charColor ?: MaterialTheme.colorScheme.onPrimaryContainer

            Text(
                text = char.toString(),
                modifier = Modifier
                    .size(40.dp)
                    .background(backgroundColor, CircleShape)
                    .wrapContentSize(),
                color = charColor,
                style = S16_W500.copy(fontSize = 16.textDp),
                textAlign = TextAlign.Center,
            )
        }
    }

    class Vector(
        @DrawableRes private val iconResId: Int,
        private val backgroundColor: Color? = null,
        private val iconColor: Color? = null,
    ) : ListItemIcon {

        @Composable
        override fun RowScope.Icon() {
            val backgroundColor = backgroundColor ?: MaterialTheme.colorScheme.primaryContainer
            val iconColor = iconColor ?: MaterialTheme.colorScheme.onPrimaryContainer

            MaterialIcon(
                modifier = Modifier
                    .size(40.dp)
                    .background(backgroundColor, CircleShape)
                    .padding(8.dp),
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = null,
                tint = iconColor,
            )
        }
    }
}

sealed interface ListItemEnd {

    @Composable
    fun RowScope.End()

    object None : ListItemEnd {

        @Composable
        override fun RowScope.End() {}
    }

    object Chevron : ListItemEnd {

        @Composable
        override fun RowScope.End() {
            MaterialIcon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
            )
        }
    }
}

sealed interface Divider {

    @SuppressLint("NotConstructor")
    @Composable
    fun BoxScope.Divider()

    object None : Divider {

        @Composable
        override fun BoxScope.Divider() {}
    }

    class Default(
        private val padding: PaddingValues = PaddingValues(start = 72.dp, end = 16.dp),
    ) : Divider {

        @Composable
        override fun BoxScope.Divider() {
            HorizontalDivider(
                modifier = Modifier.padding(padding).align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

@Composable
fun ListItem(
    icon: ListItemIcon,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    end: ListItemEnd = ListItemEnd.None,
    divider: Divider = Divider.Default(),
    titleTextStyle: TextStyle = S16_W400.copy(color = MaterialTheme.colorScheme.onSurface),
    subtitleTextStyle: TextStyle = S14_W400.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
) {
    Box {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(padding),
        ) {
            with(icon) { Icon() }
            16.dp.horizontalSpacer()
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = subtitleTextStyle,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            with(end) { End() }
        }
        with(divider) { Divider() }
    }
}