package com.xdee.jpmart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xdee.jpmart.model.CartItem;
import com.xdee.jpmart.model.ChiTietHoaDon;
import com.xdee.jpmart.model.DanhMuc;
import com.xdee.jpmart.model.HoaDon;
import com.xdee.jpmart.model.KhachHang;
import com.xdee.jpmart.model.NguoiDung;
import com.xdee.jpmart.model.NhanVien;
import com.xdee.jpmart.model.SanPham;
import com.xdee.jpmart.model.TopKhachStat;
import com.xdee.jpmart.model.TopSanPhamStat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JPMart.db";
    private static final int DATABASE_VERSION = 2;

    /** Mật khẩu mặc định khi tạo tài khoản đăng nhập cho nhân viên mới */
    public static final String DEFAULT_EMPLOYEE_PASSWORD = "123";

    /** Tên khách mặc định khi thanh toán từ giỏ hàng */
    public static final String DEFAULT_CHECKOUT_CUSTOMER_NAME = "Nguyễn Văn An";

    private static final String TABLE_NGUOI_DUNG = "nguoi_dung";
    private static final String TABLE_DANH_MUC = "danh_muc";
    private static final String TABLE_SAN_PHAM = "san_pham";
    private static final String TABLE_KHACH_HANG = "khach_hang";
    private static final String TABLE_NHAN_VIEN = "nhan_vien";
    private static final String TABLE_HOA_DON = "hoa_don";
    private static final String TABLE_CHI_TIET = "chi_tiet_hoa_don";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NGUOI_DUNG + "("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "fullname TEXT NOT NULL,"
                + "role TEXT NOT NULL DEFAULT '" + com.xdee.jpmart.model.NguoiDung.ROLE_ADMIN + "'"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_DANH_MUC + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ma TEXT NOT NULL UNIQUE,"
                + "ten TEXT NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_SAN_PHAM + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "category_id INTEGER NOT NULL,"
                + "import_date TEXT"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_KHACH_HANG + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ma TEXT NOT NULL UNIQUE,"
                + "ho_ten TEXT NOT NULL,"
                + "sdt TEXT,"
                + "email TEXT,"
                + "dia_chi TEXT"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_NHAN_VIEN + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ma TEXT NOT NULL UNIQUE,"
                + "ho_ten TEXT NOT NULL,"
                + "dia_chi TEXT,"
                + "chuc_vu TEXT,"
                + "luong REAL NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_HOA_DON + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "ma_hoa_don TEXT NOT NULL UNIQUE,"
                + "ten_nhan_vien TEXT,"
                + "ten_khach_hang TEXT,"
                + "ngay_lap TEXT NOT NULL,"
                + "tong_tien REAL NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE " + TABLE_CHI_TIET + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "hoa_don_id INTEGER NOT NULL,"
                + "san_pham_id INTEGER NOT NULL,"
                + "ten_sp TEXT NOT NULL,"
                + "so_luong INTEGER NOT NULL,"
                + "don_gia REAL NOT NULL"
                + ")");

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        ContentValues u = new ContentValues();
        u.put("username", "admin");
        u.put("password", "admin");
        u.put("fullname", "Quản trị viên");
        u.put("role", com.xdee.jpmart.model.NguoiDung.ROLE_ADMIN);
        db.insert(TABLE_NGUOI_DUNG, null, u);

        ContentValues dm1 = new ContentValues();
        dm1.put("ma", "DM1");
        dm1.put("ten", "Thực phẩm");
        long cat1 = db.insert(TABLE_DANH_MUC, null, dm1);

        ContentValues dm2 = new ContentValues();
        dm2.put("ma", "DM2");
        dm2.put("ten", "Đồ uống");
        db.insert(TABLE_DANH_MUC, null, dm2);

        int cid = (int) cat1;
        if (cid <= 0) {
            cid = 1;
        }

        String[] names = {
                "Bánh quy bơ LU Pháp",
                "Snack mực lăn muối ớt",
                "Snack khoai tây Lays",
                "Bánh gạo One One",
                "Kẹo sữa sô cô la"
        };
        double[] prices = {45000, 8000, 12000, 30000, 25000};
        int[] quantities = {10, 52, 38, 11, 30};
        for (int i = 0; i < names.length; i++) {
            ContentValues p = new ContentValues();
            p.put("name", names[i]);
            p.put("price", prices[i]);
            p.put("quantity", quantities[i]);
            p.put("category_id", cid);
            p.put("import_date", "2025-01-15");
            db.insert(TABLE_SAN_PHAM, null, p);
        }

        ContentValues kh1 = new ContentValues();
        kh1.put("ma", "KH01");
        kh1.put("ho_ten", "Nguyễn Văn An");
        kh1.put("sdt", "0901000001");
        kh1.put("email", "an@email.com");
        kh1.put("dia_chi", "Hà Nội");
        db.insert(TABLE_KHACH_HANG, null, kh1);

        ContentValues kh2 = new ContentValues();
        kh2.put("ma", "KH02");
        kh2.put("ho_ten", "Trần Thị Bình");
        kh2.put("sdt", "0901000002");
        kh2.put("email", "binh@email.com");
        kh2.put("dia_chi", "TP.HCM");
        db.insert(TABLE_KHACH_HANG, null, kh2);

        ContentValues nv = new ContentValues();
        nv.put("ma", "NV01");
        nv.put("ho_ten", "Nguyễn Xuân Đạt");
        nv.put("dia_chi", "Hà Nội");
        nv.put("chuc_vu", "Thu ngân");
        nv.put("luong", 12000000);
        db.insert(TABLE_NHAN_VIEN, null, nv);

        insertEmployeeLogin(db, "nv01", DEFAULT_EMPLOYEE_PASSWORD, "Nguyễn Xuân Đạt");

        long hd1 = insertHoaDon(db, "HD001", "Nguyễn Xuân Đạt", "Nguyễn Văn An", "2025-03-10", 143000);
        insertChiTiet(db, hd1, 1, "Bánh quy bơ LU Pháp", 3, 45000);
        insertChiTiet(db, hd1, 2, "Snack mực lăn muối ớt", 1, 8000);

        long hd2 = insertHoaDon(db, "HD002", "Nguyễn Xuân Đạt", "Trần Thị Bình", "2025-04-15", 62000);
        insertChiTiet(db, hd2, 3, "Snack khoai tây Lays", 4, 12000);
        insertChiTiet(db, hd2, 5, "Kẹo sữa sô cô la", 1, 25000);

        long hd3 = insertHoaDon(db, "HD003", "Nguyễn Xuân Đạt", "Nguyễn Văn An", "2025-04-20", 30000);
        insertChiTiet(db, hd3, 4, "Bánh gạo One One", 1, 30000);
    }

    private long insertHoaDon(SQLiteDatabase db, String ma, String tenNv, String tenKh, String ngay, double tong) {
        ContentValues v = new ContentValues();
        v.put("ma_hoa_don", ma);
        v.put("ten_nhan_vien", tenNv);
        v.put("ten_khach_hang", tenKh);
        v.put("ngay_lap", ngay);
        v.put("tong_tien", tong);
        return db.insert(TABLE_HOA_DON, null, v);
    }

    private void insertEmployeeLogin(SQLiteDatabase db, String usernameLower, String password, String fullname) {
        ContentValues eu = new ContentValues();
        eu.put("username", usernameLower.toLowerCase(Locale.ROOT));
        eu.put("password", password);
        eu.put("fullname", fullname);
        eu.put("role", com.xdee.jpmart.model.NguoiDung.ROLE_EMPLOYEE);
        db.insert(TABLE_NGUOI_DUNG, null, eu);
    }

    private void insertChiTiet(SQLiteDatabase db, long hoaDonId, int spId, String tenSp, int sl, double gia) {
        ContentValues v = new ContentValues();
        v.put("hoa_don_id", hoaDonId);
        v.put("san_pham_id", spId);
        v.put("ten_sp", tenSp);
        v.put("so_luong", sl);
        v.put("don_gia", gia);
        db.insert(TABLE_CHI_TIET, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHI_TIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOA_DON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHAN_VIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHACH_HANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAN_PHAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DANH_MUC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NGUOI_DUNG);
        onCreate(db);
    }

    // --- User ---
    public NguoiDung authenticateUser(String username, String password) {
        String userKey = username.trim().toLowerCase(Locale.ROOT);
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NGUOI_DUNG, new String[]{"username", "password", "fullname", "role"},
                "username=? AND password=?", new String[]{userKey, password},
                null, null, null);
        NguoiDung nd = null;
        if (c.moveToFirst()) {
            nd = new NguoiDung();
            nd.setUsername(c.getString(0));
            nd.setPassword(c.getString(1));
            nd.setFullname(c.getString(2));
            String role = c.getString(3);
            nd.setRole(role != null ? role : com.xdee.jpmart.model.NguoiDung.ROLE_ADMIN);
        }
        c.close();
        return nd;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("password", newPassword);
        int n = db.update(TABLE_NGUOI_DUNG, v, "username=?", new String[]{username});
        return n > 0;
    }

    // --- Danh mục ---
    public List<DanhMuc> getAllCategories() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_DANH_MUC, null, null, null, null, null, "id ASC");
        while (c.moveToNext()) {
            DanhMuc d = new DanhMuc();
            d.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            d.setMa(c.getString(c.getColumnIndexOrThrow("ma")));
            d.setTen(c.getString(c.getColumnIndexOrThrow("ten")));
            list.add(d);
        }
        c.close();
        return list;
    }

    public boolean addCategory(String ten) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DANH_MUC, null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        String ma = "DM" + (n + 1);
        ContentValues v = new ContentValues();
        v.put("ma", ma);
        v.put("ten", ten);
        return db.insert(TABLE_DANH_MUC, null, v) != -1;
    }

    public boolean updateCategory(int id, String ma, String ten) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("ma", ma);
        v.put("ten", ten);
        return db.update(TABLE_DANH_MUC, v, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_DANH_MUC, new String[]{"id"}, "id!=?", new String[]{String.valueOf(id)},
                null, null, "id ASC", "1");
        int fallbackCat = -1;
        if (c.moveToFirst()) {
            fallbackCat = c.getInt(0);
        }
        c.close();
        if (fallbackCat == -1) {
            return false;
        }
        ContentValues mv = new ContentValues();
        mv.put("category_id", fallbackCat);
        db.update(TABLE_SAN_PHAM, mv, "category_id=?", new String[]{String.valueOf(id)});
        return db.delete(TABLE_DANH_MUC, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- Sản phẩm ---
    public List<SanPham> getAllProducts() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.id, sp.name, sp.price, sp.quantity, sp.category_id, sp.import_date, dm.ten "
                + "FROM " + TABLE_SAN_PHAM + " sp LEFT JOIN " + TABLE_DANH_MUC + " dm ON sp.category_id = dm.id "
                + "ORDER BY sp.name";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            SanPham sp = new SanPham();
            sp.setId(c.getInt(0));
            sp.setName(c.getString(1));
            sp.setPrice(c.getDouble(2));
            sp.setQuantity(c.getInt(3));
            sp.setCategoryId(c.getInt(4));
            if (!c.isNull(5)) {
                sp.setImportDate(c.getString(5));
            }
            sp.setCategoryName(c.getString(6));
            list.add(sp);
        }
        c.close();
        return list;
    }

    public boolean addProduct(SanPham sp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", sp.getName());
        v.put("price", sp.getPrice());
        v.put("quantity", sp.getQuantity());
        v.put("category_id", sp.getCategoryId());
        v.put("import_date", sp.getImportDate() != null ? sp.getImportDate() : "");
        return db.insert(TABLE_SAN_PHAM, null, v) != -1;
    }

    public boolean updateProduct(SanPham sp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", sp.getName());
        v.put("price", sp.getPrice());
        v.put("quantity", sp.getQuantity());
        v.put("category_id", sp.getCategoryId());
        v.put("import_date", sp.getImportDate() != null ? sp.getImportDate() : "");
        return db.update(TABLE_SAN_PHAM, v, "id=?", new String[]{String.valueOf(sp.getId())}) > 0;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_SAN_PHAM, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- Khách hàng ---
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_KHACH_HANG, null, null, null, null, null, "id ASC");
        while (c.moveToNext()) {
            KhachHang k = new KhachHang();
            k.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            k.setMa(c.getString(c.getColumnIndexOrThrow("ma")));
            k.setHoTen(c.getString(c.getColumnIndexOrThrow("ho_ten")));
            k.setSdt(c.getString(c.getColumnIndexOrThrow("sdt")));
            k.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            k.setDiaChi(c.getString(c.getColumnIndexOrThrow("dia_chi")));
            list.add(k);
        }
        c.close();
        return list;
    }

    public boolean addKhachHang(KhachHang k) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("ma", k.getMa());
        v.put("ho_ten", k.getHoTen());
        v.put("sdt", k.getSdt());
        v.put("email", k.getEmail());
        v.put("dia_chi", k.getDiaChi());
        return db.insert(TABLE_KHACH_HANG, null, v) != -1;
    }

    public boolean deleteKhachHang(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_KHACH_HANG, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- Nhân viên ---
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NHAN_VIEN, null, null, null, null, null, "id ASC");
        while (c.moveToNext()) {
            NhanVien nv = new NhanVien();
            nv.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            nv.setMa(c.getString(c.getColumnIndexOrThrow("ma")));
            nv.setHoTen(c.getString(c.getColumnIndexOrThrow("ho_ten")));
            nv.setDiaChi(c.getString(c.getColumnIndexOrThrow("dia_chi")));
            nv.setChucVu(c.getString(c.getColumnIndexOrThrow("chuc_vu")));
            nv.setLuong(c.getDouble(c.getColumnIndexOrThrow("luong")));
            list.add(nv);
        }
        c.close();
        return list;
    }

    public boolean addNhanVien(NhanVien nv) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NHAN_VIEN, null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        String ma = "NV" + String.format("%02d", n + 1);
        ContentValues v = new ContentValues();
        v.put("ma", ma);
        v.put("ho_ten", nv.getHoTen());
        v.put("dia_chi", nv.getDiaChi());
        v.put("chuc_vu", nv.getChucVu());
        v.put("luong", nv.getLuong());
        long ins = db.insert(TABLE_NHAN_VIEN, null, v);
        if (ins == -1) {
            return false;
        }
        insertEmployeeLogin(db, ma.toLowerCase(Locale.ROOT), DEFAULT_EMPLOYEE_PASSWORD, nv.getHoTen());
        return true;
    }

    public boolean updateNhanVien(NhanVien nv) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("ho_ten", nv.getHoTen());
        v.put("dia_chi", nv.getDiaChi());
        v.put("chuc_vu", nv.getChucVu());
        v.put("luong", nv.getLuong());
        int n = db.update(TABLE_NHAN_VIEN, v, "id=?", new String[]{String.valueOf(nv.getId())});
        if (n <= 0) {
            return false;
        }
        Cursor c = db.query(TABLE_NHAN_VIEN, new String[]{"ma"}, "id=?",
                new String[]{String.valueOf(nv.getId())}, null, null, null);
        if (c.moveToFirst()) {
            String ma = c.getString(0);
            ContentValues u = new ContentValues();
            u.put("fullname", nv.getHoTen());
            db.update(TABLE_NGUOI_DUNG, u, "username=? AND role=?",
                    new String[]{ma.toLowerCase(Locale.ROOT), com.xdee.jpmart.model.NguoiDung.ROLE_EMPLOYEE});
        }
        c.close();
        return true;
    }

    public boolean deleteNhanVien(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_NHAN_VIEN, new String[]{"ma"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null);
        String ma = null;
        if (c.moveToFirst()) {
            ma = c.getString(0);
        }
        c.close();
        if (ma != null) {
            db.delete(TABLE_NGUOI_DUNG, "username=? AND role=?",
                    new String[]{ma.toLowerCase(Locale.ROOT), com.xdee.jpmart.model.NguoiDung.ROLE_EMPLOYEE});
        }
        return db.delete(TABLE_NHAN_VIEN, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- Hóa đơn ---
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_HOA_DON, null, null, null, null, null, "ngay_lap DESC, id DESC");
        while (c.moveToNext()) {
            HoaDon h = new HoaDon();
            h.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            h.setMaHoaDon(c.getString(c.getColumnIndexOrThrow("ma_hoa_don")));
            h.setTenNhanVien(c.getString(c.getColumnIndexOrThrow("ten_nhan_vien")));
            h.setTenKhachHang(c.getString(c.getColumnIndexOrThrow("ten_khach_hang")));
            h.setNgayLap(c.getString(c.getColumnIndexOrThrow("ngay_lap")));
            h.setTongTien(c.getDouble(c.getColumnIndexOrThrow("tong_tien")));
            list.add(h);
        }
        c.close();
        return list;
    }

    public boolean deleteHoaDon(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CHI_TIET, "hoa_don_id=?", new String[]{String.valueOf(id)});
        return db.delete(TABLE_HOA_DON, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<ChiTietHoaDon> getChiTietByHoaDonId(int hoaDonId) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CHI_TIET, null, "hoa_don_id=?",
                new String[]{String.valueOf(hoaDonId)}, null, null, "id ASC");
        while (c.moveToNext()) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            ct.setHoaDonId(c.getInt(c.getColumnIndexOrThrow("hoa_don_id")));
            ct.setSanPhamId(c.getInt(c.getColumnIndexOrThrow("san_pham_id")));
            ct.setTenSp(c.getString(c.getColumnIndexOrThrow("ten_sp")));
            ct.setSoLuong(c.getInt(c.getColumnIndexOrThrow("so_luong")));
            ct.setDonGia(c.getDouble(c.getColumnIndexOrThrow("don_gia")));
            list.add(ct);
        }
        c.close();
        return list;
    }

    /**
     * Tạo hóa đơn từ giỏ: ghi {@code hoa_don}, {@code chi_tiet_hoa_don}, trừ tồn kho.
     *
     * @param tenNhanVien   tên nhân viên lập (thường lấy từ phiên đăng nhập)
     * @param tenKhachHang  tên người mua
     * @param items         dòng giỏ (copy trước khi gọi — sau đó nên clear giỏ ở UI)
     * @return {@code true} nếu thành công
     */
    public boolean createBillFromCart(String tenNhanVien, String tenKhachHang, List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        String khach = tenKhachHang != null && !tenKhachHang.trim().isEmpty()
                ? tenKhachHang.trim() : DEFAULT_CHECKOUT_CUSTOMER_NAME;
        String nv = tenNhanVien != null && !tenNhanVien.trim().isEmpty()
                ? tenNhanVien.trim() : "Nhân viên";

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (CartItem ci : items) {
                Cursor c = db.query(TABLE_SAN_PHAM, new String[]{"quantity"}, "id=?",
                        new String[]{String.valueOf(ci.getProductId())}, null, null, null);
                if (!c.moveToFirst()) {
                    c.close();
                    return false;
                }
                int stock = c.getInt(0);
                c.close();
                if (stock < ci.getQuantity()) {
                    return false;
                }
            }

            double tong = 0;
            for (CartItem ci : items) {
                tong += ci.getLineTotal();
            }

            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            String maHd = nextMaHoaDon(db);

            ContentValues hd = new ContentValues();
            hd.put("ma_hoa_don", maHd);
            hd.put("ten_nhan_vien", nv);
            hd.put("ten_khach_hang", khach);
            hd.put("ngay_lap", ngay);
            hd.put("tong_tien", tong);
            long hoaDonId = db.insert(TABLE_HOA_DON, null, hd);
            if (hoaDonId == -1) {
                return false;
            }

            for (CartItem ci : items) {
                ContentValues ct = new ContentValues();
                ct.put("hoa_don_id", hoaDonId);
                ct.put("san_pham_id", ci.getProductId());
                ct.put("ten_sp", ci.getName());
                ct.put("so_luong", ci.getQuantity());
                ct.put("don_gia", ci.getPrice());
                if (db.insert(TABLE_CHI_TIET, null, ct) == -1) {
                    return false;
                }

                Cursor c = db.query(TABLE_SAN_PHAM, new String[]{"quantity"}, "id=?",
                        new String[]{String.valueOf(ci.getProductId())}, null, null, null);
                c.moveToFirst();
                int stock = c.getInt(0);
                c.close();
                ContentValues up = new ContentValues();
                up.put("quantity", stock - ci.getQuantity());
                db.update(TABLE_SAN_PHAM, up, "id=?", new String[]{String.valueOf(ci.getProductId())});
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    private String nextMaHoaDon(SQLiteDatabase db) {
        Cursor c = db.rawQuery(
                "SELECT IFNULL(MAX(CAST(SUBSTR(" + "ma_hoa_don" + ", 3) AS INTEGER)), 0) FROM " + TABLE_HOA_DON
                        + " WHERE ma_hoa_don LIKE 'HD%'",
                null);
        int max = 0;
        if (c.moveToFirst()) {
            max = c.getInt(0);
        }
        c.close();
        return String.format(Locale.US, "HD%03d", max + 1);
    }

    // --- Thống kê ---
    public double sumDoanhThu(String ngayTuIso, String ngayDenIso) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT IFNULL(SUM(tong_tien),0) FROM " + TABLE_HOA_DON
                        + " WHERE ngay_lap >= ? AND ngay_lap <= ?",
                new String[]{ngayTuIso, ngayDenIso});
        double sum = 0;
        if (c.moveToFirst()) {
            sum = c.getDouble(0);
        }
        c.close();
        return sum;
    }

    public List<TopSanPhamStat> getTopSanPhamBanChay(String ngayTuIso, String ngayDenIso, int limit) {
        List<TopSanPhamStat> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT sp.id, sp.name, SUM(ct.so_luong) AS sl "
                + "FROM " + TABLE_CHI_TIET + " ct "
                + "JOIN " + TABLE_HOA_DON + " hd ON ct.hoa_don_id = hd.id "
                + "JOIN " + TABLE_SAN_PHAM + " sp ON ct.san_pham_id = sp.id "
                + "WHERE hd.ngay_lap >= ? AND hd.ngay_lap <= ? "
                + "GROUP BY sp.id "
                + "ORDER BY sl DESC "
                + "LIMIT ?";
        Cursor c = db.rawQuery(sql, new String[]{ngayTuIso, ngayDenIso, String.valueOf(limit)});
        while (c.moveToNext()) {
            list.add(new TopSanPhamStat(c.getInt(0), c.getString(1), c.getInt(2)));
        }
        c.close();
        return list;
    }

    public List<TopKhachStat> getTopKhachHangChiTieu(int limit) {
        List<TopKhachStat> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT ten_khach_hang, SUM(tong_tien) AS s FROM " + TABLE_HOA_DON
                + " GROUP BY ten_khach_hang ORDER BY s DESC LIMIT ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(limit)});
        while (c.moveToNext()) {
            list.add(new TopKhachStat(c.getString(0), c.getDouble(1)));
        }
        c.close();
        return list;
    }
}
