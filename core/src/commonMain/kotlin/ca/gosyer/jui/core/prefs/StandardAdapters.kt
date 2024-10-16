/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.core.prefs

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

interface Adapter<T> {
    fun get(
        key: String,
        preferences: ObservableSettings,
    ): T

    fun set(
        key: String,
        value: T,
        editor: ObservableSettings,
    )

    fun isSet(
        keys: Set<String>,
        key: String,
    ): Boolean = key in keys
}

internal object StringAdapter : Adapter<String> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): String {
        return preferences.getString(key, "") // Not called unless key is present.
    }

    override fun set(
        key: String,
        value: String,
        editor: ObservableSettings,
    ) {
        editor.putString(key, value)
    }
}

internal object LongAdapter : Adapter<Long> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): Long = preferences.getLong(key, 0)

    override fun set(
        key: String,
        value: Long,
        editor: ObservableSettings,
    ) {
        editor.putLong(key, value)
    }
}

internal object IntAdapter : Adapter<Int> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): Int = preferences.getInt(key, 0)

    override fun set(
        key: String,
        value: Int,
        editor: ObservableSettings,
    ) {
        editor.putInt(key, value)
    }
}

internal object FloatAdapter : Adapter<Float> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): Float = preferences.getFloat(key, 0f)

    override fun set(
        key: String,
        value: Float,
        editor: ObservableSettings,
    ) {
        editor.putFloat(key, value)
    }
}

internal object BooleanAdapter : Adapter<Boolean> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): Boolean = preferences.getBoolean(key, false)

    override fun set(
        key: String,
        value: Boolean,
        editor: ObservableSettings,
    ) {
        editor.putBoolean(key, value)
    }
}

internal object StringSetAdapter : Adapter<Set<String>> {
    private val serializer = SetSerializer(String.serializer())

    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): Set<String> {
        return preferences.decodeValue(serializer, key, emptySet()) // Not called unless key is present.
    }

    override fun set(
        key: String,
        value: Set<String>,
        editor: ObservableSettings,
    ) {
        editor.encodeValue(serializer, key, value)
    }

    /**
     *  Encoding a string set makes a list of keys and a size key, such as key.size and key.0-size
     */
    override fun isSet(
        keys: Set<String>,
        key: String,
    ): Boolean = keys.contains("$key.size")
}

internal class ObjectAdapter<T>(
    private val serializer: (T) -> String,
    private val deserializer: (String) -> T,
) : Adapter<T> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): T {
        return deserializer(preferences.getString(key, "")) // Not called unless key is present.
    }

    override fun set(
        key: String,
        value: T,
        editor: ObservableSettings,
    ) {
        editor.putString(key, serializer(value))
    }
}

internal class JsonObjectAdapter<T>(
    private val defaultValue: T,
    private val serializer: KSerializer<T>,
    private val serializersModule: SerializersModule = EmptySerializersModule(),
) : Adapter<T> {
    override fun get(
        key: String,
        preferences: ObservableSettings,
    ): T {
        return preferences.decodeValue(serializer, key, defaultValue, serializersModule) // Not called unless key is present.
    }

    override fun set(
        key: String,
        value: T,
        editor: ObservableSettings,
    ) {
        editor.encodeValue(serializer, key, value, serializersModule)
    }

    /**
     *  Encoding a structure makes keys start with the [key] and adds extensions for values,
     *  for a pair it would be like [key].first [key].second.
     */
    override fun isSet(
        keys: Set<String>,
        key: String,
    ): Boolean = keys.any { it.startsWith(key) }
}
