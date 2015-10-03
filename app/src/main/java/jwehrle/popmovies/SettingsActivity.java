package jwehrle.popmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Package: jwehrle.popmovies
 * Class: SettingsActivity extends PreferenceActivity
 *          implements Preference.OnPreferenceChangeListener
 * Author: John Wehrle
 * Date: 9/29/15
 * Purpose: Create a basic settings activity through which the user can alter the movie discover
 * parameter sent to themoviedb.com. I anticipate that many fields will be added to this class in
 * the next stage of the assignment. Note, this class also adds a toolbar for returning to the
 * MainActivity. This settings Activity is only called from MainActivity.
 * Disclosure: This app is an implementation of a Udacity course assignment.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    /**
     * onCreate()
     * Adds and binds preferences from resources.
     * @param savedInstanceState The state of this Activity at Creation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
    }

    /**
     * onPostCreate()
     * Adds a titled toolbar with "back" function to this Activity.
     * @param savedInstanceState The state of this Activity after Creation.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root =
                (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar =
                (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * bindPreferenceSummaryToValue()
     * Saves preference changes.
     * @param preference the current preferences for this application
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * onPreferenceChange()
     * Saves new preferences.
     * @param preference the preferences for this application
     * @param value the new value for a preference
     * @return true upon successful change, false otherwise.
     */
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
            return true;
        }
        return false;
    }

}
