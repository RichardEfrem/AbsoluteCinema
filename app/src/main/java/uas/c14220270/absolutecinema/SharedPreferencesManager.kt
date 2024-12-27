package uas.c14220270.absolutecinema
import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "UserPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the SharedPreferences instance
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save a string value
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Retrieve a string value
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}