/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.uicore.prefs

import ca.gosyer.jui.core.prefs.Preference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun Preference<Int>.asStringStateIn(scope: CoroutineScope): PreferenceMutableStateFlow<String> =
    PreferenceMutableStateFlow(
        IntStringPreference(this),
        scope,
    )

class IntStringPreference(
    private val int: Preference<Int>,
) : Preference<String> {
    override fun key(): String = int.key()

    override fun get(): String = int.get().toString()

    override fun set(value: String) {
        value.toIntOrNull()?.let { int.set(it) }
    }

    override fun isSet(): Boolean = int.isSet()

    override fun delete() {
        int.delete()
    }

    override fun defaultValue(): String = int.defaultValue().toString()

    override fun changes(): Flow<String> = int.changes().map { it.toString() }

    override fun stateIn(scope: CoroutineScope): StateFlow<String> = changes().stateIn(scope, SharingStarted.Eagerly, get())
}
