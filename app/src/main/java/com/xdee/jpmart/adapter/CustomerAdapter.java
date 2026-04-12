package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.KhachHang;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.Holder> {

    public interface Listener {
        void onDelete(KhachHang k);
    }

    private List<KhachHang> data;
    private final Listener listener;

    public CustomerAdapter(List<KhachHang> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<KhachHang> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        KhachHang k = data.get(position);
        h.tvId.setText("Mã: " + k.getMa());
        h.tvName.setText("Tên: " + k.getHoTen());
        h.tvPhone.setText("SĐT: " + (k.getSdt() != null ? k.getSdt() : ""));
        h.tvEmail.setText("Email: " + (k.getEmail() != null ? k.getEmail() : ""));
        h.tvAddr.setText("Địa chỉ: " + (k.getDiaChi() != null ? k.getDiaChi() : ""));
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(k);
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
        final TextView tvPhone;
        final TextView tvEmail;
        final TextView tvAddr;
        final ImageButton btnDelete;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvCustomerId);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvEmail = itemView.findViewById(R.id.tvCustomerEmail);
            tvAddr = itemView.findViewById(R.id.tvCustomerAddress);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
