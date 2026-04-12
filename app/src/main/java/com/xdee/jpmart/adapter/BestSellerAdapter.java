package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.TopSanPhamStat;

import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.Holder> {

    private List<TopSanPhamStat> data;

    public BestSellerAdapter(List<TopSanPhamStat> data) {
        this.data = data;
    }

    public void setData(List<TopSanPhamStat> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_seller, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TopSanPhamStat s = data.get(position);
        h.tvName.setText(s.getTenSanPham());
        h.tvQty.setText("Tổng số lượng: " + s.getTongSoLuong());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView tvName;
        final TextView tvQty;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQty = itemView.findViewById(R.id.tvTotalQuantity);
        }
    }
}
