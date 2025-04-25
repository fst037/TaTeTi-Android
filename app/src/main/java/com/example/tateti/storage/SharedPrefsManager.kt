package com.example.tateti.storage
import android.content.Context
import android.content.SharedPreferences

object SharedPrefsManager {
    private const val PREF_NAME = "tictactron_prefs"
    private const val WINS_KEY_PREFIX = "wins_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveWin(context: Context, playerName: String) {
        val prefs = getPrefs(context)
        val key = "$WINS_KEY_PREFIX$playerName"
        val currentWins = prefs.getInt(key, 0)
        prefs.edit().putInt(key, currentWins + 1).apply()
    }

    fun getAllWins(context: Context): Map<String, Int> {
        val prefs = getPrefs(context)
        return prefs.all
            .filterKeys { it.startsWith(WINS_KEY_PREFIX) }
            .mapKeys { it.key.removePrefix(WINS_KEY_PREFIX) }
            .mapValues { it.value as Int }
            .toList()
            .sortedByDescending { (_, value) -> value }
            .toMap()
    }
}