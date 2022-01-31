/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.ui.sources.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ca.gosyer.data.models.Source
import ca.gosyer.ui.manga.MangaScreen
import ca.gosyer.ui.sources.browse.components.SourceScreenContent
import ca.gosyer.ui.sources.browse.filter.SourceFiltersViewModel
import ca.gosyer.ui.sources.components.LocalSourcesNavigator
import ca.gosyer.ui.sources.settings.SourceSettingsScreen
import ca.gosyer.uicore.vm.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SourceScreen(val source: Source) : Screen {

    override val key: ScreenKey = source.id.toString()

    @Composable
    override fun Content() {
        val sourceVM = viewModel {
            instantiate<SourceScreenViewModel>(SourceScreenViewModel.Params(source))
        }
        val filterVM = viewModel {
            instantiate<SourceFiltersViewModel>(SourceFiltersViewModel.Params(source.id))
        }
        val navigator = LocalNavigator.currentOrThrow
        val sourcesNavigator = LocalSourcesNavigator.current
        SourceScreenContent(
            source = source,
            onMangaClick = { navigator push MangaScreen(it) },
            onCloseSourceTabClick = sourcesNavigator::remove,
            onSourceSettingsClick = { navigator push SourceSettingsScreen(it) },
            mangas = sourceVM.mangas.collectAsState().value,
            hasNextPage = sourceVM.hasNextPage.collectAsState().value,
            loading = sourceVM.loading.collectAsState().value,
            isLatest = sourceVM.isLatest.collectAsState().value,
            showLatestButton = source.supportsLatest,
            sourceSearchQuery = sourceVM.sourceSearchQuery.collectAsState().value,
            enableLatest = sourceVM::enableLatest,
            search = sourceVM::search,
            submitSearch = sourceVM::submitSearch,
            setMode = sourceVM::setMode,
            loadNextPage = sourceVM::loadNextPage,
            setUsingFilters = sourceVM::setUsingFilters,
            // FilterVM
            filters = filterVM.filters.collectAsState().value,
            showingFilters = filterVM.showingFilters.collectAsState().value,
            showFilterButton = filterVM.filterButtonEnabled.collectAsState().value,
            setShowingFilters = filterVM::showingFilters,
            resetFiltersClicked = {
                sourceVM.setUsingFilters(false)
                filterVM.resetFilters()
            }
        )
    }
}
