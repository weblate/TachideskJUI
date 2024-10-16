/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.domain.reader.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
@Stable
enum class TappingInvertMode(
    val shouldInvertHorizontal: Boolean = false,
    val shouldInvertVertical: Boolean = false,
) {
    NONE,
    HORIZONTAL(shouldInvertHorizontal = true),
    VERTICAL(shouldInvertVertical = true),
    BOTH(shouldInvertHorizontal = true, shouldInvertVertical = true),
}
