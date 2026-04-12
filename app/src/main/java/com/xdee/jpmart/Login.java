package com.xdee.jpmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xdee.jpmart.database.DatabaseHelper;
import com.xdee.jpmart.model.NguoiDung;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private static final String PREFS_LOGIN = "jpmart_login";

    private EditText edtUsername;
    private EditText edtPassword;
    private CheckBox chkRemember;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        chkRemember = findViewById(R.id.chkRemember);
        Button btnLogin = findViewById(R.id.btnLogin);

        SharedPreferences rp = getSharedPreferences(PREFS_LOGIN, MODE_PRIVATE);
        if (rp.getBoolean("remember", false)) {
            edtUsername.setText(rp.getString("user", ""));
            edtPassword.setText(rp.getString("pass", ""));
            chkRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String user = edtUsername.getText().toString().trim().toLowerCase(Locale.ROOT);
            String pass = edtPassword.getText().toString().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            NguoiDung nd = db.authenticateUser(user, pass);
            if (nd != null) {
                Session.setLoggedInUser(this, nd);
                SharedPreferences.Editor e = rp.edit();
                if (chkRemember.isChecked()) {
                    e.putBoolean("remember", true);
                    e.putString("user", user);
                    e.putString("pass", pass);
                } else {
                    e.clear();
                }
                e.apply();
                Toast.makeText(this, "Xin chào " + nd.getFullname(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainMenu.class));
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
