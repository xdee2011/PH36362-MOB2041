package com.xdee.jpmart.model;

public class TopSanPhamStat {
    private int sanPhamId;
    private String tenSanPham;
    private int tongSoLuong;

    public TopSanPhamStat(int sanPhamId, String tenSanPham, int tongSoLuong) {
        this.sanPhamId = sanPhamId;
        this.tenSanPham = tenSanPham;
        this.tongSoLuong = tongSoLuong;
    }

    public int getSanPhamId() {
        return sanPhamId;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public int getTongSoLuong() {
        return tongSoLuong;
    }
}
