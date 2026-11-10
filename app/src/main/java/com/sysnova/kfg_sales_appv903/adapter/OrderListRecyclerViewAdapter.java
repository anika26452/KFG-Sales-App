package com.sysnova.kfg_sales_appv903.adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sysnova.kfg_sales_appv903.modal.OrderListModal;
import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.Activity.DatabaseHelper;
import com.sysnova.kfg_sales_appv903.Activity.OrderListActivity;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/*
Created by Shanzida
*/
public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<OrderListRecyclerViewAdapter.ViewHolder> {

    List<OrderListModal> orderListActivities;
    Context mContext;

    public OrderListRecyclerViewAdapter(ArrayList<OrderListModal> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.customerTextView.setText(orderListActivities.get(position).getProductname());
            holder.productTextView.setText(orderListActivities.get(position).getCustomernamevalue());
            holder.unitPriceTextView.setText(String.valueOf(orderListActivities.get(position).getQuantity()));
            holder.quantityTextView.setText(String.valueOf(orderListActivities.get(position).getTotal()));
            holder.unitTextView.setText(orderListActivities.get(position).getUom());


            holder.tableRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                    final Dialog dialog = new Dialog(mContext);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.custom);

                    Button yesBtn = dialog.findViewById(R.id.dialogButtonOK);
                    Button noBtn  = dialog.findViewById(R.id.cancel);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ///shanzida

                            databaseHelper.deleteSalesOrderLine(orderListActivities.get(position).getProductID(),orderListActivities.get(position).getDocno());
                            orderListActivities.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,orderListActivities.size());
                            OrderListActivity orderListActivity = new OrderListActivity();
                            orderListActivity.loadOrderPrice(mContext);
                            dialog.dismiss();
                        }
                    });

                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView customerTextView,productTextView,unitPriceTextView,quantityTextView,unitTextView,priceTextView;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            customerTextView = itemView.findViewById(R.id.customer_name);
            productTextView  = itemView.findViewById(R.id.product_name);
            unitPriceTextView = itemView.findViewById(R.id.price_unit);
            quantityTextView = itemView.findViewById(R.id.quantity_order);
            unitTextView     = itemView.findViewById(R.id.uom);

        }
    }
}
