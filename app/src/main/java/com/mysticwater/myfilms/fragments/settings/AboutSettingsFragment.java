package com.mysticwater.myfilms.fragments.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mysticwater.myfilms.BuildConfig;
import com.mysticwater.myfilms.R;
import com.mysticwater.myfilms.SettingsActivity;

import java.util.Calendar;

public class AboutSettingsFragment extends PreferenceFragment {

    private static final String LOG_TAG = "AboutSettingsFragment";

    private String mAppName;
    private String mAppVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_about);

        Preference appDetailsPref = findPreference(getString(R.string.pref_app_version));
        mAppName = getString(R.string.app_name);
        mAppVersion = BuildConfig.VERSION_NAME;
        appDetailsPref.setTitle(getString(R.string.label_about_app_version, mAppName, mAppVersion));

        Preference legalPref = findPreference(getString(R.string.pref_legal));
        legalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showLegalDialog();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        SettingsActivity.sToolbar.setTitle(getString(R.string.title_about));
    }

    private void showLegalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layoutView = inflater.inflate(R.layout.dialog_scroll_text, null);
        TextView dialogText = (TextView) layoutView.findViewById(R.id.dialog_text);

        String legalText = buildLegalText();
        dialogText.setText(legalText);

        builder.setView(layoutView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String buildLegalText() {
        return getString(R.string.app_name) + "\n\n" +
                generateCopyrightString() + "\n\n" +
                getString(R.string.app_name) + " is built using open source software: " + "\n\n" +
                "\u2022 Retrofit\n" +
                "\u2022 google-gson\n" +
                "\u2022 Bullet\n" +
                "\u2022 Bullet\n" +
                "\u2022 Bullet\n";
    }

    private String generateCopyrightString() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        return "Copyright \u00a9 " + year + " Mystic Water. All rights reserved.";
    }

}
