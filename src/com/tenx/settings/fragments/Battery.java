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
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import lineageos.preference.LineagePartsPreference;

import com.tenx.support.preferences.TenXPreference;

public class Battery extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Battery";

    private static final String KEY_BATTERY_SAVER_CATEGORY = "battery_saver";
    private static final String KEY_BATTERY_LIGHT_PREFERENCE_CATRGORY = "battery_light";
    private static final String KEY_BATTERY_LIGHT = "battery_lights";
    private static final String KEY_SENSOR_BLOCK = "sensor_block_settings";
    private static final String KEY_SMART_PIXELS = "smart_pixels";

    private LineagePartsPreference mBatteryLight;
    private Preference mSensorBlock;
    private TenXPreference mSmartPixels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_battery);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Context mContext = getActivity().getApplicationContext();
        final Resources res = mContext.getResources();

        mBatteryLight = (LineagePartsPreference) findPreference(KEY_BATTERY_LIGHT);
        mSensorBlock = (Preference) findPreference(KEY_SENSOR_BLOCK);
        mSmartPixels = (TenXPreference) findPreference(KEY_SMART_PIXELS);

        boolean mBatLightsSupported = res.getInteger(
                org.lineageos.platform.internal.R.integer.config_deviceLightCapabilities) >= 64;

        if (!mBatLightsSupported) {
            final PreferenceCategory lightsCategory =
                    (PreferenceCategory) prefScreen.findPreference(KEY_BATTERY_LIGHT_PREFERENCE_CATRGORY);
            prefScreen.removePreference(lightsCategory);
        }

        mSmartPixels = (TenXPreference) prefScreen.findPreference(KEY_SMART_PIXELS);
        boolean mSmartPixelsSupported = res.getBoolean(
                com.android.internal.R.bool.config_supportSmartPixels);

        PreferenceCategory batterySaverCategory = (PreferenceCategory) findPreference(KEY_BATTERY_SAVER_CATEGORY);

        if (!mSmartPixelsSupported)
            batterySaverCategory.removePreference(mSmartPixels);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void setLayoutToPreference() {
        boolean mBatLightsSupported = getActivity().getApplicationContext().getResources().getInteger(
                org.lineageos.platform.internal.R.integer.config_deviceLightCapabilities) >= 64;

        if (mBatLightsSupported) {
            mBatteryLight.setLayoutResource(R.layout.tenx_preference);
        }

        mSensorBlock.setLayoutResource(R.layout.tenx_preference_top);
        mSmartPixels.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
