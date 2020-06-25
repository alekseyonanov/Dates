package com.nollpointer.dates.activity

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.ListPreference
import android.preference.SwitchPreference
import com.nollpointer.dates.R

class SettingsActivity : AppCompatPreferenceActivity(), OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        setupActionBar()
        val sharedPreferences = preferenceScreen.sharedPreferences
        val texts = resources.getTextArray(R.array.dates_cards_types_entries)
        val languageTexts = resources.getTextArray(R.array.languages_entries)
        val saveCurrentState = sharedPreferences.getBoolean("save_current_state", true)
        val listPreference = findPreference("dates_card_type") as ListPreference
        val switchPreference = findPreference("save_current_state") as SwitchPreference
        listPreference.summary = texts[sharedPreferences.getString("dates_card_type", "0")!!.toInt()]
        switchPreference.isChecked = saveCurrentState
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)
        if (preference is ListPreference) {
            preference.setSummary(preference.entry)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }
}