package com.xdee.jpmart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public final class ToolbarHelper {

    private ToolbarHelper() {}

    public static void setupBack(AppCompatActivity activity, @Nullable String title) {
        MaterialToolbar toolbar = activity.findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            if (title != null) {
                ab.setTitle(title);
            }
        }
        toolbar.setNavigationOnClickListener(v -> activity.getOnBackPressedDispatcher().onBackPressed());
    }
}
