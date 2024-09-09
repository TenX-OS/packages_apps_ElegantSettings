/*
 * Copyright (C) 2024 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tenx.settings.fragments.notification;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.colorpicker.ColorPickerSecurePreference;
import com.tenx.support.preferences.SecureSettingListPreference;

public class AmbientEdgeLights extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PULSE_AMBIENT_LIGHT_COLOR_MODE = "pulse_ambient_light_color_mode";
    private static final String PULSE_AMBIENT_LIGHT_COLOR = "pulse_ambient_light_color";

    private SecureSettingListPreference mColorMode;
    private ColorPickerSecurePreference mColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ambient_edge_light_settings);

        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        mColorMode = (SecureSettingListPreference) findPreference(PULSE_AMBIENT_LIGHT_COLOR_MODE);
        int colorMode = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.PULSE_AMBIENT_LIGHT_COLOR_MODE, 0, UserHandle.USER_CURRENT);
        mColorMode.setValue(String.valueOf(colorMode));
        mColorMode.setSummary(mColorMode.getEntry());
        mColorMode.setOnPreferenceChangeListener(this);

        mColor = (ColorPickerSecurePreference) findPreference(PULSE_AMBIENT_LIGHT_COLOR);
        int color = Settings.Secure.getInt(resolver,
                Settings.Secure.PULSE_AMBIENT_LIGHT_COLOR, 0xFF6ACEFF);
        mColor.setNewPreviewColor(color);
        String colorHex = String.format("#%08x", (0xFF6ACEFF & color));
        if (colorHex.equals("#ffffffff")) {
            mColor.setSummary(R.string.default_string);
        } else {
            mColor.setSummary(colorHex);
        }
        mColor.setOnPreferenceChangeListener(this);

        updateColorPrefs(colorMode);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        if (preference == mColorMode) {
            int colorMode = Integer.valueOf((String) newValue);
            int index = mColorMode.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(resolver,
                    Settings.Secure.PULSE_AMBIENT_LIGHT_COLOR_MODE, colorMode, UserHandle.USER_CURRENT);
            mColorMode.setSummary(mColorMode.getEntries()[index]);
            updateColorPrefs(colorMode);
            return true;
        } else if (preference == mColor) {
            String hex = ColorPickerSecurePreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ffffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerSecurePreference.convertToColorInt(hex);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.PULSE_AMBIENT_LIGHT_COLOR, intHex);
            return true;
        }
        return false;
    }

    private void updateColorPrefs(int colorMode) {
        if (mColorMode != null) {
            mColor.setEnabled(colorMode == 2);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
