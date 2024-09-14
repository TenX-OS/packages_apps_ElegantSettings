/*
 * Copyright (C) 2024 TenX-OS
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

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.R;
import com.android.settingslib.widget.LayoutPreference;

import com.tenx.settings.fragments.dashboard.adapter.BackgroundAdapter;

public class SettingsHeaderBackgroundPicker extends LayoutPreference {

    private RecyclerView mRecyclerView;

    public SettingsHeaderBackgroundPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setLayoutResource(R.layout.tenx_settings_header_picker);

        mRecyclerView = findViewById(R.id.background_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BackgroundAdapter adapter = new BackgroundAdapter(context);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
    }
}
