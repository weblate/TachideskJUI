/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.core.prefs

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * A wrapper around an application preferences store. Implementations of this interface should
 * persist these preferences on disk.
 */
interface PreferenceStore {
    /**
     * Returns an [String] preference for this [key].
     */
    fun getString(
        key: String,
        defaultValue: String = "",
    ): Preference<String>

    /**
     * Returns a [Long] preference for this [key].
     */
    fun getLong(
        key: String,
        defaultValue: Long = 0,
    ): Preference<Long>

    /**
     * Returns an [Int] preference for this [key].
     */
    fun getInt(
        key: String,
        defaultValue: Int = 0,
    ): Preference<Int>

    /**
     * Returns a [Float] preference for this [key].
     */
    fun getFloat(
        key: String,
        defaultValue: Float = 0f,
    ): Preference<Float>

    /**
     * Returns a [Boolean] preference for this [key].
     */
    fun getBoolean(
        key: String,
        defaultValue: Boolean = false,
    ): Preference<Boolean>

    /**
     * Returns a [Set<String>] preference for this [key].
     */
    fun getStringSet(
        key: String,
        defaultValue: Set<String> = emptySet(),
    ): Preference<Set<String>>

    /**
     * Returns preference of type [T] for this [key]. The [serializer] and [deserializer] function
     * must be provided.
     */
    fun <T> getObject(
        key: String,
        defaultValue: T,
        serializer: (T) -> String,
        deserializer: (String) -> T,
    ): Preference<T>

    /**
     * Returns preference of type [T] for this [key]. The [serializer] must be provided.
     */
    fun <T> getJsonObject(
        key: String,
        defaultValue: T,
        serializer: KSerializer<T>,
        serializersModule: SerializersModule = EmptySerializersModule(),
    ): Preference<T>
}

/**
 * Returns an enum preference of type [T] for this [key].
 */
inline fun <reified T : Enum<T>> PreferenceStore.getEnum(
    key: String,
    defaultValue: T,
): Preference<T> =
    getObject(
        key,
        defaultValue,
        { it.name },
        {
            try {
                enumValueOf(it)
            } catch (e: IllegalArgumentException) {
                defaultValue
            }
        },
    )
