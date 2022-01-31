/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.ui.library

import ca.gosyer.core.lang.throwIfCancellation
import ca.gosyer.core.lang.withDefaultContext
import ca.gosyer.data.library.LibraryPreferences
import ca.gosyer.data.models.Category
import ca.gosyer.data.models.Manga
import ca.gosyer.data.server.interactions.CategoryInteractionHandler
import ca.gosyer.data.server.interactions.LibraryInteractionHandler
import ca.gosyer.data.server.interactions.UpdatesInteractionHandler
import ca.gosyer.uicore.vm.ViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

private typealias CategoryItems = Pair<MutableStateFlow<List<Manga>>, MutableStateFlow<List<Manga>>>
private typealias LibraryMap = MutableMap<Long, CategoryItems>
private data class Library(val categories: MutableStateFlow<List<Category>>, val mangaMap: LibraryMap)

private fun LibraryMap.getManga(id: Long) =
    getOrPut(id) { MutableStateFlow(emptyList<Manga>()) to MutableStateFlow(emptyList()) }
private suspend fun LibraryMap.setManga(query: String?, id: Long, manga: List<Manga>) {
    getManga(id).let { (items, unfilteredItems) ->
        items.value = filterManga(query, manga)
        unfilteredItems.value = manga
    }
}
private suspend fun LibraryMap.updateMangaFilter(query: String?) {
    values.forEach { (items, unfilteredItems) ->
        items.value = filterManga(query, unfilteredItems.value)
    }
}

private suspend fun filterManga(query: String?, mangaList: List<Manga>): List<Manga> {
    if (query.isNullOrBlank()) return mangaList
    val queries = query.split(" ")
    return mangaList.asFlow()
        .filter { manga ->
            queries.all { query ->
                manga.title.contains(query, true) ||
                    manga.author.orEmpty().contains(query, true) ||
                    manga.artist.orEmpty().contains(query, true) ||
                    manga.genre.any { it.contains(query, true) } ||
                    manga.description.orEmpty().contains(query, true) ||
                    manga.status.name.contains(query, true)
            }
        }
        .cancellable()
        .buffer()
        .toList()
}

class LibraryScreenViewModel @Inject constructor(
    private val categoryHandler: CategoryInteractionHandler,
    private val libraryHandler: LibraryInteractionHandler,
    private val updatesHandler: UpdatesInteractionHandler,
    libraryPreferences: LibraryPreferences
) : ViewModel() {
    private val library = Library(MutableStateFlow(emptyList()), mutableMapOf())
    val categories = library.categories.asStateFlow()

    private val _selectedCategoryIndex = MutableStateFlow(0)
    val selectedCategoryIndex = _selectedCategoryIndex.asStateFlow()

    val displayMode = libraryPreferences.displayMode().stateIn(scope)

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        getLibrary()

        _query.mapLatest {
            library.mangaMap.updateMangaFilter(it)
        }.launchIn(scope)
    }

    private fun getLibrary() {
        scope.launch {
            _isLoading.value = true
            try {
                val categories = categoryHandler.getCategories()
                if (categories.isEmpty()) {
                    throw Exception("Library is empty")
                }
                library.categories.value = categories.sortedBy { it.order }
                updateCategories(categories)
            } catch (e: Exception) {
                e.throwIfCancellation()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSelectedPage(page: Int) {
        _selectedCategoryIndex.value = page
    }

    fun getLibraryForCategoryId(id: Long): StateFlow<List<Manga>> {
        return library.mangaMap.getManga(id).first.asStateFlow()
    }

    private suspend fun updateCategories(categories: List<Category>) {
        withDefaultContext {
            categories.map {
                async {
                    library.mangaMap.setManga(query.value, it.id, categoryHandler.getMangaFromCategory(it))
                }
            }.awaitAll()
        }
    }

    private fun getCategoriesToUpdate(mangaId: Long): List<Category> {
        return library.mangaMap
            .filter { mangaMapEntry ->
                mangaMapEntry.value.first.value.firstOrNull { it.id == mangaId } != null
            }
            .map { (id) -> library.categories.value.first { it.id == id } }
    }

    fun removeManga(mangaId: Long) {
        scope.launch {
            libraryHandler.removeMangaFromLibrary(mangaId)
            updateCategories(getCategoriesToUpdate(mangaId))
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    fun updateLibrary() {
        scope.launch {
            updatesHandler.updateLibrary()
        }
    }

    fun updateCategory(category: Category) {
        scope.launch {
            updatesHandler.updateCategory(category)
        }
    }

    companion object {
        const val QUERY_KEY = "query"
        const val SELECTED_CATEGORY_KEY = "selected_category"
    }
}
