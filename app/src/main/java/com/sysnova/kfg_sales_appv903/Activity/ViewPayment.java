package com.sysnova.kfg_sales_appv903.Activity;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.adapter.ShowPayment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
/*
 Created by Anika
 */
public class ViewPayment extends AppCompatActivity {
    RecyclerView Paymentrecyclerview;
    ArrayList<ViewPaymentListModel> paymentListModalArrayList = new ArrayList<>();
    Button showdate;
    String pdate, datestyle;
    private DatePickerDialog.OnDateSetListener dateSetListener1;

    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payment);
        /**designing code**/
        getSupportActionBar().setTitle("View Payment");
        //  getSupportActionBar().setSubtitle("Brac Fisheries App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        /**designing code**/

        Paymentrecyclerview = findViewById(R.id.showpaymentinlist);
        mDatabaseHelper = new DatabaseHelper(this);
        //anika -(19-058-2022)
        showdate = findViewById(R.id.dateforpayment);

        pdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        datestyle = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        showdate.setText(" "+pdate+" ");

        showdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ViewPayment.this,
                        dateSetListener1,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String sdate = dayOfMonth+"/"+month+"/"+year;
                Date date = new Date(year-1900, month-1,dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd");
                pdate = formatter.format(date);
                datestyle = formt.format((date));
                showdate.setText(pdate);

                AsyncViewer task = new AsyncViewer(); //anika
                task.execute();
                //loadPaymentList();
            }
        };

        //anika -(19-058-2022)
        AsyncViewer task = new AsyncViewer(); //anika
        task.execute();
    }

    private final class AsyncViewer extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String result = "", error = "";
            try {
                result = DBSYNCVIEW();
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewPayment.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Payments are Fetching ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Insert Successfully into View Payment Table")) {
                loadPaymentList();
                Toast.makeText(ViewPayment.this, "Payments are fetched Successfully ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ViewPayment.this, result, Toast.LENGTH_LONG).show();
                loadPaymentList();
            }
        }
    }

    private String DBSYNCVIEW() {

        String res = "";
        try {
            mDatabaseHelper.deleteViewPayment();
            //toastMessage("Delete Products Successfull");
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(),""+ e.getMessage(),Toast.LENGTH_LONG).show();
            res = e.getMessage();
            return res;
        }

        res=fetchAllPayment();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    private void loadPaymentList(){
        if(!paymentListModalArrayList.isEmpty()) {
            paymentListModalArrayList.clear();
        }
        Cursor data = mDatabaseHelper.getPaymentFromViewPayment(LoginActivity.ad_user_id);

        if(data.moveToFirst()){
            do{
                String docno = data.getString(1);//for date
                String dealer = data.getString(2);
                String status = data.getString(3);
                String kfgbank = data.getString(4);
                String paymentamt = data.getString(5);
                String paymentdate = data.getString(6);
                String t_app_payment_id = data.getString(7);
                System.out.println(docno+"====================="+dealer+"====================="+status+"====================="+kfgbank+"====================="+paymentamt);
                ViewPaymentListModel paymentListModel = new ViewPaymentListModel(docno,dealer,status,kfgbank, paymentamt, t_app_payment_id, paymentdate);
                paymentListModalArrayList.add(paymentListModel);
            }while(data.moveToNext());
        }
        System.out.println(paymentListModalArrayList);
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        Paymentrecyclerview.setLayoutManager(linearLayoutManager);
        ShowPayment adapter = new ShowPayment(paymentListModalArrayList,ViewPayment.this);
        Paymentrecyclerview.setAdapter(adapter);
    }

    private String fetchAllPayment() {
        String catagoryname, res = "";
        int id;
        boolean result = false;
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SyncOrderDetails3")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/SyncOrderDetails3");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "SyncOrderDetails3"));
            System.out.println("datestyle ==============="+datestyle);
            list.add(new BasicNameValuePair("pdate", datestyle));
            list.add(new BasicNameValuePair("ad_user_id", LoginActivity.ad_user_id));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);


            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            System.out.println(s+"----------------");
            if (s.isEmpty() || !s.startsWith("[")){
                res="There are no Payment";
                return res;
            }


            ArrayList list4=new ArrayList();

            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }


            String m_warehouse_id="";
            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                String doc_no = (String) line.get(0);
                String dealer = (String) line.get(1);
                String paymentstatus = (String) line.get(2);
                String kfgbank = (String) line.get(3);
                String paymentamt = (String) line.get(4);
                String paymentdate = (String) line.get(5);
                String t_app_payment_id = (String) line.get(6);
                System.out.println("doc_no= "+doc_no+"\ndealer = "+dealer+"\ndealerbank = "+paymentstatus+"\nkfgbank = "+kfgbank);
                result = mDatabaseHelper.insertIntoViewPayment(doc_no, dealer, paymentstatus, kfgbank,paymentamt,paymentdate, t_app_payment_id, LoginActivity.ad_user_id);
            }

            if (result) {
                res = "Insert Successfully into View Payment Table";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
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

                //sb.append(line);
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return a;
    }
    public void callCancelNew(View view) {
        Intent i=new Intent(ViewPayment.this,SelectionAcitvity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(ViewPayment.this, SelectionAcitvity.class);
        startActivity(intent);
    }
}