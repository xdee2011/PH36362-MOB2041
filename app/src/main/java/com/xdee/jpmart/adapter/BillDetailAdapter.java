package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.ChiTietHoaDon;

import java.util.List;
import java.util.Locale;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.Holder> {

    private List<ChiTietHoaDon> data;

    public BillDetailAdapter(List<ChiTietHoaDon> data) {
        this.data = data;
    }

    public void setData(List<ChiTietHoaDon> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_detail, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        ChiTietHoaDon ct = data.get(position);
        h.tvName.setText(ct.getTenSp());
        h.tvPrice.setText(String.format(Locale.getDefault(), "%,.0f đ", ct.getDonGia()));
        h.tvQty.setText("x" + ct.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView tvName;
        final TextView tvPrice;
        final TextView tvQty;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQty = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
