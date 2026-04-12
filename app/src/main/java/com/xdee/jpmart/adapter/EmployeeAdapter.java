package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.NhanVien;

import java.util.List;
import java.util.Locale;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.Holder> {

    public interface Listener {
        void onEdit(NhanVien nv);

        void onDelete(NhanVien nv);
    }

    private List<NhanVien> data;
    private final Listener listener;

    public EmployeeAdapter(List<NhanVien> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<NhanVien> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        NhanVien nv = data.get(position);
        h.tvId.setText(nv.getMa());
        h.tvName.setText(nv.getHoTen());
        h.tvAddr.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
        h.tvRole.setText(nv.getChucVu() != null ? nv.getChucVu() : "");
        h.tvSalary.setText(String.format(Locale.getDefault(), "%,.0f đ", nv.getLuong()));
        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(nv);
            }
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(nv);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView tvId;
        final TextView tvName;
        final TextView tvAddr;
        final TextView tvRole;
        final TextView tvSalary;
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvEmployeeId);
            tvName = itemView.findViewById(R.id.tvEmployeeName);
            tvAddr = itemView.findViewById(R.id.tvEmployeeAddress);
            tvRole = itemView.findViewById(R.id.tvEmployeeRole);
            tvSalary = itemView.findViewById(R.id.tvEmployeeSalary);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
