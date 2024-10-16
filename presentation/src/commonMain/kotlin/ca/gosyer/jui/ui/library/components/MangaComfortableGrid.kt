/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.library.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gosyer.jui.domain.manga.model.Manga
import ca.gosyer.jui.ui.main.components.bottomNav
import ca.gosyer.jui.uicore.components.VerticalScrollbar
import ca.gosyer.jui.uicore.components.mangaAspectRatio
import ca.gosyer.jui.uicore.components.rememberVerticalScrollbarAdapter
import ca.gosyer.jui.uicore.components.scrollbarPadding
import ca.gosyer.jui.uicore.image.ImageLoaderImage
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LibraryMangaComfortableGrid(
    library: ImmutableList<Manga>,
    gridColumns: Int,
    gridSize: Int,
    onClickManga: (Long) -> Unit,
    onRemoveMangaClicked: (Long) -> Unit,
    showUnread: Boolean,
    showDownloaded: Boolean,
    showLanguage: Boolean,
    showLocal: Boolean,
) {
    Box {
        val state = rememberLazyGridState()
        val cells = if (gridColumns < 1) {
            GridCells.Adaptive(gridSize.dp)
        } else {
            GridCells.Fixed(gridColumns)
        }
        LazyVerticalGrid(
            columns = cells,
            state = state,
            modifier = Modifier.fillMaxSize().padding(4.dp),
            contentPadding = WindowInsets.bottomNav.add(
                WindowInsets.navigationBars.only(
                    WindowInsetsSides.Bottom,
                ),
            ).asPaddingValues(),
        ) {
            items(library) { manga ->
                LibraryMangaComfortableGridItem(
                    modifier = Modifier.libraryMangaModifier(
                        { onClickManga(manga.id) },
                        { onRemoveMangaClicked(manga.id) },
                    ),
                    manga = manga,
                    showUnread = showUnread,
                    showDownloaded = showDownloaded,
                    showLanguage = showLanguage,
                    showLocal = showLocal,
                )
            }
        }
        VerticalScrollbar(
            rememberVerticalScrollbarAdapter(state, cells),
            Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight()
                .scrollbarPadding()
                .windowInsetsPadding(
                    WindowInsets.bottomNav.add(
                        WindowInsets.navigationBars.only(
                            WindowInsetsSides.Bottom,
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun LibraryMangaComfortableGridItem(
    modifier: Modifier,
    manga: Manga,
    showUnread: Boolean,
    showDownloaded: Boolean,
    showLanguage: Boolean,
    showLocal: Boolean,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium) then modifier,
    ) {
        Column {
            ImageLoaderImage(
                manga,
                contentDescription = manga.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(mangaAspectRatio)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.Medium,
            )
            Text(
                text = manga.title,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                style = MaterialTheme.typography.subtitle2,
            )
        }
        LibraryMangaBadges(
            modifier = Modifier.padding(4.dp),
            manga = manga,
            showUnread = showUnread,
            showDownloaded = showDownloaded,
            showLanguage = showLanguage,
            showLocal = showLocal,
        )
    }
}
