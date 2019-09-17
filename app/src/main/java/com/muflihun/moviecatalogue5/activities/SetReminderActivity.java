package com.muflihun.moviecatalogue5.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

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
        SwitchPreferenceCompat dailyReminderSwitch;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            dailyReminderSwitch = findPreference("dailyReminder");
//            dailyReminderSwitch.setSwitchTextOn(DAILY_TEXT_ON);
//            dailyReminderSwitch.setSwitchTextOff(DAILY_TEXT_OFF);

            if (sp.getBoolean(PREFERENCE_DAILY_REMINDER, false)){
                dailyReminderSwitch.setSwitchTextOn(DAILY_TEXT_ON);
            } else {
                dailyReminderSwitch.setSwitchTextOff(DAILY_TEXT_OFF);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            switch (preference.getKey()){
                case "dailyRemider":
//                    if (dailyReminderSwitch.getS)
                    break;
                case "releaseReminder":
                    break;
                default:
            }
            return super.onPreferenceTreeClick(preference);

        }
    }
}