package com.xdee.jpmart.ui.managing;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.BillDetailAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.ChiTietHoaDon;

import java.util.ArrayList;
import java.util.List;

public class BillDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_detail);
        ToolbarHelper.setupBack(this, "Chi tiết hóa đơn");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int hid = getIntent().getIntExtra(Bill.EXTRA_HOA_DON_ID, -1);
        if (hid < 0) {
            Toast.makeText(this, "Thiếu hóa đơn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        List<ChiTietHoaDon> lines = new ArrayList<>(db.getChiTietByHoaDonId(hid));

        RecyclerView rv = findViewById(R.id.rvBillDetail);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new BillDetailAdapter(lines));
    }
}
