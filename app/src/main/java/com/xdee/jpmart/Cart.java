package com.xdee.jpmart;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.Session;
import com.xdee.jpmart.adapter.CartAdapter;
import com.xdee.jpmart.cart.CartManager;
import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.CartItem;
import com.xdee.jpmart.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements CartAdapter.Listener {

    private CartAdapter adapter;
    private TextView tvTotal;
    private final List<CartItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ToolbarHelper.setupBack(this, "Giỏ hàng");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTotal = findViewById(R.id.tvTotalAmount);
        RecyclerView rv = findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(list, this);
        rv.setAdapter(adapter);

        findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }
            List<CartItem> snapshot = new ArrayList<>(CartManager.getItems());
            String tenNv = Session.getFullname(this);
            if (tenNv == null || tenNv.trim().isEmpty()) {
                String u = Session.getUsername(this);
                tenNv = u != null ? u : "Nhân viên";
            }
            DatabaseHelper db = new DatabaseHelper(this);
            boolean ok = db.createBillFromCart(
                    tenNv,
                    DatabaseHelper.DEFAULT_CHECKOUT_CUSTOMER_NAME,
                    snapshot);
            if (ok) {
                CartManager.clear();
                refreshUi();
                Toast.makeText(this, "Đã thanh toán và tạo hóa đơn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không tạo được hóa đơn (kiểm tra tồn kho)", Toast.LENGTH_SHORT).show();
            }
        });

        refreshUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();
    }

    private void refreshUi() {
        list.clear();
        list.addAll(CartManager.getItems());
        adapter.notifyDataSetChanged();
        tvTotal.setText(DateUtil.formatMoneyVi(CartManager.getTotal()));
    }

    @Override
    public void onCartChanged() {
        refreshUi();
    }
}
