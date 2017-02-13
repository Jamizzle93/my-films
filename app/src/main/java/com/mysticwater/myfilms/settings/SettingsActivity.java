package com.mysticwater.myfilms.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mysticwater.myfilms.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    static LinearLayout sRoot;
    public static Toolbar sToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRoot = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        sToolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, sRoot, false);
        sToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        sRoot.addView(sToolbar, 0);
        sToolbar.setTitle(getTitle());
        sToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {

        ArrayList<String> validFragments = new ArrayList<>();

        validFragments.add(AboutSettingsFragment.class.getName());
//        validFragments.add(SyncSettingsFragment.class.getName());
//        validFragments.add(HelpSettingsFragment.class.getName());
//        validFragments.add(AppearenceSettingsFragment.class.getName());
//        validFragments.add(DebugSettingsFragment.class.getName());

        return validFragments.contains(fragmentName);
    }

}
