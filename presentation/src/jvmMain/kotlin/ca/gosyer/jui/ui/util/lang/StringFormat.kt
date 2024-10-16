/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.util.lang

import java.util.Formatter

actual fun stringFormat(
    string: String,
    vararg args: Any?,
): String =
    Formatter().use {
        it.format(string, *args).toString()
    }
