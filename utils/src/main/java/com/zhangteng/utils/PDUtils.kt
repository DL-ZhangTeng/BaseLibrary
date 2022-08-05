package com.zhangteng.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.currentUserDataStore by preferencesDataStore("currentUser")

/**
 * description 异步输入数据
 */
fun <Value> Context.putToPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    value: Value
) {
    when (value) {
        is Int -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[intPreferencesKey(key)] = value
            }
        }
        is Long -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[longPreferencesKey(key)] = value
            }
        }
        is Float -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[floatPreferencesKey(key)] = value
            }
        }
        is Double -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[doublePreferencesKey(key)] = value
            }
        }
        is Boolean -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[booleanPreferencesKey(key)] = value
            }
        }
        is String -> runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[stringPreferencesKey(key)] = value
            }
        }
        else -> throw IllegalArgumentException("unSupport $value type !!!")
    }
}

/**
 * description 同步输入数据
 */
suspend fun <Value> Context.putSyncToPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    value: Value
) {
    when (value) {
        is Int -> dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
        is Long -> dataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
        is Float -> dataStore.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
        is Double -> dataStore.edit { mutablePreferences ->
            mutablePreferences[doublePreferencesKey(key)] = value
        }
        is Boolean -> dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
        is String -> dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
        else -> throw IllegalArgumentException("unSupport $value type !!!")
    }
}

/**
 * description 异步输入数据
 */
fun Context.putToPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    value: Set<String>
) = runBlocking {
    putSyncToPD(dataStore, key, value)
}

/**
 * description 同步输入数据
 */
suspend fun Context.putSyncToPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    value: Set<String>
) {
    dataStore.edit { mutablePreferences ->
        mutablePreferences[stringSetPreferencesKey(key)] = value
    }
}

/**
 *
 * description 异步获取数据
 */
@Suppress("UNCHECKED_CAST")
fun <Value> Context.getFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    defaultValue: Value
): Value {
    val result = when (defaultValue) {
        is Int -> {
            var resultValue: Int = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[intPreferencesKey(key)] ?: resultValue
                    true
                }
            }
            resultValue
        }
        is Long -> {
            var resultValue: Long = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[longPreferencesKey(key)] ?: resultValue
                    true
                }
            }
            resultValue
        }
        is Float -> {
            var resultValue: Float = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[floatPreferencesKey(key)] ?: resultValue
                    true
                }
            }
            resultValue
        }
        is Double -> {
            var resultValue: Double = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[doublePreferencesKey(key)] ?: resultValue
                    true
                }
            }
            resultValue
        }
        is Boolean -> {
            var resultValue: Boolean = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[booleanPreferencesKey(key)] ?: resultValue
                    true
                }
            }
            resultValue
        }
        is String -> {
            var resultValue: String = defaultValue
            runBlocking {
                dataStore.data.first {
                    resultValue = it[stringPreferencesKey(key)] ?: defaultValue
                    true
                }
            }
            resultValue
        }
        else -> throw IllegalArgumentException("can not find the $key type")
    }

    return result as Value
}

/**
 * description 同步获取数据
 */
@Suppress("UNCHECKED_CAST")
fun <Value> Context.getSyncFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    defaultValue: Value
): Flow<Value> {
    val result = when (defaultValue) {
        is Int -> dataStore.data.map { it[intPreferencesKey(key)] ?: defaultValue }
        is Long -> dataStore.data.map { it[longPreferencesKey(key)] ?: defaultValue }
        is Float -> dataStore.data.map { it[floatPreferencesKey(key)] ?: defaultValue }
        is Double -> dataStore.data.map { it[doublePreferencesKey(key)] ?: defaultValue }
        is Boolean -> dataStore.data.map { it[booleanPreferencesKey(key)] ?: defaultValue }
        is String -> dataStore.data.map { it[stringPreferencesKey(key)] ?: defaultValue }
        else -> throw IllegalArgumentException("can Not find the $key type")
    }
    return result as Flow<Value>
}

/**
 * description 异步获取数据
 */
fun Context.getFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    defaultValue: Set<String> = HashSet()
): Set<String> {
    var resultValue = defaultValue
    runBlocking {
        dataStore.data.first {
            resultValue = it[stringSetPreferencesKey(key)] ?: defaultValue
            true
        }
    }
    return resultValue
}

/**
 * description 同步获取数据
 */
fun Context.getSyncFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    defaultValue: Set<String> = HashSet()
): Flow<Set<String>> = dataStore.data.map { it[stringSetPreferencesKey(key)] ?: defaultValue }

/**
 * description 异步获取全部数据
 */
fun Context.getFromPDForAll(
    dataStore: DataStore<Preferences> = currentUserDataStore
): Map<Preferences.Key<*>, Any> {
    var resultValue: Map<Preferences.Key<*>, Any> = HashMap()
    runBlocking {
        dataStore.data.map {
            resultValue = it.asMap()
        }
    }
    return resultValue
}

/**
 * description 同步获取全部数据
 */
fun Context.getSyncFromPDForAll(
    dataStore: DataStore<Preferences> = currentUserDataStore
): Flow<Map<Preferences.Key<*>, Any>> = dataStore.data.map { it.asMap() }

/**
 *
 * description 异步移除数据
 * @param valueType 值类型
 * @return 与该键相关联的前一个值，如果该键不在映射中，则为“null”
 */
