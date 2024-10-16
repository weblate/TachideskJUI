/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.uicore.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import dev.icerock.moko.resources.FileResource

@Composable
expect fun FileResource.rememberReadText(): String

@Composable
expect fun FileResource.readTextAsync(): State<String?>
