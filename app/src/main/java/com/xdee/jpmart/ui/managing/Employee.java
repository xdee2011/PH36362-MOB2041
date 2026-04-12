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
import com.xdee.jpmart.adapter.EmployeeAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.NhanVien;
import com.xdee.jpmart.util.AdminUi;

import java.util.ArrayList;
import java.util.List;

public class Employee extends AppCompatActivity implements EmployeeAdapter.Listener {

    private DatabaseHelper db;
    private EmployeeAdapter adapter;
    private final List<NhanVien> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdminUi.blockIfEmployee(this)) {
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ToolbarHelper.setupBack(this, "Nhân viên");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        RecyclerView rv = findViewById(R.id.rvEmployee);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmployeeAdapter(list, this);
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddEmployee);
        fab.setOnClickListener(v -> showAddDialog());

        reload();
    }

    private void reload() {
        list.clear();
        list.addAll(db.getAllNhanVien());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_add_employee, null);
        EditText edtName = root.findViewById(R.id.edtEmployeeName);
        EditText edtAddr = root.findViewById(R.id.edtEmployeeAddress);
        EditText edtRole = root.findViewById(R.id.edtEmployeeRole);
        EditText edtSal = root.findViewById(R.id.edtEmployeeSalary);

        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();
        root.findViewById(R.id.btnCancel).setOnClickListener(x -> dlg.dismiss());
        root.findViewById(R.id.btnAdd).setOnClickListener(x -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nhập tên nhân viên", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double luong = Double.parseDouble(edtSal.getText().toString().trim().isEmpty()
                        ? "0" : edtSal.getText().toString().trim());
                NhanVien nv = new NhanVien();
                nv.setHoTen(name);
                nv.setDiaChi(edtAddr.getText().toString().trim());
                nv.setChucVu(edtRole.getText().toString().trim());
                nv.setLuong(luong);
                if (db.addNhanVien(nv)) {
                    Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                    reload();
                } else {
                    Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Lương không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

    @Override
    public void onEdit(NhanVien nv) {
        View root = LayoutInflater.from(this).inflate(R.layout.dialog_edit_employee, null);
        EditText edtMa = root.findViewById(R.id.edtEmployeeId);
        edtMa.setText(nv.getMa());
        EditText edtName = root.findViewById(R.id.edtEmployeeName);
        edtName.setText(nv.getHoTen());
        EditText edtAddr = root.findViewById(R.id.edtEmployeeAddress);
        edtAddr.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
        EditText edtRole = root.findViewById(R.id.edtEmployeeRole);
        edtRole.setText(nv.getChucVu() != null ? nv.getChucVu() : "");
        EditText edtSal = root.findViewById(R.id.edtEmployeeSalary);
        edtSal.setText(String.valueOf((long) nv.getLuong()));

        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();
        root.findViewById(R.id.btnCancel).setOnClickListener(x -> dlg.dismiss());
        root.findViewById(R.id.btnSave).setOnClickListener(x -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nhập tên", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double luong = Double.parseDouble(edtSal.getText().toString().trim());
                nv.setHoTen(name);
                nv.setDiaChi(edtAddr.getText().toString().trim());
                nv.setChucVu(edtRole.getText().toString().trim());
                nv.setLuong(luong);
                if (db.updateNhanVien(nv)) {
                    Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                    reload();
                } else {
                    Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Lương không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

    @Override
    public void onDelete(NhanVien nv) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa nhân viên")
                .setMessage("Xóa " + nv.getHoTen() + "?")
                .setPositiveButton("Đồng ý", (d, w) -> {
                    if (db.deleteNhanVien(nv.getId())) {
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
