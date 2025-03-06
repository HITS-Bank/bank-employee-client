package com.hits.bankemployee.core.presentation.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.R
import com.hits.bankemployee.core.presentation.theme.S16_W400

@Composable
fun SearchTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = text,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                text = placeholder,
                style = S16_W400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        colors = defaultTextFieldColors,
    )
}