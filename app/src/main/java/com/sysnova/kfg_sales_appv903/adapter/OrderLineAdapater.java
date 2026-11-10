package com.sysnova.kfg_sales_appv903.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.modal.OrderLineModel;
import com.sysnova.kfg_sales_appv903.Activity.DatabaseHelper;
import com.sysnova.kfg_sales_appv903.Activity.LoginActivity;

import java.util.List;

/*
 Created by Shanzida
 */
public class OrderLineAdapater extends RecyclerView.Adapter<OrderLineAdapater.MyViewHolder> {

    private List<OrderLineModel> medium;
    private Context context;
    DatabaseHelper databaseHelper;

    public OrderLineAdapater(List<OrderLineModel> medium, Context context) {
        this.medium = medium;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_order_line_adapater, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        OrderLineModel model = medium.get(position);
        //holder.code.setText(model.getCode());
        holder.name.setText(" "+model.getName());
        holder.qty.setText(String.valueOf(model.getQty()));
        holder.total.setText(String.valueOf(model.getTotal()));
        holder.serial.setText(String.valueOf(position+1));
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOneProductOrderLine(model.getId());
                int newPosition = holder.getAbsoluteAdapterPosition();
                medium.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, medium.size());
                double grandTotal=getTotal(medium);
                int totalQty=getTotaQty(medium);


                Intent intent = new Intent("custom-message");
                intent.putExtra("TOTAL",grandTotal);
                intent.putExtra("TOTALQTY",totalQty);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int gottenQTY =Integer.parseInt(holder.qty.getText().toString()) ;
                Double gottenQTY  =Double.parseDouble(holder.qty.getText().toString());
                double gottenTotal =Double.parseDouble(holder.total.getText().toString()) ;
                gottenQTY++;
                gottenTotal=gottenQTY; //(Double)(model.getPrice()*gottenQTY);

                try{
                    if(databaseHelper.updateProductOrderLine(model.getId(),gottenQTY,gottenTotal)) {
                        notifyDataSetChanged();
                        holder.qty.setText(String.valueOf(gottenQTY));
                        holder.total.setText(String.valueOf(gottenQTY));
                        Intent intent = new Intent("custom-message");
                        loadArrayList();
                        intent.putExtra("TOTAL",getTotal(medium));
                        intent.putExtra("TOTALQTY",getTotaQty(medium));

                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                     //   toast("Clicked"+getTotal(medium)+" "+getTotaQty(medium));
                    }
                    else {
                        toast("Could not update ");
                    }
                }catch (Exception e) {
                    toast(e.getMessage());
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // int gottenQTY =Integer.parseInt(holder.qty.getText().toString()) ;
                Double gottenQTY  =Double.parseDouble(holder.qty.getText().toString());
                double gottenTotal =Double.parseDouble(holder.total.getText().toString()) ;

                if(gottenQTY==1) {
                    toast("Minimum 1 unit must be ordered");
                }
                else {
                    gottenQTY--;
                    gottenTotal=gottenQTY; //(Double)(model.getPrice()*gottenQTY);
                    if(databaseHelper.updateProductOrderLine(model.getId(),gottenQTY,gottenTotal)) {
                        holder.qty.setText(String.valueOf(gottenQTY));
                        holder.total.setText(String.valueOf(gottenQTY)); //gottenTotal));
                        Intent intent = new Intent("custom-message");
                        loadArrayList();
                        intent.putExtra("TOTAL",getTotal(medium));
                        intent.putExtra("TOTALQTY",getTotaQty(medium));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                    else{
                        toast("Could not update !");
                    }
                }
            }
        });
        //2022-09-25
        holder.qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int gottenQTY = 0;
                Boolean isNumber;

                try{
                    //BigDecimal n = new BigDecimal(holder.qty.getText().toString());
                    int n = Integer.parseInt(holder.qty.getText().toString());
                    gottenQTY = Integer.parseInt(holder.qty.getText().toString());
                    isNumber = true;
                }
                catch (Exception e){
                    isNumber = false;
                    toast("Quantity Must be Numeric/Without Fraction!");
                    return;
                }
                //double gottenQTY = Double.parseDouble(holder.qty.getText().toString());
                double gottenTotal = Double.parseDouble(holder.total.getText().toString());

                if (gottenQTY == 1) {
                    toast("Minimum 1 unit must be ordered");
                    return;
                } else {
                    //gottenQTY--;

                    //gottenQTY = Double.parseDouble(holder.qty.getText().toString());
                    gottenTotal = gottenQTY; //(Double)(model.getPrice()*gottenQTY);
                    if (databaseHelper.updateProductOrderLine(model.getId(), gottenQTY, gottenTotal)) {
                        holder.qty.setText(String.valueOf(gottenQTY));
                        holder.total.setText(String.valueOf(gottenQTY)); //gottenTotal));
                        Intent intent = new Intent("custom-message");
                        loadArrayList();
                        intent.putExtra("TOTAL", getTotal(medium));
                        intent.putExtra("TOTALQTY", getTotaQty(medium));
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } else {
                        toast("Could not update !");
                    }
                }
            }
        });


    }
    public double getTotal(List<OrderLineModel> list){
        double total=0;
        for(int i=0;i<list.size();i++){
            total=total+list.get(i).getTotal();
        }
        return total;
    }

    public void loadArrayList(){
        medium.clear();
        Cursor data = databaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
        if(data.moveToFirst()){
            do{
                int id= data.getInt(0);
                String code=data.getString(1);
                String name=data.getString(2);
                int qty=data.getInt(3);
                double price=data.getDouble(4);
                double total=data.getDouble(5);
                OrderLineModel orderListModal = new OrderLineModel(id,code,name,qty,price,total);
                medium.add(orderListModal);
            }while(data.moveToNext());
        }
    }
    public int getTotaQty(List<OrderLineModel> list){
        int total=0;
        for(int i=0;i<list.size();i++){
            total=total+list.get(i).getQty();
        }
        return total;
    }
    void toast(String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return medium.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView code,name,total,serial; //qty
        EditText qty;
        ImageView cross,add,minus;

        public MyViewHolder(View itemView) {

            super(itemView);
           // code = itemView.findViewById(R.id.codeTV);
            name = itemView.findViewById(R.id.nameTV);
            qty = itemView.findViewById(R.id.qtyTV);

            total = itemView.findViewById(R.id.totalTV);
            serial = itemView.findViewById(R.id.serial);
            cross=(ImageView)itemView.findViewById(R.id.crossTV);
            add=(ImageView)itemView.findViewById(R.id.addTV);
            minus=(ImageView)itemView.findViewById(R.id.minusTV);
        }
    }

}