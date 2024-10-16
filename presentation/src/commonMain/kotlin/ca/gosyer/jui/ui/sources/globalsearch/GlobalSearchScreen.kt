/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.sources.globalsearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ca.gosyer.jui.ui.base.screen.BaseScreen
import ca.gosyer.jui.ui.manga.MangaScreen
import ca.gosyer.jui.ui.sources.browse.SourceScreen
import ca.gosyer.jui.ui.sources.components.LocalSourcesNavigator
import ca.gosyer.jui.ui.sources.globalsearch.components.GlobalSearchScreenContent
import ca.gosyer.jui.ui.stateViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class GlobalSearchScreen(
    private val initialQuery: String,
) : BaseScreen() {
    @Composable
    override fun Content() {
        val vm = stateViewModel {
            globalSearchViewModel(it, GlobalSearchViewModel.Params(initialQuery))
        }
        val sourcesNavigator = LocalSourcesNavigator.current
        val navigator = LocalNavigator.currentOrThrow

        GlobalSearchScreenContent(
            sources = vm.sources.collectAsState().value,
            results = vm.results,
            displayMode = vm.displayMode.collectAsState().value,
            query = vm.query.collectAsState().value,
            setQuery = vm::setQuery,
            submitSearch = vm::startSearch,
            onSourceClick = {
                if (sourcesNavigator != null) {
                    sourcesNavigator.open(it, vm.query.value)
                } else {
                    navigator push SourceScreen(it, vm.query.value)
                }
            },
            onMangaClick = {
                navigator push MangaScreen(it.id)
            },
        )
    }
}
