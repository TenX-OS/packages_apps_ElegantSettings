/*
 * Copyright (C) 2020-2023 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenx.settings.fragments.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import android.provider.SearchIndexableResource;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;

import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;
import com.tenx.support.colorpicker.ColorPickerPreference;

import com.tenx.settings.controllers.SettingsDashboardGradientStart;
import com.tenx.settings.controllers.SettingsDashboardGradientEnd;

import java.util.ArrayList;
import java.util.List;

public class SettingsDashboard extends DashboardFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    public static final String TAG = "Dashboard";

    private static final String SETTINGS_DASHBOARD_BACKGROUND_SHOWN = "settings_dashboard_background_shown";
    private static final String SETTINGS_DASHBOARD_BACKGROUND_COLOR = "settings_dashboard_background_color";
    private static final String SETTINGS_DASHBOARD_BACKGROUND_STROKE_WIDTH = "settings_dashboard_background_stroke_width";
    private static final String SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_START = "settings_dashboard_background_gradient_start_color";
    private static final String SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_END = "settings_dashboard_background_gradient_end_color";

    private SystemSettingSwitchPreference mSettingsDashboardBackgroundShown;
    private SystemSettingSwitchPreference mSettingsDashboardBackgroundColor;
    private SystemSettingSeekBarPreference mSettingsDashboardBackgroundStrokeWidth;

    private ColorPickerPreference mSettingsDashboardBackgroundGradientStart;
    private ColorPickerPreference mSettingsDashboardBackgroundGradientEnd;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.tenx_settings_dashboard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsDashboardBackgroundShown = (SystemSettingSwitchPreference) findPreference(SETTINGS_DASHBOARD_BACKGROUND_SHOWN);
        mSettingsDashboardBackgroundColor = (SystemSettingSwitchPreference) findPreference(SETTINGS_DASHBOARD_BACKGROUND_COLOR);
        mSettingsDashboardBackgroundStrokeWidth = (SystemSettingSeekBarPreference) findPreference(SETTINGS_DASHBOARD_BACKGROUND_STROKE_WIDTH);

        mSettingsDashboardBackgroundGradientStart = (ColorPickerPreference) findPreference(SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_START);
        mSettingsDashboardBackgroundGradientEnd = (ColorPickerPreference) findPreference(SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_END);
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle, Fragment fragment) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        controllers.add(new SettingsDashboardGradientStart(context));
        controllers.add(new SettingsDashboardGradientEnd(context));
        return controllers;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Context context = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        final Context context = getActivity();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.tenx_settings_dashboard;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}

