package com.xdee.jpmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xdee.jpmart.ui.analytics.BestSeller;
import com.xdee.jpmart.ui.analytics.Revenue;
import com.xdee.jpmart.ui.analytics.TopCustomer;
import com.xdee.jpmart.ui.managing.Bill;
import com.xdee.jpmart.ui.managing.Category;
import com.xdee.jpmart.ui.managing.Customer;
import com.xdee.jpmart.ui.managing.Employee;
import com.xdee.jpmart.ui.managing.Product;
import com.xdee.jpmart.ui.user.ChangePassword;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Session.isLoggedIn(this)) {
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Session.isEmployee(this)) {
            findViewById(R.id.tvSectionAnalytics).setVisibility(View.GONE);
            findViewById(R.id.gridAnalytics).setVisibility(View.GONE);
            findViewById(R.id.btnEmployees).setVisibility(View.GONE);
        }

        findViewById(R.id.btnRevenue).setOnClickListener(v ->
                startActivity(new Intent(this, Revenue.class)));
        findViewById(R.id.btnTopProducts).setOnClickListener(v ->
                startActivity(new Intent(this, BestSeller.class)));
        findViewById(R.id.btnTopCustomers).setOnClickListener(v ->
                startActivity(new Intent(this, TopCustomer.class)));

        findViewById(R.id.btnProducts).setOnClickListener(v ->
                startActivity(new Intent(this, Product.class)));
        findViewById(R.id.btnCustomers).setOnClickListener(v ->
                startActivity(new Intent(this, Customer.class)));
        findViewById(R.id.btnInvoices).setOnClickListener(v ->
                startActivity(new Intent(this, Bill.class)));
        findViewById(R.id.btnCategories).setOnClickListener(v ->
                startActivity(new Intent(this, Category.class)));
        findViewById(R.id.btnEmployees).setOnClickListener(v ->
                startActivity(new Intent(this, Employee.class)));

        findViewById(R.id.btnChangePassword).setOnClickListener(v ->
                startActivity(new Intent(this, ChangePassword.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Session.clear(this);
            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
