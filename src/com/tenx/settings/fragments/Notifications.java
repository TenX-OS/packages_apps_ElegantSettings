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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingMasterSwitchPreference;
import com.tenx.support.preferences.GlobalSettingMasterSwitchPreference;

public class Notifications extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Notifications";
    private static final String HEADS_UP_NOTIFICATIONS_ENABLED = "heads_up_notifications_enabled";

    private GlobalSettingMasterSwitchPreference mHeadsUpEnabled;

    private static final String AMBIENT_NOTIFICATION_LIGHT = "pulse_ambient_light";

    private SystemSettingMasterSwitchPreference mEdgeLightEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_notifications);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();
        final ContentResolver resolver = getActivity().getContentResolver();

        mEdgeLightEnabled = (SystemSettingMasterSwitchPreference) findPreference(AMBIENT_NOTIFICATION_LIGHT);
        mEdgeLightEnabled.setOnPreferenceChangeListener(this);
        int edgeLightEnabled = Settings.System.getInt(getContentResolver(),
                AMBIENT_NOTIFICATION_LIGHT, 0);
        mEdgeLightEnabled.setChecked(edgeLightEnabled != 0);

        mHeadsUpEnabled = (GlobalSettingMasterSwitchPreference) findPreference(HEADS_UP_NOTIFICATIONS_ENABLED);
        mHeadsUpEnabled.setOnPreferenceChangeListener(this);
        int headsUpEnabled = Settings.Global.getInt(getContentResolver(),
                HEADS_UP_NOTIFICATIONS_ENABLED, 1);
        mHeadsUpEnabled.setChecked(headsUpEnabled != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mEdgeLightEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
                    AMBIENT_NOTIFICATION_LIGHT, value ? 1 : 0);
            return true;
        } else if (preference == mHeadsUpEnabled) {
            boolean value = (Boolean) newValue;
            Settings.Global.putInt(getContentResolver(),
		            HEADS_UP_NOTIFICATIONS_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
