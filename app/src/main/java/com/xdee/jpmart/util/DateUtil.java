package com.xdee.jpmart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DMY = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private DateUtil() {
    }

    /**
     * Chuẩn hóa chuỗi ngày về yyyy-MM-dd để so sánh trong SQLite. Hỗ trợ dd/MM/yyyy và yyyy-MM-dd.
     */
    public static String normalizeToIso(String raw) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        }
        try {
            if (s.matches("\\d{4}-\\d{2}-\\d{2}")) {
                synchronized (ISO) {
                    Date d = ISO.parse(s);
                    return ISO.format(d);
                }
            }
            synchronized (DMY) {
                Date d = DMY.parse(s);
                synchronized (ISO) {
                    return ISO.format(d);
                }
            }
        } catch (ParseException ignored) {
        }
        return null;
    }

    public static String formatMoneyVi(double value) {
        return String.format(Locale.getDefault(), "%,.0f đ", value);
    }
}
