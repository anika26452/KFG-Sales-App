package com.sysnova.kfg_sales_appv903.adapter;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.kfg_sales_appv903.Activity.DatabaseHelper;
import com.sysnova.kfg_sales_appv903.Activity.FullViewPendingOrderActivity;
import com.sysnova.kfg_sales_appv903.Activity.PendingViewOrder;
import com.sysnova.kfg_sales_appv903.Activity.ViewOrderNew;
import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.modal.PendingViewOrderListModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 Created by Shanzida
 */
public class ShowPendingOrderWise extends RecyclerView.Adapter<ShowPendingOrderWise.ViewHolder> {
    ArrayList<PendingViewOrderListModel> orderListActivities;
    Context mContext;
    String Order_ID,docstatus,dateText,Order_no;
    View itemView1=null;

    DatabaseHelper mdatabaseHelper=new DatabaseHelper(mContext);
    private SQLiteDatabase db;

    public ShowPendingOrderWise(ArrayList<PendingViewOrderListModel> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_show_order_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Order_no=orderListActivities.get(position).getOrderno();
        String docNo=String.valueOf(orderListActivities.get(position).getDocstatus());
        holder.bpnameView.setText(orderListActivities.get(position).getBPname());
        holder.ordernoTextView.setText(orderListActivities.get(position).getDocstatus());
        //holder.dateTextView.setText(orderListActivities.get(position).getDate());
        holder.TotalPriceTextView.setText(String.valueOf(orderListActivities.get(position).getTotal()));
        holder.docstatus_cancel.setText(String.valueOf(orderListActivities.get(position).getDate()));
        holder.dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(mContext, FullViewPendingOrderActivity.class);
                    intent.putExtra("order_doc_id", orderListActivities.get(position).getOrderno());
                    //intent.putExtra("erp_doc",orderListActivities.get(position).getErpDoc());
                    mContext.startActivity(intent);
            }
        });
    }


    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Are you want to Delete this order?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                       // mDatabaseHelper.deleteSalesOrderDocno(Order_no);
                       // mDatabaseHelper.deleteSalesOrderLineDocno(Order_no);

                        Intent intent = new Intent(mContext, PendingViewOrder.class);
                        //intent.putExtra("dateText", dateText);
                        mContext.startActivity(intent);

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ordernoTextView,dateTextView,TotalPriceTextView,bpnameView,docstatus_cancel,docstatusView;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            bpnameView = itemView.findViewById(R.id.dealer); //2022-05-18 newly added by Shanzida
            ordernoTextView = itemView.findViewById(R.id.order_No);
            dateTextView  = itemView.findViewById(R.id.date);
            TotalPriceTextView = itemView.findViewById(R.id.total_price);
            //docstatusView = itemView.findViewById(R.id.docstatus);
            docstatus_cancel= itemView.findViewById(R.id.docstatus_cancel); //2022-05-19 newly added by Shanzida
            itemView1=itemView;

        }
    }

    //2022-05-19 Start newly added by Shanzida
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                ArrayList salesOrder=new ArrayList();

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "updateDocStatus"));
                list.add(new BasicNameValuePair("T_SalesOrder_ID", Order_ID));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else if(s.equals("Success")){
                    return "Success";
                }

            }
            catch (Exception e){
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(s.equals("Success")){
                    toastMessage("Order Updated Successfully");
                    Intent intent = new Intent(mContext, ViewOrderNew.class);
                    intent.putExtra("dateText", dateText);
                    mContext.startActivity(intent);
                    /*Intent intent = new Intent(ShoworderDateWise.this, ViewOrder.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();*/
                }else {
                   // confirmBtn.setEnabled(true);
                    toastMessage("Server Connection Failed Please Send Again");
                }
            }catch (NullPointerException ex){

            }
        }

        private void toastMessage(String s) {
        }
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {

                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }


    //2022-05-19 end
    public boolean internetIsConnected() {
        try {
            //String command = "ping -w 1 -c 1 172.16.253.22";
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
