package com.hits.bankemployee.presentation.screen.users.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hits.bankemployee.presentation.screen.users.event.UsersScreenEvent
import com.hits.bankemployee.presentation.screen.users.model.UsersTab
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.presentation.theme.S14_W500

@Composable
fun ColumnScope.UsersScreenPager(
    onEvent: (UsersScreenEvent) -> Unit,
    content: @Composable (UsersTab) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        UsersTab.entries.size
    })
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            onEvent(UsersScreenEvent.TabSelected(UsersTab.entries[index]))
        }
    }
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .padding(horizontal = (configuration.screenWidthDp * 0.18).dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp),
                    )
                    .height(3.dp)
            )
        }
    ) {
        UsersTab.entries.forEachIndexed { index, userTab ->
            Tab(
                selected = index == pagerState.currentPage,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ) {
                Text(
                    text = userTab.title,
                    modifier = Modifier.padding(vertical = 14.dp),
                    style = S14_W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { index ->
        content(UsersTab.entries[index])
    }
}