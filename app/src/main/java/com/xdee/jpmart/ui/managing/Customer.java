package com.xdee.jpmart.ui.managing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.CustomerAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.KhachHang;

import java.util.ArrayList;
import java.util.List;

public class Customer extends AppCompatActivity implements CustomerAdapter.Listener {

    private DatabaseHelper db;
    private CustomerAdapter adapter;
    private final List<KhachHang> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer);
        ToolbarHelper.setupBack(this, "Khách hàng");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        RecyclerView rv = findViewById(R.id.rvCustomer);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(list, this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddCustomer);
        fab.setOnClickListener(v -> showAddDialog());

        reload();
    }

    private void reload() {
        list.clear();
        list.addAll(db.getAllKhachHang());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_add_customer, null);
        EditText edtMa = root.findViewById(R.id.edtMaKhach);
        EditText edtTen = root.findViewById(R.id.edtHoTen);
        EditText edtSdt = root.findViewById(R.id.edtSdt);
        EditText edtEmail = root.findViewById(R.id.edtEmail);
        EditText edtDc = root.findViewById(R.id.edtDiaChi);

        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();
        root.findViewById(R.id.btnCancel).setOnClickListener(x -> dlg.dismiss());
        root.findViewById(R.id.btnSave).setOnClickListener(x -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Nhập mã và họ tên", Toast.LENGTH_SHORT).show();
                return;
            }
            KhachHang k = new KhachHang();
            k.setMa(ma);
            k.setHoTen(ten);
            k.setSdt(edtSdt.getText().toString().trim());
            k.setEmail(edtEmail.getText().toString().trim());
            k.setDiaChi(edtDc.getText().toString().trim());
            if (db.addKhachHang(k)) {
                Toast.makeText(this, "Đã thêm khách hàng", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
                reload();
            } else {
                Toast.makeText(this, "Mã khách hàng có thể đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

    @Override
    public void onDelete(KhachHang k) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa khách hàng")
                .setMessage("Xóa " + k.getHoTen() + "?")
                .setPositiveButton("Đồng ý", (d, w) -> {
                    if (db.deleteKhachHang(k.getId())) {
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        reload();
                    } else {
                        Toast.makeText(this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
