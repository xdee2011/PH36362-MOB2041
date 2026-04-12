package com.xdee.jpmart.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xdee.jpmart.R;
import com.xdee.jpmart.ToolbarHelper;
import com.xdee.jpmart.Session;
import com.xdee.jpmart.database.DatabaseHelper;

public class ChangePassword extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ToolbarHelper.setupBack(this, "Đổi mật khẩu");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String user = Session.getUsername(this);
        if (user == null) {
            finish();
            return;
        }

        db = new DatabaseHelper(this);
        EditText edtOld = findViewById(R.id.edtOldPassword);
        EditText edtNew = findViewById(R.id.edtNewPassword);
        EditText edtConfirm = findViewById(R.id.edtConfirmNewPassword);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> {
            String oldP = edtOld.getText().toString();
            String newP = edtNew.getText().toString();
            String cf = edtConfirm.getText().toString();
            if (oldP.isEmpty() || newP.isEmpty() || cf.isEmpty()) {
                Toast.makeText(this, "Nhập đủ các trường", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newP.equals(cf)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.authenticateUser(user, oldP) == null) {
                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.updatePassword(user, newP)) {
                Toast.makeText(this, "Đã đổi mật khẩu", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Không cập nhật được", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
