/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.updates

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ca.gosyer.jui.ui.manga.MangaScreen
import ca.gosyer.jui.ui.reader.rememberReaderLauncher
import ca.gosyer.jui.ui.updates.components.UpdatesScreenContent
import ca.gosyer.jui.ui.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class UpdatesScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val vm = viewModel { updatesViewModel() }
        val updatesVM = viewModel { libraryUpdatesViewModel(false) }
        val navigator = LocalNavigator.currentOrThrow
        val readerLauncher = rememberReaderLauncher()
        UpdatesScreenContent(
            isLoading = vm.isLoading.collectAsState().value,
            updates = vm.updates.collectAsState().value,
            inActionMode = vm.inActionMode.collectAsState().value,
            selectedItems = vm.selectedItems.collectAsState().value,
            loadNextPage = vm::loadNextPage,
            openChapter = readerLauncher::launch,
            openManga = { navigator push MangaScreen(it) },
            markRead = vm::markRead,
            markUnread = vm::markUnread,
            bookmarkChapter = vm::bookmarkChapter,
            unBookmarkChapter = vm::unBookmarkChapter,
            downloadChapter = vm::downloadChapter,
            deleteDownloadedChapter = vm::deleteDownloadedChapter,
            stopDownloadingChapter = vm::stopDownloadingChapter,
            onSelectChapter = vm::selectChapter,
            onUnselectChapter = vm::unselectChapter,
            selectAll = vm::selectAll,
            invertSelection = vm::invertSelection,
            clearSelection = vm::clearSelection,
            onUpdateLibrary = vm::updateLibrary,
            updateWebsocketStatus = updatesVM.serviceStatus.collectAsState().value,
            restartLibraryUpdates = updatesVM::restartLibraryUpdates,
        )
        readerLauncher.Reader()
    }
}
