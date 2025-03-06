package com.hits.bankemployee.core.presentation.common.component.dropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.common.component.defaultTextFieldColors
import com.hits.bankemployee.core.presentation.common.noRippleClickable
import com.hits.bankemployee.core.presentation.theme.S16_W400

@Composable
fun <T : DropdownItem> DropdownField(
    items: Iterable<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    isDropdownOpen: Boolean,
    onOpenDropdown: () -> Unit,
    onCloseDropdown: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().noRippleClickable(onOpenDropdown),
            value = selectedItem?.title ?: "",
            onValueChange = {},
            enabled = false,
            label = {
                Text(
                    text = label,
                    style = S16_W400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_drop_down),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            colors = defaultTextFieldColors,
        )
        DropdownMenu(
            expanded = isDropdownOpen,
            onDismissRequest = onCloseDropdown,
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.title,
                            style = S16_W400,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                    },
                )
            }
        }
    }
}