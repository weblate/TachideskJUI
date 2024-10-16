/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.base.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
expect interface TooltipPlacement

fun CursorPoint(
    offset: DpOffset = DpOffset.Zero,
    alignment: Alignment = Alignment.BottomEnd,
    windowMargin: Dp = 4.dp,
) = CursorPointImpl(offset, alignment, windowMargin)

@ExperimentalFoundationApi
expect class CursorPointImpl(
    offset: DpOffset,
    alignment: Alignment,
    windowMargin: Dp,
) : TooltipPlacement

fun ComponentRect(
    anchor: Alignment = Alignment.BottomCenter,
    alignment: Alignment = Alignment.BottomCenter,
    offset: DpOffset = DpOffset.Zero,
) = ComponentRectImpl(anchor, alignment, offset)

@ExperimentalFoundationApi
expect class ComponentRectImpl(
    anchor: Alignment,
    alignment: Alignment,
    offset: DpOffset,
) : TooltipPlacement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipArea(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    delayMillis: Int = 500,
    tooltipPlacement: TooltipPlacement = CursorPoint(
        offset = DpOffset(0.dp, 16.dp),
    ),
    content: @Composable () -> Unit,
) = RealTooltipArea(
    tooltip = tooltip,
    modifier = modifier,
    delayMillis = delayMillis,
    tooltipPlacement = tooltipPlacement,
    content = content,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal expect fun RealTooltipArea(
    tooltip: @Composable () -> Unit,
    modifier: Modifier,
    delayMillis: Int,
    tooltipPlacement: TooltipPlacement,
    content: @Composable () -> Unit,
)
