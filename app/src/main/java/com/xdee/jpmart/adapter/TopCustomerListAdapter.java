package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.TopKhachStat;
import com.xdee.jpmart.util.DateUtil;

import java.util.List;

public class TopCustomerListAdapter extends RecyclerView.Adapter<TopCustomerListAdapter.Holder> {

    private List<TopKhachStat> data;

    public TopCustomerListAdapter(List<TopKhachStat> data) {
        this.data = data;
    }

    public void setData(List<TopKhachStat> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_customer, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TopKhachStat t = data.get(position);
        h.tvStt.setText(String.valueOf(position + 1));
        h.tvName.setText(t.getTenKhach());
        h.tvSpend.setText(DateUtil.formatMoneyVi(t.getTongChiTieu()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView tvStt;
        final TextView tvName;
        final TextView tvSpend;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tvStt);
            tvName = itemView.findViewById(R.id.tvCustomerName);
            tvSpend = itemView.findViewById(R.id.tvTotalSpend);
        }
    }
}
