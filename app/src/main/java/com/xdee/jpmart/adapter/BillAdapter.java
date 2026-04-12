package com.xdee.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xdee.jpmart.R;
import com.xdee.jpmart.model.HoaDon;
import com.xdee.jpmart.util.DateUtil;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.Holder> {

    public interface Listener {
        void onOpen(HoaDon h);

        void onDelete(HoaDon h);
    }

    private List<HoaDon> data;
    private final Listener listener;

    public BillAdapter(List<HoaDon> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<HoaDon> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        HoaDon bill = data.get(position);
        h.tvBillId.setText(bill.getMaHoaDon());
        h.tvEmployee.setText(bill.getTenNhanVien() != null ? bill.getTenNhanVien() : "");
        h.tvCustomer.setText(bill.getTenKhachHang() != null ? bill.getTenKhachHang() : "");
        h.tvDate.setText(bill.getNgayLap());
        h.tvTotal.setText(DateUtil.formatMoneyVi(bill.getTongTien()));
        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpen(bill);
            }
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(bill);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView tvBillId;
        final TextView tvEmployee;
        final TextView tvCustomer;
        final TextView tvDate;
        final TextView tvTotal;
        final ImageButton btnDelete;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvBillId = itemView.findViewById(R.id.tvBillId);
            tvEmployee = itemView.findViewById(R.id.tvEmployeeName);
            tvCustomer = itemView.findViewById(R.id.tvCustomerName);
            tvDate = itemView.findViewById(R.id.tvCreatedDate);
            tvTotal = itemView.findViewById(R.id.tvTotalAmount);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
