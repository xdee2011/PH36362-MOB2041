package com.xdee.jpmart.ui.analytics;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.xdee.jpmart.adapter.BestSellerAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.TopSanPhamStat;
import com.xdee.jpmart.util.AdminUi;
import com.xdee.jpmart.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class BestSeller extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdminUi.blockIfEmployee(this)) {
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_best_seller);
        ToolbarHelper.setupBack(this, "Bán chạy");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtFrom = findViewById(R.id.edtFromDate);
        EditText edtTo = findViewById(R.id.edtToDate);
        EditText edtLimit = findViewById(R.id.edtProductCount);
        Button btn = findViewById(R.id.btnProductStats);
        TextView tvMsg = findViewById(R.id.tvInputMessage);
        RecyclerView rv = findViewById(R.id.rvBestSeller);

        DatabaseHelper db = new DatabaseHelper(this);
        List<TopSanPhamStat> data = new ArrayList<>();
        BestSellerAdapter adapter = new BestSellerAdapter(data);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btn.setOnClickListener(v -> {
            String from = DateUtil.normalizeToIso(edtFrom.getText().toString());
            String to = DateUtil.normalizeToIso(edtTo.getText().toString());
            String limStr = edtLimit.getText().toString().trim();
            if (from == null || to == null || limStr.isEmpty()) {
                tvMsg.setText("Vui lòng nhập đủ: từ ngày, đến ngày, số lượng sản phẩm.");
                return;
            }
            if (from.compareTo(to) > 0) {
                tvMsg.setText("Từ ngày không được sau đến ngày.");
                return;
            }
            int limit;
            try {
                limit = Integer.parseInt(limStr);
                if (limit <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                tvMsg.setText("Số lượng phải là số nguyên dương.");
                return;
            }
            tvMsg.setText("");
            data.clear();
            data.addAll(db.getTopSanPhamBanChay(from, to, limit));
            adapter.notifyDataSetChanged();
            if (data.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu trong khoảng thời gian này", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
