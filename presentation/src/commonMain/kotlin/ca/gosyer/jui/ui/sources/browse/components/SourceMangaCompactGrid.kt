/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.sources.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shadow
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
fun SourceMangaCompactGrid(
    mangas: ImmutableList<Manga>,
    gridColumns: Int,
    gridSize: Int,
    onClickManga: (Long) -> Unit,
    hasNextPage: Boolean = false,
    onLoadNextPage: () -> Unit,
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
            itemsIndexed(mangas) { index, manga ->
                if (hasNextPage && index == mangas.lastIndex) {
                    LaunchedEffect(Unit) { onLoadNextPage() }
                }
                SourceMangaCompactGridItem(
                    modifier = Modifier.clickable(
                        onClick = { onClickManga(manga.id) },
                    ),
                    manga = manga,
                    inLibrary = manga.inLibrary,
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
private fun SourceMangaCompactGridItem(
    modifier: Modifier,
    manga: Manga,
    inLibrary: Boolean,
) {
    Box(
        modifier = Modifier.padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(mangaAspectRatio)
            .clip(MaterialTheme.shapes.medium) then modifier,
    ) {
        ImageLoaderImage(
            manga,
            manga.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.Medium,
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color(0xAA000000),
                    ),
                )
                .fillMaxHeight(0.33f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            text = manga.title,
            color = Color.White,
            style = MaterialTheme.typography.subtitle2.copy(
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 4f,
                ),
            ),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        SourceMangaBadges(
            inLibrary = inLibrary,
            modifier = Modifier.padding(4.dp),
        )
    }
}