@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun <V> Context.removeFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    valueType: Class<*>
): V? {
    val result = when (valueType) {
        Int::class.java -> {
            var resultValue: Int? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(intPreferencesKey(key))
                }
            }
            resultValue
        }
        Long::class.java -> {
            var resultValue: Long? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(longPreferencesKey(key))
                }
            }
            resultValue
        }
        Float::class.java -> {
            var resultValue: Float? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(floatPreferencesKey(key))
                }
            }
            resultValue
        }
        Double::class.java -> {
            var resultValue: Double? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(doublePreferencesKey(key))
                }
            }
            resultValue
        }
        Boolean::class.java -> {
            var resultValue: Boolean? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(booleanPreferencesKey(key))
                }
            }
            resultValue
        }
        String::class.java -> {
            var resultValue: String? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(stringPreferencesKey(key))
                }
            }
            resultValue
        }
        Set::class.java -> {
            var resultValue: Set<String>? = null
            runBlocking {
                dataStore.edit {
                    resultValue = it.remove(stringSetPreferencesKey(key))
                }
            }
            resultValue
        }
        else -> throw IllegalArgumentException("can not find the $key type")
    }

    return result as V?
}

/**
 *
 * description 同步移除数据
 * @param valueType 值类型
 * @return 与该键相关联的前一个值，如果该键不在映射中，则为“null”
 */
@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
suspend fun <V> Context.removeSyncFromPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    valueType: Class<*>
): V? {
    val result = when (valueType) {
        Int::class.java -> {
            var resultValue: Int? = null
            dataStore.edit {
                resultValue = it.remove(intPreferencesKey(key))
            }
            resultValue
        }
        Long::class.java -> {
            var resultValue: Long? = null
            dataStore.edit {
                resultValue = it.remove(longPreferencesKey(key))
            }
            resultValue
        }
        Float::class.java -> {
            var resultValue: Float? = null
            dataStore.edit {
                resultValue = it.remove(floatPreferencesKey(key))
            }
            resultValue
        }
        Double::class.java -> {
            var resultValue: Double? = null
            dataStore.edit {
                resultValue = it.remove(doublePreferencesKey(key))
            }
            resultValue
        }
        Boolean::class.java -> {
            var resultValue: Boolean? = null
            dataStore.edit {
                resultValue = it.remove(booleanPreferencesKey(key))
            }
            resultValue
        }
        String::class.java -> {
            var resultValue: String? = null
            dataStore.edit {
                resultValue = it.remove(stringPreferencesKey(key))
            }
            resultValue
        }
        Set::class.java -> {
            var resultValue: Set<String>? = null
            dataStore.edit {
                resultValue = it.remove(stringSetPreferencesKey(key))
            }
            resultValue
        }
        else -> throw IllegalArgumentException("can not find the $key type")
    }

    return result as V?
}

/**
 * description 异步清除数据
 */
fun Context.clearFromPD() {
    runBlocking {
        clearSyncFromPD()
    }
}

/**
 * description 同步清除数据
 */
suspend fun Context.clearSyncFromPD(dataStore: DataStore<Preferences> = currentUserDataStore) {
    dataStore.edit { it.clear() }
}

/**
 *
 * description 异步是否包含指定Key
 * @param valueType 值类型
 * @return 如果映射包含指定的[key]，则返回' true
 */
@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun Context.containsInPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    valueType: Class<*>
): Boolean {
    val result = when (valueType) {
        Int::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(intPreferencesKey(key))
                }
            }
            resultValue
        }
        Long::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(longPreferencesKey(key))
                }
            }
            resultValue
        }
        Float::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(floatPreferencesKey(key))
                }
            }
            resultValue
        }
        Double::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(doublePreferencesKey(key))
                }
            }
            resultValue
        }
        Boolean::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(booleanPreferencesKey(key))
                }
            }
            resultValue
        }
        String::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(stringPreferencesKey(key))
                }
            }
            resultValue
        }
        Set::class.java -> {
            var resultValue = false
            runBlocking {
                dataStore.edit {
                    resultValue = it.contains(stringSetPreferencesKey(key))
                }
            }
            resultValue
        }
        else -> throw IllegalArgumentException("can not find the $key type")
    }

    return result
}

/**
 *
 * description 同步是否包含指定Key
 * @param valueType 值类型
 * @return 如果映射包含指定的[key]，则返回' true
 */
@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
suspend fun Context.containsSyncInPD(
    dataStore: DataStore<Preferences> = currentUserDataStore,
    key: String,
    valueType: Class<*>
): Boolean {
    val result = when (valueType) {
        Int::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(intPreferencesKey(key))
            }
            resultValue
        }
        Long::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(longPreferencesKey(key))
            }
            resultValue
        }
        Float::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(floatPreferencesKey(key))
            }
            resultValue
        }
        Double::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(doublePreferencesKey(key))
            }
            resultValue
        }
        Boolean::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(booleanPreferencesKey(key))
            }
            resultValue
        }
        String::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(stringPreferencesKey(key))
            }
            resultValue
        }
        Set::class.java -> {
            var resultValue = false
            dataStore.edit {
                resultValue = it.contains(stringSetPreferencesKey(key))
            }
            resultValue
        }
        else -> throw IllegalArgumentException("can not find the $key type")
    }

    return result
}