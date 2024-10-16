/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.reader.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import ca.gosyer.jui.domain.reader.model.TappingInvertMode
import ca.gosyer.jui.ui.reader.model.Navigation

@Immutable
abstract class ViewerNavigation {
    data class Rect(
        val xRange: IntRange,
        val yRange: IntRange,
    ) {
        private val right get() = xRange.last
        private val left get() = xRange.first
        private val bottom get() = yRange.last
        private val top get() = yRange.first

        constructor(left: Int, top: Int, right: Int, bottom: Int) :
            this(left..right, top..bottom)

        operator fun contains(offset: IntOffset): Boolean {
            val (x, y) = offset
            return x in xRange && y in yRange
        }

        fun invert(invertMode: TappingInvertMode): Rect {
            val horizontal = invertMode.shouldInvertHorizontal
            val vertical = invertMode.shouldInvertVertical
            return when {
                horizontal && vertical -> Rect(100 - this.right, 100 - this.bottom, 100 - this.left, 100 - this.top)
                vertical -> Rect(this.left, 100 - this.bottom, this.right, 100 - this.top)
                horizontal -> Rect(100 - this.right, this.top, 100 - this.left, this.bottom)
                else -> this
            }
        }
    }

    data class Region(
        val rect: Rect,
        val type: Navigation,
    ) {
        fun invert(invertMode: TappingInvertMode): Region {
            if (invertMode == TappingInvertMode.NONE) return this
            return this.copy(
                rect = this.rect.invert(invertMode),
            )
        }
    }

    abstract var regions: List<Region>

    var invertMode: TappingInvertMode = TappingInvertMode.NONE

    fun getAction(
        pos: Offset,
        layoutSize: Size,
    ): Navigation {
        val realX = pos.x / (layoutSize.width * 0.01F)
        val realY = pos.y / (layoutSize.height * 0.01F)
        val realPos = IntOffset(realX.toInt(), realY.toInt())

        val region = regions.map { it.invert(invertMode) }
            .find { realPos in it.rect }
        return when {
            region != null -> region.type
            else -> Navigation.MENU
        }
    }
}
