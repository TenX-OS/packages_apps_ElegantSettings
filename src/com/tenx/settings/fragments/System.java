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
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.settings.utils.TelephonyUtils;

import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;
import com.tenx.support.preferences.TenXPreferenceCategory;

import com.android.internal.util.tenx.Utils;

public class System extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "System";

    private static final String KEY_VIBRATE_CATEGORY = "vibration";
    private static final String KEY_VIBRATE_CONNECT = "vibrate_on_connect";
    private static final String KEY_VIBRATE_CALLWAITING = "vibrate_on_callwaiting";
    private static final String KEY_VIBRATE_DISCONNECT = "vibrate_on_disconnect";
    private static final String FLASHLIGHT_CATEGORY = "flashlight_category";
    private static final String FLASHLIGHT_CALL_PREF = "flashlight_on_call";
    private static final String FLASHLIGHT_DND_PREF = "flashlight_on_call_ignore_dnd";
    private static final String FLASHLIGHT_RATE_PREF = "flashlight_on_call_rate";

    private SystemSettingListPreference mFlashOnCall;
    private SystemSettingSwitchPreference mFlashOnCallIgnoreDND;
    private SystemSettingSeekBarPreference mFlashOnCallRate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_system);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Context mContext = getActivity().getApplicationContext();
        final ContentResolver resolver = mContext.getContentResolver();

        final PreferenceCategory vibCategory = prefScreen.findPreference(KEY_VIBRATE_CATEGORY);

        if (!TelephonyUtils.isVoiceCapable(getActivity())) {
            prefScreen.removePreference(vibCategory);
        }

        if (!Utils.deviceHasFlashlight(mContext)) {
            final PreferenceCategory flashlightCategory =
                    (PreferenceCategory) prefScreen.findPreference(FLASHLIGHT_CATEGORY);
            prefScreen.removePreference(flashlightCategory);
        } else {
            mFlashOnCall = (SystemSettingListPreference)
                    prefScreen.findPreference(FLASHLIGHT_CALL_PREF);
            mFlashOnCall.setOnPreferenceChangeListener(this);

            mFlashOnCallIgnoreDND = (SystemSettingSwitchPreference)
                    prefScreen.findPreference(FLASHLIGHT_DND_PREF);
            int value = Settings.System.getInt(resolver,
                    Settings.System.FLASHLIGHT_ON_CALL, 0);

            mFlashOnCallRate = (SystemSettingSeekBarPreference)
                    prefScreen.findPreference(FLASHLIGHT_RATE_PREF);

            mFlashOnCallIgnoreDND.setEnabled(value > 1);
            mFlashOnCallRate.setEnabled(value > 0);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFlashOnCall) {
            int value = Integer.parseInt((String) newValue);
            mFlashOnCallIgnoreDND.setEnabled(value > 1);
            mFlashOnCallRate.setEnabled(value > 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
