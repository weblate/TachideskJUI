/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.base.navigation

import androidx.compose.runtime.Composable

@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
) = RealBackHandler(enabled, onBack)

@Composable
internal expect fun RealBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
