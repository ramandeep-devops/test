package com.codebrew.encober.utils.local

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringDef
import com.google.gson.Gson
import java.util.concurrent.atomic.AtomicBoolean

class PrefsManager private constructor(context: Context)
{
    private val PREF_NAME = "Encober"
    private val GSON = Gson()
    private var preferences: SharedPreferences

    companion object
    {
        const val LANGUAGE_CODE = 1
        const val PREF_LANGUAGE = "en"

//        const val LANGUAGE_CODE = 2
//        const val PREF_LANGUAGE = "es"

        const val PREF_USER_PROFILE = "PREF_USER_PROFILE"
        const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        const val PREF_DEVICE_TOKEN = "PREF_DEVICE_TOKEN"
        const val PREF_USER_TYPE= "PREF_USER_TYPE"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"

        @StringDef(
            PREF_USER_PROFILE,
            PREF_ACCESS_TOKEN,
            PREF_DEVICE_TOKEN,
            PREF_USER_TYPE,
            LATITUDE,
            LONGITUDE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class PrefKey

        private lateinit var instance: PrefsManager
        private val isInitialized = AtomicBoolean()     // To check if instance was previously initialized or not

        fun initialize(context: Context)
        {
            //if (!isInitialized.getAndSet(true))
            //{
            instance = PrefsManager(context.applicationContext)
            //}
        }

        fun get(): PrefsManager = instance
    }

    init
    {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun save(@PrefKey key: String, value: String)
    {
        preferences.edit().putString(key, value).apply()
    }

    fun save(@PrefKey key: String, value: Int)
    {
        preferences.edit().putInt(key, value).apply()
    }

    fun save(@PrefKey key: String, value: Boolean)
    {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun save(@PrefKey key: String, `object`: Any?)
    {
        if (`object` == null)
        {
            throw IllegalArgumentException("object is null")
        }

        // Convert the provided object to JSON string
        save(key, GSON.toJson(`object`))
    }

    fun getString(@PrefKey key: String, defValue: String): String = preferences.getString(key, defValue)?:defValue

    fun getInt(@PrefKey key: String, defValue: Int): Int = preferences.getInt(key, defValue)

    fun getBoolean(@PrefKey key: String, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

    fun <T> getObject(@PrefKey key: String, objectClass: Class<T>): T?
    {
        val jsonString = preferences.getString(key, null)
        return if (jsonString == null)
        {
            null
        }
        else
        {
            try
            {
                GSON.fromJson(jsonString, objectClass)
            }
            catch (e: Exception)
            {
                throw IllegalArgumentException("Object stored with key $key is instance of other class")
            }
        }
    }

    fun removeAll()
    {
        preferences.edit().clear().apply()
    }

    fun remove(key: String)
    {
        preferences.edit().remove(key).apply()
    }
}