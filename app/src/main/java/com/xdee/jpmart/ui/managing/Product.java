package com.xdee.jpmart.ui.managing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xdee.jpmart.Cart;
import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.ProductAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.DanhMuc;
import com.xdee.jpmart.model.SanPham;
import com.xdee.jpmart.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class Product extends AppCompatActivity implements ProductAdapter.Listener {

    private DatabaseHelper db;
    private ProductAdapter adapter;
    private final List<SanPham> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ToolbarHelper.setupBack(this, "Sản phẩm");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        findViewById(R.id.btnCart).setOnClickListener(v ->
                startActivity(new Intent(this, Cart.class)));

        RecyclerView rv = findViewById(R.id.rvProduct);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(list, this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(v -> showProductDialog(null));

        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        list.clear();
        list.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEdit(SanPham sp) {
        showProductDialog(sp);
    }

    @Override
    public void onDelete(SanPham sp) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Xóa \"" + sp.getName() + "\"?")
                .setPositiveButton("Đồng ý", (d, w) -> {
                    if (db.deleteProduct(sp.getId())) {
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        reload();
                    } else {
                        Toast.makeText(this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showProductDialog(SanPham existing) {
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        TextView tvTitle = root.findViewById(R.id.tvDialogTitle);
        tvTitle.setText(existing == null ? "Thêm sản phẩm" : "Sửa sản phẩm");

        Spinner spCat = root.findViewById(R.id.spCategory);
        EditText edtName = root.findViewById(R.id.edtProductName);
        EditText edtPrice = root.findViewById(R.id.edtPrice);
        EditText edtQty = root.findViewById(R.id.edtQuantity);
        EditText edtDate = root.findViewById(R.id.edtImportDate);

        List<DanhMuc> cats = db.getAllCategories();
        ArrayAdapter<DanhMuc> catAd = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cats);
        catAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCat.setAdapter(catAd);

        if (existing != null) {
            edtName.setText(existing.getName());
            edtPrice.setText(String.valueOf((long) existing.getPrice()));
            edtQty.setText(String.valueOf(existing.getQuantity()));
            if (existing.getImportDate() != null) {
                edtDate.setText(existing.getImportDate());
            }
            for (int i = 0; i < cats.size(); i++) {
                if (cats.get(i).getId() == existing.getCategoryId()) {
                    spCat.setSelection(i);
                    break;
                }
            }
        }

        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();

        root.findViewById(R.id.btnCancel).setOnClickListener(v -> dlg.dismiss());
        root.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String pStr = edtPrice.getText().toString().trim();
            String qStr = edtQty.getText().toString().trim();
            if (name.isEmpty() || pStr.isEmpty() || qStr.isEmpty()) {
                Toast.makeText(this, "Nhập đủ tên, giá, số lượng", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double price = Double.parseDouble(pStr);
                int qty = Integer.parseInt(qStr);
                if (price <= 0 || qty < 0) {
                    Toast.makeText(this, "Giá/số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                int catId = cats.get(Math.max(0, spCat.getSelectedItemPosition())).getId();
                String imp = edtDate.getText().toString().trim();
                String iso = DateUtil.normalizeToIso(imp);
                if (!imp.isEmpty() && iso == null) {
                    Toast.makeText(this, "Ngày nhập không hợp lệ (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                    return;
                }
                SanPham sp = existing != null ? existing : new SanPham();
                if (existing != null) {
                    sp.setId(existing.getId());
                }
                sp.setName(name);
                sp.setPrice(price);
                sp.setQuantity(qty);
                sp.setCategoryId(catId);
                sp.setImportDate(iso != null ? iso : imp);

                boolean ok = existing == null ? db.addProduct(sp) : db.updateProduct(sp);
                if (ok) {
                    Toast.makeText(this, "Đã lưu", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                    reload();
                } else {
                    Toast.makeText(this, "Không lưu được", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
            }
        });

        dlg.show();
    }
}
