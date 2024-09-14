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
package com.tenx.settings.fragments.dashboard.adapter;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.R;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {

    private final Context mContext;
    private final int[] mBackgroundImages = {
            R.drawable.tenx_settings_dashboard_background_image,
            R.drawable.tenx_settings_dashboard_background_image_1,
            R.drawable.tenx_settings_dashboard_background_image_2,
            R.drawable.tenx_settings_dashboard_background_image_3,
            R.drawable.tenx_settings_dashboard_background_image_4,
            R.drawable.tenx_settings_dashboard_background_image_5,
            R.drawable.tenx_settings_dashboard_background_image_6,
            R.drawable.tenx_settings_dashboard_background_image_7,
            R.drawable.tenx_settings_dashboard_background_image_8,
            R.drawable.tenx_settings_dashboard_background_image_9,
            R.drawable.tenx_settings_dashboard_background_image_10,
            R.drawable.tenx_settings_dashboard_background_image_11,
            R.drawable.tenx_settings_dashboard_background_image_12,
            R.drawable.tenx_settings_dashboard_background_image_13,
            R.drawable.tenx_settings_dashboard_background_image_14,
            R.drawable.tenx_settings_dashboard_background_image_15,
    };
    private int mSelectedBackground;

    public BackgroundAdapter(Context context) {
        mContext = context;
        mSelectedBackground = Settings.System.getInt(
                mContext.getContentResolver(),
                Settings.System.SETTINGS_TENX_DASHBOARD_BACKGROUND, 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tenx_settings_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.backgroundButton.setBackgroundResource(mBackgroundImages[position]);

        if (position == mSelectedBackground) {
            holder.backgroundButton.setImageResource(R.drawable.ic_checked_background);
        } else {
            holder.backgroundButton.setImageResource(0);
        }

        holder.backgroundButton.setOnClickListener(v -> {
            mSelectedBackground = position;
            notifyDataSetChanged();
            updateSettings(position);
        });
    }

    @Override
    public int getItemCount() {
        return mBackgroundImages.length;
    }

    private void updateSettings(int value) {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SETTINGS_TENX_DASHBOARD_BACKGROUND, value);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton backgroundButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundButton = itemView.findViewById(R.id.background_button);
        }
    }
}
