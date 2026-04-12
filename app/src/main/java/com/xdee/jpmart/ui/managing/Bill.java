package com.xdee.jpmart.ui.managing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.BillAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.HoaDon;

import java.util.ArrayList;
import java.util.List;

public class Bill extends AppCompatActivity implements BillAdapter.Listener {

    public static final String EXTRA_HOA_DON_ID = "hoa_don_id";

    private DatabaseHelper db;
    private BillAdapter adapter;
    private final List<HoaDon> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill);
        ToolbarHelper.setupBack(this, "Hóa đơn");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        RecyclerView rv = findViewById(R.id.rvBill);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BillAdapter(list, this);
        rv.setAdapter(adapter);

        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        list.clear();
        list.addAll(db.getAllHoaDon());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOpen(HoaDon h) {
        Intent i = new Intent(this, BillDetail.class);
        i.putExtra(EXTRA_HOA_DON_ID, h.getId());
        startActivity(i);
    }

    @Override
    public void onDelete(HoaDon h) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa hóa đơn")
                .setMessage("Xóa " + h.getMaHoaDon() + "?")
                .setPositiveButton("Đồng ý", (d, w) -> {
                    if (db.deleteHoaDon(h.getId())) {
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
