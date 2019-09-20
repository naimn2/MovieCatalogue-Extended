package com.muflihun.moviecatalogue5.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.muflihun.moviecatalogue5.R;

public class SetReminderActivity extends AppCompatActivity {
    private static final String DAILY_TEXT_ON = "daily_on";
    private static final String DAILY_TEXT_OFF = "daily_off";
    private static final String PREFERENCE_DAILY_REMINDER = "daily_reminder";
    public static final String PREFERENCES_REMINDER = "reminder";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sp = getSharedPreferences(PREFERENCES_REMINDER,0);
        spe = sp.edit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        final static String TAG = SettingsFragment.class.getSimpleName();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SwitchPreferenceCompat dailyReminderSwitch = findPreference("dailyReminder");
            SwitchPreferenceCompat releaseReminderSwitch = findPreference("releaseReminder");

            dailyReminderSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Enable daily reminder
                    Log.d(TAG, preference.getKey()+" = "+newValue);
                    return true;
                }
            });

            releaseReminderSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // Enable release reminder
                    return true;
                }
            });
        }
    }
}