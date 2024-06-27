package com.app.ehealthaidoctor.utils

import android.content.Context
import android.content.SharedPreferences
import com.app.ehealthaidoctor.models.responses.LoginData
import com.app.ehealthaidoctor.models.responses.Profile
import com.google.gson.Gson


class SharedPrefs {
    companion object {
        private val SHARED_PREFS_FILE_NAME = "eHealthAI"
        val USER_DATA = "user_data"
        val LANGUAGE = "lang"
        val TOKEN = "Token"


        fun getLanguage(context: Context): String? {
            return SharedPrefs.getString(context, LANGUAGE)
        }

        fun setLanguage(context: Context, lang: String) {
            SharedPrefs.save(context, LANGUAGE, lang)
        }


        fun getToken(context: Context): String? {
            return SharedPrefs.getString(context, TOKEN)
        }

        fun setToken(context: Context, lang: String) {
            SharedPrefs.save(context, TOKEN, lang)
        }


        fun setUserData(context: Context, userData: Profile) {
            SharedPrefs.save(context, SharedPrefs.USER_DATA, Gson().toJson(userData))
        }


        fun removeUserData(context: Context) {
            SharedPrefs.removeKey(context, SharedPrefs.USER_DATA)

        }

        fun deleteAll(context: Context) {
            getPrefs(context).edit().clear().commit()
        }


        fun getUserData(context: Context): Profile? {

            return if (getPrefs(context).contains(USER_DATA)) {
                Gson().fromJson<Profile>(
                    SharedPrefs.getString(context, USER_DATA),
                    Profile::class.java
                )

            } else null

        }

        fun getUserDataPro(context: Context): LoginData? {

            return if (getPrefs(context).contains(USER_DATA)) {
                Gson().fromJson<LoginData>(
                    SharedPrefs.getString(context, USER_DATA),
                    LoginData::class.java
                )

            } else null

        }


        //get the SharedPreferences object instance
        //create SharedPreferences file if not present


        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_MULTI_PROCESS)
        }

        //Save Booleans
        fun savePref(context: Context, key: String, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).commit()
        }

        //Get Booleans
        fun getBoolean(context: Context, key: String): Boolean {
            return getPrefs(context).getBoolean(key, false)
        }

        //Get Booleans if not found return a predefined default value
        fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
            return getPrefs(context).getBoolean(key, defaultValue)
        }

        //Strings
        fun save(context: Context, key: String, value: String) {
            getPrefs(context).edit().putString(key, value).commit()
        }

        fun save(context: Context, key: String, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).commit()
        }

        fun getString(context: Context, key: String): String? {
            return getPrefs(context).getString(key, "")
        }

        fun getString(context: Context, key: String, defaultValue: String): String? {
            return getPrefs(context).getString(key, defaultValue)
        }

        //Integers
        fun save(context: Context, key: String, value: Int) {
            getPrefs(context).edit().putInt(key, value).commit()
        }

        fun getInt(context: Context, key: String): Int {
            return getPrefs(context).getInt(key, 0)
        }

        fun getInt(context: Context, key: String, defaultValue: Int): Int {
            return getPrefs(context).getInt(key, defaultValue)
        }

        //Floats
        fun save(context: Context, key: String, value: Float) {
            getPrefs(context).edit().putFloat(key, value).commit()
        }

        fun getFloat(context: Context, key: String): Float {
            return getPrefs(context).getFloat(key, 0f)
        }

        fun getFloat(context: Context, key: String, defaultValue: Float): Float {
            return getPrefs(context).getFloat(key, defaultValue)
        }

        //Longs
        fun save(context: Context, key: String, value: Long) {
            getPrefs(context).edit().putLong(key, value).commit()
        }

        fun getLong(context: Context, key: String): Long {
            return getPrefs(context).getLong(key, 0)
        }

        fun getLong(context: Context, key: String, defaultValue: Long): Long {
            return getPrefs(context).getLong(key, defaultValue)
        }

        //StringSets
        fun save(context: Context, key: String, value: Set<String>) {
            getPrefs(context).edit().putStringSet(key, value).commit()
        }

        fun getStringSet(context: Context, key: String): Set<String>? {
            return getPrefs(context).getStringSet(key, null)
        }

        fun getStringSet(context: Context, key: String, defaultValue: Set<String>): Set<String>? {
            return getPrefs(context).getStringSet(key, defaultValue)
        }

        fun removeKey(context: Context, key: String) {
            getPrefs(context).edit().remove(key).commit()
        }

    }
}