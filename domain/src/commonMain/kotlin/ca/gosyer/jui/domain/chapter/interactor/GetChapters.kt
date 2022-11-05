/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.domain.chapter.interactor

import ca.gosyer.jui.domain.chapter.service.ChapterRepository
import ca.gosyer.jui.domain.manga.model.Manga
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.singleOrNull
import me.tatarka.inject.annotations.Inject
import org.lighthousegames.logging.logging

class GetChapters @Inject constructor(private val chapterRepository: ChapterRepository) {

    suspend fun await(mangaId: Long, onError: suspend (Throwable) -> Unit = {}) = asFlow(mangaId)
        .catch {
            onError(it)
            log.warn(it) { "Failed to get chapters for $mangaId" }
        }
        .singleOrNull()

    suspend fun await(manga: Manga, onError: suspend (Throwable) -> Unit = {}) = asFlow(manga)
        .catch {
            onError(it)
            log.warn(it) { "Failed to get chapters for ${manga.title}(${manga.id})" }
        }
        .singleOrNull()

    fun asFlow(mangaId: Long) = chapterRepository.getChapters(mangaId)

    fun asFlow(manga: Manga) = chapterRepository.getChapters(manga.id)

    companion object {
        private val log = logging()
    }
}
