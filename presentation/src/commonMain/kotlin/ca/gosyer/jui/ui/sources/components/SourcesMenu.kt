/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.sources.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ca.gosyer.jui.i18n.MR
import ca.gosyer.jui.ui.base.components.CursorPoint
import ca.gosyer.jui.ui.base.components.TooltipArea
import ca.gosyer.jui.ui.base.model.StableHolder
import ca.gosyer.jui.ui.main.components.bottomNav
import ca.gosyer.jui.ui.sources.home.SourceHomeScreen
import ca.gosyer.jui.uicore.components.VerticalScrollbar
import ca.gosyer.jui.uicore.components.rememberScrollbarAdapter
import ca.gosyer.jui.uicore.components.scrollbarPadding
import ca.gosyer.jui.uicore.image.ImageLoaderImage
import ca.gosyer.jui.uicore.resources.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.collections.immutable.ImmutableList

expect fun Modifier.sourceSideMenuItem(
    onSourceTabClick: () -> Unit,
    onSourceCloseTabClick: () -> Unit,
): Modifier

@Composable
fun SourcesMenu() {
    val homeScreenHolder = remember { StableHolder(SourceHomeScreen()) }
    BoxWithConstraints {
        if (maxWidth > 720.dp) {
            SourcesNavigator(
                homeScreenHolder,
            ) { navigator ->
                Row {
                    SourcesSideMenu(
                        sourceTabs = navigator.tabs,
                        onSourceTabClick = {
                            when (it) {
                                SourceNavigatorScreen.HomeScreen -> navigator.goHome()
                                SourceNavigatorScreen.SearchScreen -> navigator.goToSearch()
                                is SourceNavigatorScreen.SourceScreen -> navigator.select(it.source)
                            }
                        },
                        onCloseSourceTabClick = {
                            navigator.remove(it.source)
                        },
                    )

                    CurrentSource()
                }
            }
        } else {
            LocalNavigator.currentOrThrow.saveableState("sourcesHome", homeScreenHolder.item) {
                homeScreenHolder.item.Content()
            }
        }
    }
}

@Composable
fun SourcesSideMenu(
    sourceTabs: ImmutableList<SourceNavigatorScreen>,
    onSourceTabClick: (SourceNavigatorScreen) -> Unit,
    onCloseSourceTabClick: (SourceNavigatorScreen.SourceScreen) -> Unit,
) {
    Surface(elevation = 1.dp) {
        Box {
            val state = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxHeight().width(64.dp),
                state = state,
                contentPadding = WindowInsets.bottomNav.add(
                    WindowInsets.navigationBars.only(
                        WindowInsetsSides.Bottom,
                    ),
                ).asPaddingValues(),
            ) {
                items(sourceTabs) { screen ->
                    TooltipArea(
                        {
                            Surface(
                                modifier = Modifier.shadow(4.dp),
                                shape = RoundedCornerShape(4.dp),
                                elevation = 4.dp,
                            ) {
                                Text(
                                    when (screen) {
                                        SourceNavigatorScreen.HomeScreen -> stringResource(MR.strings.sources_home)
                                        SourceNavigatorScreen.SearchScreen -> stringResource(MR.strings.location_global_search)
                                        is SourceNavigatorScreen.SourceScreen -> screen.source.name
                                    },
                                    modifier = Modifier.padding(10.dp),
                                )
                            }
                        },
                        modifier = Modifier.size(64.dp),
                        tooltipPlacement = CursorPoint(
                            offset = DpOffset(0.dp, 16.dp),
                        ),
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            val modifier = Modifier
                                .sourceSideMenuItem(
                                    onSourceTabClick = {
                                        onSourceTabClick(screen)
                                    },
                                    onSourceCloseTabClick = {
                                        if (screen is SourceNavigatorScreen.SourceScreen) {
                                            onCloseSourceTabClick(screen)
                                        }
                                    },
                                )
                                .requiredSize(50.dp)
                                .align(Alignment.Center)
                            when (screen) {
                                SourceNavigatorScreen.HomeScreen -> Icon(
                                    imageVector = Icons.Rounded.Home,
                                    contentDescription = stringResource(MR.strings.sources_home),
                                    modifier = modifier,
                                )

                                SourceNavigatorScreen.SearchScreen -> Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = stringResource(MR.strings.location_global_search),
                                    modifier = modifier,
                                )

                                is SourceNavigatorScreen.SourceScreen -> Box(Modifier.align(Alignment.Center)) {
                                    ImageLoaderImage(
                                        data = screen.source,
                                        contentDescription = screen.source.displayName,
                                        modifier = modifier,
                                        filterQuality = FilterQuality.Medium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            VerticalScrollbar(
                rememberScrollbarAdapter(state),
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
}
