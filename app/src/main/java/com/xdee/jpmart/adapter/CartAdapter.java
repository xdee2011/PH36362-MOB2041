package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.cart.CartManager;
import com.xdee.jpmart.model.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Holder> {

    public interface Listener {
        void onCartChanged();
    }

    private List<CartItem> data;
    private final Listener listener;

    public CartAdapter(List<CartItem> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<CartItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        CartItem item = data.get(position);
        int pid = item.getProductId();
        h.tvName.setText(item.getName());
        h.tvPrice.setText(String.format(Locale.getDefault(), "Giá: %,.0f đ", item.getPrice()));
        h.tvQty.setText(String.valueOf(item.getQuantity()));

        h.btnDelete.setOnClickListener(v -> {
            CartManager.remove(pid);
            if (listener != null) {
                listener.onCartChanged();
            }
        });
        h.btnDecrease.setOnClickListener(v -> {
            CartManager.decrement(pid);
            if (listener != null) {
                listener.onCartChanged();
            }
        });
        h.btnIncrease.setOnClickListener(v -> {
            CartManager.increment(pid);
            if (listener != null) {
                listener.onCartChanged();
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
        final TextView tvQty;
        final ImageButton btnDelete;
        final ImageButton btnDecrease;
        final ImageButton btnIncrease;

        Holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQty = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
}
