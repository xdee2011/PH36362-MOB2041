package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.cart.CartManager;
import com.xdee.jpmart.model.SanPham;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder> {

    public interface Listener {
        void onEdit(SanPham sp);

        void onDelete(SanPham sp);
    }

    private List<SanPham> data;
    private final Listener listener;

    public ProductAdapter(List<SanPham> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<SanPham> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        SanPham sp = data.get(position);
        h.tvName.setText(sp.getName());
        h.tvPrice.setText(String.format(Locale.getDefault(), "Giá: %,.0f đ", sp.getPrice()));
        h.tvStock.setText(String.format(Locale.getDefault(), "Tồn kho: %d", sp.getQuantity()));
        h.btnAddToCart.setOnClickListener(v -> {
            if (CartManager.addProduct(sp)) {
                Toast.makeText(v.getContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Không thêm được (hết hàng hoặc đủ số lượng tồn)", Toast.LENGTH_SHORT).show();
            }
        });
        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(sp);
            }
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(sp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView tvName;
        final TextView tvPrice;
        final TextView tvStock;
        final ImageButton btnAddToCart;
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
