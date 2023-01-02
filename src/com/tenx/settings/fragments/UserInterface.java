/*
 * Copyright (C) 2020 TenX-OS
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
package com.tenx.settings.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class UserInterface extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "UserInterface";

    private static final String KEY_ICON_PACK = "android.theme.customization.icon_pack";
    private static final String KEY_ADAPTIVE_ICON_PACK = "android.theme.customization.adaptive_icon_shape";
    private static final String KEY_SYSTEM_FONT = "android.theme.customization.fonts";
    private static final String KEY_CLOCK_FONT = "android.theme.customization.lockscreen_clock_font";

    private Preference mIconPack;
    private Preference mAdaptiveIconPack;
    private Preference mSystemFont;
    private Preference mClockFont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_ui);

        mIconPack = (Preference) findPreference(KEY_ICON_PACK);
        mAdaptiveIconPack = (Preference) findPreference(KEY_ADAPTIVE_ICON_PACK);
        mSystemFont = (Preference) findPreference(KEY_SYSTEM_FONT);
        mClockFont = (Preference) findPreference(KEY_CLOCK_FONT);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void setLayoutToPreference() {
        mIconPack.setLayoutResource(R.layout.tenx_preference_middle);
        mAdaptiveIconPack.setLayoutResource(R.layout.tenx_preference_middle);
        mSystemFont.setLayoutResource(R.layout.tenx_preference_middle);
        mClockFont.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
