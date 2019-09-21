package com.muflihun.moviecatalogue5.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.muflihun.moviecatalogue5.R;
import com.muflihun.moviecatalogue5.broadcasts.AlarmReceiver;
import com.muflihun.moviecatalogue5.broadcasts.ReleaseReceiver;
import com.muflihun.moviecatalogue5.models.Item;
import com.muflihun.moviecatalogue5.viewmodels.ItemViewModel;

import java.util.ArrayList;
import java.util.Date;

import static com.muflihun.moviecatalogue5.broadcasts.ReleaseReceiver.DATE_FORMAT;

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

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        private final static String TAG = SettingsFragment.class.getSimpleName();
        public static final String DAILY_REMINDER_PREFERENCE_KEY = "dailyReminder";
        public static final String RELEASE_REMINDER_PREFERENCE_KEY = "releaseReminder";

        public static final String DAILY_REMINDER_TIME = "07:00";
        public static final String DAILY_REMINDER_MESSAGE = "BALIK LAGI DONG :)";
        public static final String RELEASE_REMINDER_TIME = "08:00";
        public static final String RELEASE_REMINDER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=%s&primary_release_date.gte=%s&primary_release_date.lte=%s";

        private AlarmReceiver alarmReceiver;
        private ReleaseReceiver releaseReceiver;
        private ItemViewModel itemViewModel;

        private ProgressDialog progressDialog;

        private Observer<ArrayList<Item>> observer = new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                if (items != null) {
                    releaseReceiver.setUpReleaseAlarm(getContext(), RELEASE_REMINDER_TIME, items);
                    Log.d(TAG, "List Count = "+items.size());
                    progressDialog.dismiss();
                }
            }
        };

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SwitchPreferenceCompat dailyReminderSwitch = findPreference(DAILY_REMINDER_PREFERENCE_KEY);
            SwitchPreferenceCompat releaseReminderSwitch = findPreference(RELEASE_REMINDER_PREFERENCE_KEY);

            dailyReminderSwitch.setOnPreferenceChangeListener(this);
            releaseReminderSwitch.setOnPreferenceChangeListener(this);

            releaseReceiver = new ReleaseReceiver();
            alarmReceiver = new AlarmReceiver();

            itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
            itemViewModel.getListItem().observe(this, observer);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch(preference.getKey()){
                case DAILY_REMINDER_PREFERENCE_KEY:
                    Log.d(TAG, preference.getKey()+" = "+newValue);
                    if (newValue.equals(true)){
                        alarmReceiver.setUpAlarm(getContext(), DAILY_REMINDER_TIME, DAILY_REMINDER_MESSAGE, AlarmReceiver.DAILY_REMINDER_TYPE);
                        Toast.makeText(getContext(), getResources().getString(R.string.daily_reminder_on), Toast.LENGTH_SHORT).show();
                    } else {
                        alarmReceiver.cancelAlarm(getContext());
                        Toast.makeText(getContext(), getResources().getString(R.string.daily_reminder_off), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RELEASE_REMINDER_PREFERENCE_KEY:
                    Log.d(TAG, preference.getKey()+" = "+newValue);
                    if (newValue.equals(true)){
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Switching On Release Reminder");
                        progressDialog.show();
                        Date d = new Date();
                        CharSequence date = android.text.format.DateFormat.format(DATE_FORMAT, d.getTime());
                        itemViewModel.setItem(String.format(RELEASE_REMINDER_URL, ItemViewModel.API_KEY, date, date), ItemViewModel.ITEM_MOVIE);
                        Log.d(TAG, String.format(RELEASE_REMINDER_URL, ItemViewModel.API_KEY, date, date));
                        Toast.makeText(getContext(), getResources().getString(R.string.release_reminder_on), Toast.LENGTH_SHORT).show();
                    } else {
                        releaseReceiver.cancelAlarm(getContext());
                        Toast.makeText(getContext(), getResources().getString(R.string.release_reminder_off), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}