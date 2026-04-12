package com.xdee.jpmart.util;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xdee.jpmart.Session;

/**
 * Chặn nhân viên mở màn hình chỉ dành cho quản trị (thống kê, quản lý nhân viên).
 */
public final class AdminUi {

    private AdminUi() {
    }

    public static boolean blockIfEmployee(AppCompatActivity activity) {
        if (Session.isEmployee(activity)) {
            Toast.makeText(activity, "Chức năng chỉ dành cho quản trị viên.", Toast.LENGTH_SHORT).show();
            activity.finish();
            return true;
        }
        return false;
    }
}
