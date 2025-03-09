package com.hits.bankemployee.presentation.common.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable

val defaultTextFieldColors
    @Composable get() = OutlinedTextFieldDefaults.colors(
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )