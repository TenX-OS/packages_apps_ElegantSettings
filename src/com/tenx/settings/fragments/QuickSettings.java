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
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.tenx.ThemeUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import lineageos.preference.LineageSecureSettingListPreference;
import lineageos.preference.LineageSecureSettingSwitchPreference;
import com.tenx.support.preferences.SecureSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_BATTERY_STYLE = "qs_battery_style";
    private static final String KEY_BATTERY_PERCENT = "qs_show_battery_percent";
    private static final String KEY_QS_UI_STYLE  = "qs_tile_ui_style";
    private static final String KEY_QS_SPLIT_SHADE = "qs_split_shade";
    private static final String QS_SPLIT_SHADE_LAYOUT_CTG = "android.theme.customization.qs_landscape_layout";
    private static final String QS_SPLIT_SHADE_LAYOUT_PKG = "com.android.systemui.qs.landscape.split_shade_layout";
    private static final String QS_SPLIT_SHADE_LAYOUT_TARGET = "com.android.systemui";

    private static final int BATTERY_STYLE_PORTRAIT = 0;
    private static final int BATTERY_STYLE_TEXT = 4;
    private static final int BATTERY_STYLE_HIDDEN = 5;

    private SystemSettingListPreference mBatteryStyle;
    private SystemSettingListPreference mBatteryPercent;
    private SystemSettingListPreference mQsUI;
    private SystemSettingSwitchPreference mSplitShade;

    private static ThemeUtils mThemeUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_quicksettings);

        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        mThemeUtils = new ThemeUtils(getActivity());

        mBatteryStyle = (SystemSettingListPreference) findPreference(KEY_BATTERY_STYLE);
        mBatteryPercent = (SystemSettingListPreference) findPreference(KEY_BATTERY_PERCENT);

        int batterystyle = Settings.System.getIntForUser(resolver,
                Settings.System.QS_BATTERY_STYLE, BATTERY_STYLE_PORTRAIT, UserHandle.USER_CURRENT);

        mBatteryStyle.setOnPreferenceChangeListener(this);

        mBatteryPercent.setEnabled(
                batterystyle != BATTERY_STYLE_TEXT && batterystyle != BATTERY_STYLE_HIDDEN);

        String isA11Style = Integer.toString(Settings.System.getIntForUser(resolver,
                Settings.System.QS_TILE_UI_STYLE , 0, UserHandle.USER_CURRENT));

        mSplitShade = findPreference(KEY_QS_SPLIT_SHADE);
        boolean ssEnabled = isSplitShadeEnabled();
        mSplitShade.setChecked(ssEnabled);
        mSplitShade.setOnPreferenceChangeListener(this);

        mQsUI = (SystemSettingListPreference) findPreference(KEY_QS_UI_STYLE);
        int index = mQsUI.findIndexOfValue(isA11Style);
        mQsUI.setValue(isA11Style);
        mQsUI.setSummary(mQsUI.getEntries()[index]);
        mQsUI.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            mBatteryPercent.setEnabled(
                    value != BATTERY_STYLE_TEXT && value != BATTERY_STYLE_HIDDEN);
            return true;
        } else if (preference == mQsUI) {
            int value = Integer.parseInt((String) newValue);
            int index = mQsUI.findIndexOfValue((String) newValue);
            mQsUI.setValue((String) newValue);
            mQsUI.setSummary(mQsUI.getEntries()[index]);
            Settings.System.putIntForUser(resolver,
                    Settings.System.QS_TILE_UI_STYLE, value, UserHandle.USER_CURRENT);
            updateQsStyle(getActivity());
            return true;
        } else if (preference == mSplitShade) {
            updateSplitShadeState(((Boolean) newValue).booleanValue());
            return true;
        }
        return false;
    }

    private boolean isSplitShadeEnabled() {
        return mThemeUtils.isOverlayEnabled(QS_SPLIT_SHADE_LAYOUT_PKG);
    }
    private void updateSplitShadeState(boolean enable) {
        mThemeUtils.setOverlayEnabled(
                QS_SPLIT_SHADE_LAYOUT_CTG,
                enable ? QS_SPLIT_SHADE_LAYOUT_PKG : QS_SPLIT_SHADE_LAYOUT_TARGET,
                QS_SPLIT_SHADE_LAYOUT_TARGET);
    }

    private static void updateQsStyle(Context context) {
        ContentResolver resolver = context.getContentResolver();

        boolean isA11Style = Settings.System.getIntForUser(resolver,
                Settings.System.QS_TILE_UI_STYLE , 0, UserHandle.USER_CURRENT) != 0;

	String qsUIStyleCategory = "android.theme.customization.qs_ui";
        String overlayThemeTarget  = "com.android.systemui";
        String overlayThemePackage  = "com.android.system.qs.ui.A11";

        if (mThemeUtils == null) {
            mThemeUtils = new ThemeUtils(context);
        }

        // reset all overlays before applying
        mThemeUtils.setOverlayEnabled(qsUIStyleCategory, overlayThemeTarget, overlayThemeTarget);

        if (isA11Style) {
            mThemeUtils.setOverlayEnabled(qsUIStyleCategory, overlayThemePackage, overlayThemeTarget);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
