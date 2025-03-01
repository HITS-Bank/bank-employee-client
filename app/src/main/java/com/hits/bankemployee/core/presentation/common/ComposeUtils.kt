package com.hits.bankemployee.core.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.verticalSpacer() = Spacer(modifier = Modifier.height(this))

@Composable
fun Dp.horizontalSpacer() = Spacer(modifier = Modifier.width(this))