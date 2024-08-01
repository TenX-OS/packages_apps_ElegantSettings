/*
 * Copyright (C) 2019-2024 TenX-OS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.tenx.settings;

import android.content.Context;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class TopLevelTenXSettingsPreferenceController extends BasePreferenceController {

    public TopLevelTenXSettingsPreferenceController(Context context,
            String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }
}
