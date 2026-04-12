package com.xdee.jpmart.ui.analytics;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.adapter.TopCustomerListAdapter;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.TopKhachStat;
import com.xdee.jpmart.util.AdminUi;

import java.util.ArrayList;
import java.util.List;

public class TopCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AdminUi.blockIfEmployee(this)) {
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_customer);
        ToolbarHelper.setupBack(this, "Top khách hàng");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rv = findViewById(R.id.rvTopCustomer);
        rv.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper db = new DatabaseHelper(this);
        List<TopKhachStat> data = new ArrayList<>(db.getTopKhachHangChiTieu(5));
        rv.setAdapter(new TopCustomerListAdapter(data));
    }
}
