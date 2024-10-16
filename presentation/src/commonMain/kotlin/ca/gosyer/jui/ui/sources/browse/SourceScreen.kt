/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.sources.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ca.gosyer.jui.domain.source.model.Source
import ca.gosyer.jui.ui.base.screen.BaseScreen
import ca.gosyer.jui.ui.manga.MangaScreen
import ca.gosyer.jui.ui.sources.browse.components.SourceScreenContent
import ca.gosyer.jui.ui.sources.browse.filter.SourceFiltersViewModel
import ca.gosyer.jui.ui.sources.components.LocalSourcesNavigator
import ca.gosyer.jui.ui.sources.settings.SourceSettingsScreen
import ca.gosyer.jui.ui.stateViewModel
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SourceScreen(
    val source: Source,
    private val initialQuery: String? = null,
) : BaseScreen() {
    override val key: ScreenKey = source.id.toString()

    @Composable
    override fun Content() {
        val sourceVM = stateViewModel {
            sourceViewModel(it, SourceScreenViewModel.Params(source, initialQuery))
        }
        val filterVM = stateViewModel {
            sourceFiltersViewModel(it, SourceFiltersViewModel.Params(source.id))
        }
        val sourcesNavigator = LocalSourcesNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        SourceScreenContent(
            source = source,
            onMangaClick = { navigator push MangaScreen(it) },
            onCloseSourceTabClick = if (sourcesNavigator != null) {
                { sourcesNavigator.remove(it) }
            } else {
                {
                    navigator.pop()
                }
            },
            onSourceSettingsClick = { navigator push SourceSettingsScreen(it) },
            displayMode = sourceVM.displayMode.collectAsState().value,
            gridColumns = sourceVM.gridColumns.collectAsState().value,
            gridSize = sourceVM.gridSize.collectAsState().value,
            mangas = sourceVM.mangas.collectAsState().value,
            hasNextPage = sourceVM.hasNextPage.collectAsState().value,
            loading = sourceVM.loading.collectAsState().value,
            isLatest = sourceVM.isLatest.collectAsState().value,
            showLatestButton = source.supportsLatest,
            sourceSearchQuery = sourceVM.sourceSearchQuery.collectAsState().value,
            search = sourceVM::search,
            submitSearch = sourceVM::submitSearch,
            setMode = sourceVM::setMode,
            loadNextPage = sourceVM::loadNextPage,
            setUsingFilters = sourceVM::setUsingFilters,
            onSelectDisplayMode = sourceVM::selectDisplayMode,
            // FilterVM
            filters = filterVM.filters.collectAsState().value,
            showingFilters = filterVM.showingFilters.collectAsState().value,
            showFilterButton = filterVM.filterButtonEnabled.collectAsState().value,
            setShowingFilters = filterVM::showingFilters,
            resetFiltersClicked = {
                sourceVM.setUsingFilters(false)
                filterVM.resetFilters()
            },
        )
    }
}
