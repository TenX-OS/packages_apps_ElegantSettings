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
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingSwitchPreference;

public class Misc extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Misc";

    private static final String KEY_THREE_FINGERS_SCREENSHOT = "three_finger_gesture";
    private static final String KEY_POCKET_JUDGE = "pocket_judge";
    private static final String KEY_CHARGING_ANIMATION = "charging_animation";
    private static final String KEY_ROTATION_BUTTON = "enable_floating_rotation_button";

    private SystemSettingSwitchPreference mThreeFingersScreenshot;
    private SystemSettingSwitchPreference mPocketJudge;
    private SystemSettingSwitchPreference mChargingAnimation;
    private SystemSettingSwitchPreference mRotationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_misc);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        mThreeFingersScreenshot = (SystemSettingSwitchPreference) findPreference(KEY_THREE_FINGERS_SCREENSHOT);
        mPocketJudge = (SystemSettingSwitchPreference) findPreference(KEY_POCKET_JUDGE);
        mChargingAnimation = (SystemSettingSwitchPreference) findPreference(KEY_CHARGING_ANIMATION);
        mRotationButton = (SystemSettingSwitchPreference) findPreference(KEY_ROTATION_BUTTON);

        boolean mPocketJudgeSupported = res.getBoolean(
                com.android.internal.R.bool.config_pocketModeSupported);
        if (!mPocketJudgeSupported)
            prefScreen.removePreference(mPocketJudge);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void setLayoutToPreference() {
        mThreeFingersScreenshot.setLayoutResource(R.layout.tenx_preference_top);
        mPocketJudge.setLayoutResource(R.layout.tenx_preference_middle);
        mChargingAnimation.setLayoutResource(R.layout.tenx_preference_middle);
        mRotationButton.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
