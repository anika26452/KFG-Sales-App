package com.sysnova.kfg_sales_appv903.adapter;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.sysnova.kfg_sales_app.Activity.CreatePaymentActivityDirectSending;
import com.sysnova.kfg_sales_appv903.Activity.ViewPaymentDetails;
import com.sysnova.kfg_sales_appv903.Activity.ViewPaymentNew;
import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.modal.ViewPaymentListModel;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowPayment extends RecyclerView.Adapter<ShowPayment.ViewHolder> {
    List<ViewPaymentListModel> paymentListActivities;
    Context mContext;
    String t_app_payment_id, datetext;

    public ShowPayment(ArrayList<ViewPaymentListModel> paymentListActivities, Context mContext) {
        this.paymentListActivities = paymentListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_payment_list_temp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mrnno.setText(paymentListActivities.get(position).getOrderno());
        holder.dealertext.setText(paymentListActivities.get(position).getDealer());
        holder.kfgbanktext.setText(paymentListActivities.get(position).getKfgbank());
        holder.payment.setText(paymentListActivities.get(position).getPaymentamt());
        holder.status.setText(paymentListActivities.get(position).getstatus());
        if(paymentListActivities.get(position).getstatus().equals("Drafted")){
            holder.cancelbtn.setClickable(true);
            holder.cancelbtn.setEnabled(true);

            holder.viewbtn.setClickable(true);
            holder.viewbtn.setEnabled(true);

            holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    t_app_payment_id = paymentListActivities.get(position).getT_app_payment_id();
                    datetext = paymentListActivities.get(position).getPaymentdate();
                    cancelalert();
                }
            });

            holder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, ViewPaymentDetails.class);
                    i.putExtra("payment_id", paymentListActivities.get(position).getOrderno());
                    mContext.startActivity(i);
                }
            });
        } else{
            holder.cancelbtn.setTextColor(Color.parseColor("#FFFFFF"));
            holder.cancelbtn.setClickable(false);
            holder.cancelbtn.setEnabled(false);

            holder.viewbtn.setClickable(true);
            holder.viewbtn.setEnabled(true);
            holder.viewbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, ViewPaymentDetails.class);
                    i.putExtra("payment_id", paymentListActivities.get(position).getOrderno());
                    mContext.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return paymentListActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mrnno,dealertext, kfgbanktext, payment, status, cancelbtn, viewbtn;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.paymentListRow);
            mrnno = itemView.findViewById(R.id.mrn_no);
            dealertext  = itemView.findViewById(R.id.dealertext);
            kfgbanktext = itemView.findViewById(R.id.kfgbanktext);
            status = itemView.findViewById(R.id.pstatus);
            cancelbtn = itemView.findViewById(R.id.pcancel);
            payment = itemView.findViewById(R.id.paymentamt);
            viewbtn = itemView.findViewById(R.id.pview);
        }
    }

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Are you sure you want to cancel this payment?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //   Toast.makeText(mContext, "Yes", Toast.LENGTH_LONG).show();
                        AsyncSendToServer asyncSendToServer = new AsyncSendToServer();
                        asyncSendToServer.execute();
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

    private final class AsyncSendToServer extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/UpdateOperation")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/UpdateOperation");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "updatePayment"));
                list.add(new BasicNameValuePair("t_app_payment_id", t_app_payment_id));
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
                    // Toast.makeText(mContext, "Order Updated Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, ViewPaymentNew.class);
                    intent.putExtra("datetext",datetext);
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext, "Server Connection Failed Please Send Again", Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException ex){

            }
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
        }
        return a;
    }
    public boolean internetIsConnected() {
        try {
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