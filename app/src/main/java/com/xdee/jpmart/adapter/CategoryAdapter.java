package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.DanhMuc;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    public interface Listener {
        void onEdit(DanhMuc dm);

        void onDelete(DanhMuc dm);
    }

    private List<DanhMuc> data;
    private final Listener listener;

    public CategoryAdapter(List<DanhMuc> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<DanhMuc> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        DanhMuc d = data.get(position);
        h.tvId.setText("ID: " + d.getMa());
        h.tvName.setText(d.getTen());
        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(d);
            }
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(d);
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
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvCategoryId);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
