package com.xdee.jpmart.model;

public class TopKhachStat {
    private final String tenKhach;
    private final double tongChiTieu;

    public TopKhachStat(String tenKhach, double tongChiTieu) {
        this.tenKhach = tenKhach;
        this.tongChiTieu = tongChiTieu;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public double getTongChiTieu() {
        return tongChiTieu;
    }
}
