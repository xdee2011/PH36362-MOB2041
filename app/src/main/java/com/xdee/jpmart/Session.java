package com.xdee.jpmart;

import android.content.Context;
import android.content.SharedPreferences;

import com.xdee.jpmart.model.NguoiDung;

public final class Session {

    private static final String PREFS = "jpmart_session";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FULLNAME = "fullname";

    private Session() {
    }

    /**
     * Lưu phiên sau đăng nhập (username chữ thường, kèm vai trò).
     */
    public static void setLoggedInUser(Context context, String username, String role) {
        String r = role != null ? role : NguoiDung.ROLE_ADMIN;
        prefs(context).edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, r)
                .putString(KEY_FULLNAME, "")
                .apply();
    }

    public static void setLoggedInUser(Context context, NguoiDung nd) {
        if (nd == null) {
            return;
        }
        String r = nd.getRole() != null ? nd.getRole() : NguoiDung.ROLE_ADMIN;
        String fn = nd.getFullname() != null ? nd.getFullname() : "";
        prefs(context).edit()
                .putString(KEY_USERNAME, nd.getUsername())
                .putString(KEY_ROLE, r)
                .putString(KEY_FULLNAME, fn)
                .apply();
    }

    public static String getUsername(Context context) {
        return prefs(context).getString(KEY_USERNAME, null);
    }

    /** Họ tên người đăng nhập (hiển thị trên hóa đơn). */
    public static String getFullname(Context context) {
        return prefs(context).getString(KEY_FULLNAME, "");
    }

    public static String getRole(Context context) {
        String r = prefs(context).getString(KEY_ROLE, null);
        return r != null ? r : NguoiDung.ROLE_ADMIN;
    }

    public static boolean isEmployee(Context context) {
        return NguoiDung.ROLE_EMPLOYEE.equals(getRole(context));
    }

    public static boolean isAdmin(Context context) {
        return !isEmployee(context);
    }

    public static boolean isLoggedIn(Context context) {
        String u = getUsername(context);
        return u != null && !u.isEmpty();
    }

    public static void clear(Context context) {
        prefs(context).edit().clear().apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
