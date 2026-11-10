package com.sysnova.kfg_sales_appv903.Activity;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sysnova.kfg_sales_appv10.R;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ViewPaymentDetails extends AppCompatActivity {

    String i, trxdt, activity, org, dval, dname, daddress, dbankacc, kfgbank, kfgbranch, kfgbankacc, payamt, mrno,
            deponame, depophone, deposlip, documentno, order_id, erpdocno;
    TextView trxdatevw, erpdocvw, activityvw, orgvw, dvalview, dnamevw, daddressvw, dbankaccvw, kfgbankvw, kfgbranchvw,
            kfgbankaccvw, payamtvw, mrnovw, deponamevw, depophonevw, deposlipvw;
    byte[] image;
    Bitmap bitmap;
    Button goback;
    Uri uri;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payment_details);

        getSupportActionBar().setTitle("Payment In Details");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));

        trxdatevw = findViewById(R.id.trxdatetxtvw);
        erpdocvw = findViewById(R.id.erpdoctxtvw);
        activityvw = findViewById(R.id.activitytxtvw);
        orgvw = findViewById(R.id.orgtxtvw);
        dvalview = findViewById(R.id.dealervaltxtvw);
        dnamevw = findViewById(R.id.dealernametxtvw);
        daddressvw = findViewById(R.id.dealeraddtxtvw);
        dbankaccvw = findViewById(R.id.dealerbanktxtvw);
        kfgbankvw = findViewById(R.id.kfgbanktxtvw);
        kfgbranchvw = findViewById(R.id.kfgbranchtxtvw);
        kfgbankaccvw = findViewById(R.id.kfgbankactxtvw);
        payamtvw = findViewById(R.id.paymentamttxtvw);
        mrnovw = findViewById(R.id.mrnotxtvw);
        deponamevw = findViewById(R.id.deponametxtvw);
        depophonevw = findViewById(R.id.depophonetxtvw);
        deposlipvw = findViewById(R.id.deposliptxtvw);
        goback = findViewById(R.id.back_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            documentno = getIntent().getStringExtra("payment_id");
        }

        databaseHelper = new DatabaseHelper(this);

        AsyncFullPayment asyncLoadData = new AsyncFullPayment();
        asyncLoadData.execute();
    }

    private final class AsyncFullPayment extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... strings) {

            String result = "", error = "";
            try {
                result = DBSYNC();
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

            pd = new ProgressDialog(ViewPaymentDetails.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Loading....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Success")) {
                SetDataIntextView();
                //fetchImageFromServer();
            } else {
                toastMessage("Something Went Wrong!!!!");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(ViewPaymentDetails.this, ViewPayment.class);
        startActivity(intent);
    }
 */

    private String DBSYNC() {

        String res = "";
        res = FetchDataFromServer();
        try {
            return res;
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    public String FetchDataFromServer() {
        int catagoryID;
        boolean result = false;
        String res = "";
        HttpURLConnection con = null;

        try {

            con = (HttpURLConnection) (new URL("" + apiUrl + "/SyncViewPaymentInDetails")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("" + apiUrl + "/SyncViewPaymentInDetails");

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("erpdoc", documentno));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            String s = readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")) {
                res = "No Data Found";
                return res;
            }

            ArrayList list4 = new ArrayList();

            String[] parts = s.split("_");
            for (int i = 0; i < parts.length; i++) {
                Vector line = new Vector();
                String test = parts[i];
                String[] parts1 = parts[i].split(",", 26);
                for (int j = 0; j < parts1.length; j++) {

                    String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                    value = value.replaceAll("[\\[\\]]", "");
                    line.add(value);
                }
                list4.add(line);
            }

            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                order_id = ((String) line.get(0));
                erpdocno = (String) line.get(1);
                trxdt = ((String) line.get(2));
                activity = ((String) line.get(3));
                org = ((String) line.get(4));
                dval = ((String) line.get(5));
                dname = ((String) line.get(6));
                daddress = ((String) line.get(7));
                dbankacc = ((String) line.get(8));
                kfgbranch = ((String) line.get(9));
                kfgbankacc = ((String) line.get(10));
                payamt = ((String) line.get(11));
                mrno = ((String) line.get(12));
                deponame = ((String) line.get(13));
                depophone = ((String) line.get(14));
                deposlip = ((String) line.get(15));
                kfgbank = ((String) line.get(16));

                result = true;
            }
            if (result) {
                res = "Success";
            }
        } catch (Exception e) {
            res = e.getMessage();
        } finally {
            try {
                if (con != null)
                    con.disconnect();
                con = null;
            } catch (Exception e) {
            }
        }
        return res;
    }

    public String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "", a = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                a = line;
            }
            return_text = sb.toString();
        } catch (Exception e) {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }

    public void SetDataIntextView() {
        orgvw.setText(org);
        dvalview.setText(dval);
        dnamevw.setText(dname);
        daddressvw.setText(daddress);
        dbankaccvw.setText(dbankacc);
        kfgbankvw.setText(kfgbank);
        kfgbranchvw.setText(kfgbranch);
        kfgbankaccvw.setText(kfgbankacc);
        payamtvw.setText(payamt);
        mrnovw.setText(mrno);
        deponamevw.setText(deponame);
        depophonevw.setText(depophone);
        deposlipvw.setText(deposlip);
        trxdatevw.setText(trxdt);
        activityvw.setText(activity);
        if (erpdocno.equals("null" )) {
            erpdocvw.setText("");
        }else {
            erpdocvw.setText(erpdocno);
        }
    }

    public void fetchImageFromServer() {
        AsyncImage asyncSendToServer = new AsyncImage();
        asyncSendToServer.execute();

    }

    private final class AsyncImage extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            boolean isactive = true;
            try {
                res = getData();
                System.out.println(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewPaymentDetails.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Image is fetching ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s.equals("Success")) {
                    pd.dismiss();
                    toastMessage("Image fetched Successfully!!!");
                } else {
                    pd.dismiss();
                    System.out.println(s);
                    toastMessage("Image fetching failed");
                }
            } catch (NullPointerException ex) {
            }
        }
    }

    private String getData() {
        Connection conn = null;
        boolean isCommit = false;
        String result = "";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://172.16.253.22:5433/golocdb_14sep2022?useUnicode=yes&characterEncoding=UTF-8", "adempiere", "adempiere");

            if (conn == null) {
                return result;
            }
            conn.setAutoCommit(false);

            String new_sql = "select tap.binarydata image, tap.t_app_payment_id from public.t_app_payment tap where tap.t_app_payment_id = '" + documentno + "'";

            PreparedStatement statement1;

            statement1 = conn.prepareStatement(new_sql);

            ResultSet rs = statement1.executeQuery();
            while (rs.next()){
                i = rs.getString("image");
            }
            image = i.getBytes();//Base64.decode(i, Base64.DEFAULT);
       /*     String s = new String(image, "UTF-8");
            uri = Uri.parse(s);
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);*/
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            if(bitmap != null) {
                isCommit = true;
            }else {
                isCommit = false;
            }


            statement1.close();

            if (isCommit) {
                conn.commit();
                result = "Success";
            } else {
                result = "Fetching failed";
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void goback(View view) {
        Intent intent = new Intent(ViewPaymentDetails.this, ViewPaymentNew.class);
        intent.putExtra("datetext",trxdt);
        startActivity(intent);
    }
}