/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import ca.gosyer.BuildConfig
import ca.gosyer.data.library.model.DisplayMode
import ca.gosyer.data.models.Category
import ca.gosyer.data.models.Manga
import ca.gosyer.ui.base.components.LoadingScreen
import ca.gosyer.ui.base.vm.viewModel
import ca.gosyer.ui.manga.openMangaMenu
import ca.gosyer.util.compose.ThemedWindow
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

fun openLibraryMenu() {
    ThemedWindow(BuildConfig.NAME) {
        LibraryScreen()
    }
}

@Composable
fun LibraryScreen(onClickManga: (Long) -> Unit = { openMangaMenu(it) }) {
    val vm = viewModel<LibraryScreenViewModel>()
    val categories by vm.categories.collectAsState()
    val selectedCategoryIndex by vm.selectedCategoryIndex.collectAsState()
    val displayMode by vm.displayMode.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val serverUrl by vm.serverUrl.collectAsState()
    // val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Surface {
        if (categories.isEmpty()) {
            LoadingScreen(isLoading)
        } else {
            /*ModalBottomSheetLayout(
                sheetState = sheetState,
                sheetContent = { *//*LibrarySheet()*//* }
            ) {*/
            Column(Modifier.fillMaxWidth()) {
                /*Toolbar(
                    title = {
                        val text = if (vm.showCategoryTabs) {
                            stringResource(R.string.library_label)
                        } else {
                            vm.selectedCategory?.visibleName.orEmpty()
                        }
                        Text(text)
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { sheetState.show() }}) {
                            Icon(Icons.Default.FilterList, contentDescription = null)
                        }
                    }
                )*/
                LibraryTabs(
                    visible = true, // vm.showCategoryTabs,
                    categories = categories,
                    selectedPage = selectedCategoryIndex,
                    onPageChanged = vm::setSelectedPage
                )
                LibraryPager(
                    categories = categories,
                    displayMode = displayMode,
                    selectedPage = selectedCategoryIndex,
                    serverUrl = serverUrl,
                    getLibraryForPage = { vm.getLibraryForCategoryIndex(it).collectAsState() },
                    onPageChanged = { vm.setSelectedPage(it) },
                    onClickManga = onClickManga
                )
            }
            // }
        }
    }
}

@Composable
private fun LibraryTabs(
    visible: Boolean,
    categories: List<Category>,
    selectedPage: Int,
    onPageChanged: (Int) -> Unit
) {
    if (categories.isEmpty()) return

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedPage,
            // backgroundColor = CustomColors.current.bars,
            // contentColor = CustomColors.current.onBars,
            edgePadding = 0.dp
        ) {
            categories.fastForEachIndexed { i, category ->
                Tab(
                    selected = selectedPage == i,
                    onClick = { onPageChanged(i) },
                    text = { Text(category.name) }
                )
            }
        }
    }
}

@Composable
private fun LibraryPager(
    categories: List<Category>,
    displayMode: DisplayMode,
    selectedPage: Int,
    serverUrl: String,
    getLibraryForPage: @Composable (Int) -> State<List<Manga>>,
    onPageChanged: (Int) -> Unit,
    onClickManga: (Long) -> Unit
) {
    if (categories.isEmpty()) return

    val state = rememberPagerState(categories.size, selectedPage)
    LaunchedEffect(state.currentPage) {
        if (state.currentPage != selectedPage) {
            onPageChanged(state.currentPage)
        }
    }
    LaunchedEffect(selectedPage) {
        if (state.currentPage != selectedPage) {
            state.animateScrollToPage(selectedPage)
        }
    }
    HorizontalPager(state = state) {
        val library by getLibraryForPage(it)
        when (displayMode) {
            DisplayMode.CompactGrid -> LibraryMangaCompactGrid(
                library = library,
                serverUrl = serverUrl,
                onClickManga = onClickManga
            )
            /*DisplayMode.ComfortableGrid -> LibraryMangaComfortableGrid(
                library = library,
                onClickManga = onClickManga
            )
            DisplayMode.List -> LibraryMangaList(
                library = library,
                onClickManga = onClickManga
            )*/
            else -> Box {}
        }
    }
}
