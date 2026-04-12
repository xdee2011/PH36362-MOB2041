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

import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.util.AdminUi;
import com.xdee.jpmart.util.DateUtil;

public class Revenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdminUi.blockIfEmployee(this)) {
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revenue);
        ToolbarHelper.setupBack(this, "Doanh thu");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtFrom = findViewById(R.id.edtFromDate);
        EditText edtTo = findViewById(R.id.edtToDate);
        Button btn = findViewById(R.id.btnRevenueStats);
        TextView tv = findViewById(R.id.tvRevenue);

        DatabaseHelper db = new DatabaseHelper(this);

        btn.setOnClickListener(v -> {
            String from = DateUtil.normalizeToIso(edtFrom.getText().toString());
            String to = DateUtil.normalizeToIso(edtTo.getText().toString());
            if (from == null || to == null) {
                Toast.makeText(this, "Nhập từ ngày / đến ngày hợp lệ (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (from.compareTo(to) > 0) {
                Toast.makeText(this, "Từ ngày không được sau đến ngày", Toast.LENGTH_SHORT).show();
                return;
            }
            double sum = db.sumDoanhThu(from, to);
            tv.setText("Doanh thu: " + DateUtil.formatMoneyVi(sum));
        });
    }
}
