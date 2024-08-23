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
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;
import android.text.TextUtils;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.tenx.OmniJawsClient;
import com.android.internal.util.tenx.Utils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.settings.fragments.lockscreen.UdfpsAnimation;
import com.tenx.settings.fragments.lockscreen.UdfpsIconPicker;

import com.tenx.support.preferences.SecureSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.preferences.TenXPreference;

public class LockScreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_WEATHER_ENABLED = "lockscreen_weather_enabled";
    private static final String KEY_UDFPS_ANIMATIONS = "udfps_recognizing_animation_preview";
    private static final String KEY_UDFPS_ICONS = "udfps_icon_picker";
    private static final String SCREEN_OFF_UDFPS_ENABLED = "screen_off_udfps_enabled";
    private static final String KEY_FINGERPRINT_CATEGORY = "fingerprint";

    private SystemSettingSwitchPreference mWeatherEnabled;
    private TenXPreference mUdfpsAnimations;
    private TenXPreference mUdfpsIcons;
    private SecureSettingSwitchPreference mScreenOffUdfps;

    private OmniJawsClient mWeatherClient;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_lockscreen);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        PreferenceCategory fingerprintCategory = (PreferenceCategory) findPreference(KEY_FINGERPRINT_CATEGORY);

        FingerprintManager mFingerprintManager = (FingerprintManager)
                getActivity().getSystemService(Context.FINGERPRINT_SERVICE);

        mWeatherEnabled = (SystemSettingSwitchPreference) findPreference(KEY_WEATHER_ENABLED);
        mUdfpsAnimations = (TenXPreference) findPreference(KEY_UDFPS_ANIMATIONS);
        mUdfpsIcons = (TenXPreference) findPreference(KEY_UDFPS_ICONS);
        mScreenOffUdfps = (SecureSettingSwitchPreference) findPreference(SCREEN_OFF_UDFPS_ENABLED);

        if (mFingerprintManager == null || !mFingerprintManager.isHardwareDetected()) {
            fingerprintCategory.removePreference(mUdfpsAnimations);
            fingerprintCategory.removePreference(mUdfpsIcons);
            fingerprintCategory.removePreference(mScreenOffUdfps);
        } else {
            if (!Utils.isPackageInstalled(getContext(), "com.tenx.udfps.animations")) {
                fingerprintCategory.removePreference(mUdfpsAnimations);
            }
            if (!Utils.isPackageInstalled(getContext(), "com.tenx.udfps.icons")) {
                fingerprintCategory.removePreference(mUdfpsIcons);
            }
            boolean screenOffUdfpsAvailable = resources.getBoolean(
                    com.android.internal.R.bool.config_supportScreenOffUdfps) ||
                    !TextUtils.isEmpty(resources.getString(
                        com.android.internal.R.string.config_dozeUdfpsLongPressSensorType));
            if (!screenOffUdfpsAvailable)
                fingerprintCategory.removePreference(mScreenOffUdfps);
        }

        mWeatherClient = new OmniJawsClient(getContext());
        updateWeatherSettings();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void updateWeatherSettings() {
        if (mWeatherClient == null || mWeatherEnabled == null) return;

        boolean weatherEnabled = mWeatherClient.isOmniJawsEnabled();
        mWeatherEnabled.setEnabled(weatherEnabled);
        mWeatherEnabled.setSummary(weatherEnabled ? R.string.lockscreen_weather_summary :
            R.string.lockscreen_weather_enabled_info);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWeatherSettings();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
