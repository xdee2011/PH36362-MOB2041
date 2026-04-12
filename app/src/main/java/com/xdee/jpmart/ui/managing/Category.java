package com.xdee.jpmart.ui.managing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.CategoryAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.DanhMuc;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity implements CategoryAdapter.Listener {

    private DatabaseHelper db;
    private CategoryAdapter adapter;
    private final List<DanhMuc> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ToolbarHelper.setupBack(this, "Danh mục");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        RecyclerView rv = findViewById(R.id.rvCategory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(list, this);
        rv.setAdapter(adapter);

        findViewById(R.id.fabAddCategory).setOnClickListener(v -> showAddDialog());

        reload();
    }

    private void reload() {
        list.clear();
        list.addAll(db.getAllCategories());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View root = LayoutInflater.from(this).inflate(R.layout.add_category_dialog, null);
        EditText edtName = root.findViewById(R.id.edtCategoryName);
        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();
        root.findViewById(R.id.btnCancel).setOnClickListener(x -> dlg.dismiss());
        root.findViewById(R.id.btnSave).setOnClickListener(x -> {
            String ten = edtName.getText().toString().trim();
            if (ten.isEmpty()) {
                Toast.makeText(this, "Nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.addCategory(ten)) {
                Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
                reload();
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

    @Override
    public void onEdit(DanhMuc dm) {
        View root = LayoutInflater.from(this).inflate(R.layout.edit_category_dialog, null);
        TextView tvTitle = root.findViewById(R.id.tvDialogTitle);
        tvTitle.setText("Sửa danh mục");
        EditText edtMa = root.findViewById(R.id.edtCategoryId);
        edtMa.setText(dm.getMa());
        edtMa.setEnabled(false);
        EditText edtName = root.findViewById(R.id.edtCategoryName);
        edtName.setText(dm.getTen());

        AlertDialog dlg = new AlertDialog.Builder(this).setView(root).create();
        root.findViewById(R.id.btnCancel).setOnClickListener(x -> dlg.dismiss());
        root.findViewById(R.id.btnSave).setOnClickListener(x -> {
            String ten = edtName.getText().toString().trim();
            if (ten.isEmpty()) {
                Toast.makeText(this, "Nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.updateCategory(dm.getId(), dm.getMa(), ten)) {
                Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
                reload();
            } else {
                Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
        dlg.show();
    }

    @Override
    public void onDelete(DanhMuc dm) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Xóa \"" + dm.getTen() + "\"? Sản phẩm thuộc danh mục sẽ chuyển sang danh mục khác.")
                .setPositiveButton("Đồng ý", (d, w) -> {
                    if (db.deleteCategory(dm.getId())) {
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        reload();
                    } else {
                        Toast.makeText(this, "Không xóa được (cần ít nhất 1 danh mục)", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
