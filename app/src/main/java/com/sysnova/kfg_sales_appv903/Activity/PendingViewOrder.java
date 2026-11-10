package com.sysnova.kfg_sales_appv903.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.adapter.ShowPendingOrderWise;
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
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.*;
import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

/*
 Created by Shanzida
 */
public class PendingViewOrder extends AppCompatActivity {
    Button showdate;
    String cdate,cdate1,datetext,dateOrdered="";
    String countordernoall="0";
    DatePickerDialog dpd;
    Calendar c;
    Context mContext;
    RecyclerView orderListRecyclerView;
    DatabaseHelper mDatabaseHelper;
    TextView emptyListText;
    ArrayList<PendingViewOrderListModel> orderListModalArrayList = new ArrayList<PendingViewOrderListModel>();
    //ArrayList<ViewOrderListModel> ViewOrderListModel = new ArrayList<>();
    //public static String selectedOrgId="",selectedWHId="",selectedOrgName="",selectedWHName="";
    static String docNo;
    private DatePickerDialog.OnDateSetListener dateSetListener1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_view_order);
        /**designing code**/
        getSupportActionBar().setTitle("Pending Sales Order");
        //  getSupportActionBar().setSubtitle("Brac Fisheries App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        /**designing code**/

    //    showdate = (Button) findViewById(R.id.date);
        orderListRecyclerView = findViewById(R.id.showorderinlist);
        emptyListText = findViewById(R.id.no_list_id);

        mDatabaseHelper = new DatabaseHelper(this);
        AsyncViewer task = new AsyncViewer();
        task.execute();
    }

    private void Showallorder() {
        AsyncViewer task = new AsyncViewer();
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

            pd = new ProgressDialog(PendingViewOrder.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Order is Fetching ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Insert Successfully into View Order Table")) {
                initRecyclerView();
               // loadOrderList();
                //Toast.makeText(ViewOrder.this, "Orders are fetched Succefully", Toast.LENGTH_LONG).show();
            } else {
                initRecyclerView();//null 2022-07-20
                Toast.makeText(PendingViewOrder.this, result, Toast.LENGTH_LONG).show();
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

                //sb.append(line);
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return a;
    }

    private String DBSYNCVIEW() {

        String res = "";
        res=fetchAllOrderDateWise();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    private String  fetchAllOrderDateWise() {
        String catagoryname, res = "";
        int id;
        String doc_no ="",dateOrder ="", grandtotal ="",user_id, bpname ="",docstatus="";
        boolean result = false;
        if(!orderListModalArrayList.isEmpty())
        {
            orderListModalArrayList.clear();
        }
        try {
            //2022-10-10
            Cursor dataorder = mDatabaseHelper.getSalesOrderUser(LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
            while (dataorder.moveToNext()) {
                String ids = dataorder.getString(1); //doc_no
                String ad_org_id = dataorder.getString(2);
                String m_warehouse_id = dataorder.getString(3);
                String c_bpartner_id = String.valueOf(dataorder.getString(4));//chemistid
                String ad_user_id1 = dataorder.getString(5);//userid
                String salestype = String.valueOf(dataorder.getString(6));
                grandtotal = String.valueOf(dataorder.getString(7));
                dateOrder = dataorder.getString(8);
                String documentno = dataorder.getString(9);
                String C_Region_ID = String.valueOf(dataorder.getString(10));
                String C_City_ID = String.valueOf(dataorder.getString(11));
                String description = String.valueOf(dataorder.getString(12));
                String C_Activity_ID = String.valueOf(dataorder.getString(13));
                String producttype = String.valueOf(dataorder.getString(14));
                bpname = String.valueOf(dataorder.getString(15));
                String PriceListType = String.valueOf(dataorder.getString(16));
                String truckNo = String.valueOf(dataorder.getString(17));
                String latitude = String.valueOf(dataorder.getString(18));
                String longitude = String.valueOf(dataorder.getString(19));
                String Geo_Location = String.valueOf(dataorder.getString(20));
                Integer orderid = dataorder.getInt(0);
                PendingViewOrderListModel orderListModal = new PendingViewOrderListModel(documentno,dateOrder,grandtotal,bpname,String.valueOf(orderid));
                orderListModalArrayList.add(orderListModal);
            }
            //2022-10-10
            if (result) {
                res = "Insert Successfully into View Order Table";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        ShowPendingOrderWise adapter = new ShowPendingOrderWise(orderListModalArrayList, PendingViewOrder.this);
        orderListRecyclerView.setAdapter(adapter);
    }
    public void callCancel(View view) {
        Intent i=new Intent(PendingViewOrder.this,SelectionAcitvity.class);
        startActivity(i);
    }
    public void callCancelNew(View view) {
        Intent i=new Intent(PendingViewOrder.this,SelectionAcitvity.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(PendingViewOrder.this, SelectionAcitvity.class);
        startActivity(i);
    }
    public void callDeletePendingOrder(View view) {
        mDatabaseHelper.deleteSalesOrder();
        mDatabaseHelper.deleteSalesOrderLine();
        Intent i=new Intent(PendingViewOrder.this, SelectionAcitvity.class);
        startActivity(i);

    }
    public void OrderSendToERP(View view) {
        //boolean vpn=checkVPN();
        boolean vpn = internetIsConnected();
        if (!vpn) {
            ShowAlert("ERROR", "Please Check Internet/VPN Connection");
            return;
        } else {
            savealert();
            //AsyncSendToServerOrder task = new AsyncSendToServerOrder();
            //task.execute();
        }
    }
    public void savealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Order Send to ERP");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //2022-10-14
                        Cursor dataorderno = mDatabaseHelper.getSalesOrderNoUser(ad_user_id);
                        if (dataorderno.moveToNext()) {
                            countordernoall = dataorderno.getString(0);
                        }
                        //2022-10-14
                        AsyncSendToServerOrder task = new AsyncSendToServerOrder();
                        task.execute();
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
        //alert11.show();
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }
    public boolean internetIsConnected() {
        try {
            //String command = "ping -w 1 -c 1 172.16.253.22";
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private final class AsyncSendToServerOrder extends AsyncTask<String,String,String> {
        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "",ss="";
            boolean active = false;
            try {
                ArrayList salesHeader = new ArrayList();
                Cursor dataorder = mDatabaseHelper.getSalesOrderUser(LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
                while (dataorder.moveToNext()) {
                    String ids = dataorder.getString(1);
                    String ad_org_id = dataorder.getString(2);
                    String m_warehouse_id = dataorder.getString(3);
                    String c_bpartner_id = String.valueOf(dataorder.getString(4));//chemistid
                    String ad_user_id1 = dataorder.getString(5);//userid
                    String salestype = String.valueOf(dataorder.getString(6));
                    String grandtotal = String.valueOf(dataorder.getString(7));
                    String dateordered = dataorder.getString(8);
                    String documentno = dataorder.getString(9);
                    String C_Region_ID = String.valueOf(dataorder.getString(10));
                    String C_City_ID = String.valueOf(dataorder.getString(11));
                    String description = String.valueOf(dataorder.getString(12));
                    String C_Activity_ID = String.valueOf(dataorder.getString(13));
                    String producttype = String.valueOf(dataorder.getString(14));
                    String bpname = String.valueOf(dataorder.getString(15));
                    String PriceListType = String.valueOf(dataorder.getString(16));
                    String truckNo = String.valueOf(dataorder.getString(17));
                    String latitude = String.valueOf(dataorder.getString(18));
                    String longitude = String.valueOf(dataorder.getString(19));
                    String Geo_Location = String.valueOf(dataorder.getString(20));

                    Integer orderid = dataorder.getInt(0);
                    Vector headerLines = new Vector();
                    headerLines.add(ad_org_id);
                    headerLines.add(m_warehouse_id);
                    headerLines.add(documentno);
                    //headerLines.add(LoginActivity.ad_user_id);
                    //2022-10-25
                    if(LoginActivity.ad_user_id.isEmpty()) {
                        headerLines.add(ad_user_id1);
                    }
                    else {
                        headerLines.add(LoginActivity.ad_user_id);
                    }//2022-10-25
                    headerLines.add(salestype);
                    headerLines.add(c_bpartner_id);
                    headerLines.add(grandtotal);
                    headerLines.add(description);
                    headerLines.add(C_Activity_ID);
                    headerLines.add(dateordered);
                    headerLines.add(truckNo);
                    headerLines.add(producttype);
                    headerLines.add(C_Region_ID);
                    headerLines.add(C_City_ID);
                    headerLines.add(bpname);
                    headerLines.add(PriceListType);
                    headerLines.add(latitude);
                    headerLines.add(longitude);
                    headerLines.add(Geo_Location);
                    headerLines.add(LoginActivity.employeeid+"|"+LoginActivity.Version);  //2022-10-23
                    headerLines.add(LoginActivity.userbp_id); //2022-10-26.

                    salesHeader.add(headerLines);

                    String headerString = "";
                    for (Object s : salesHeader) {
                        if (headerString.isEmpty()) {
                            headerString = headerString + s;
                        } else {
                            headerString = headerString + "_" + s;

                        }
                    }
                    Cursor dataorderLine = mDatabaseHelper.getSalesOrderLineDataNew(orderid, documentno); //getSalesOrderLine(orderid);

                    ArrayList salesOrder = new ArrayList();
                    while (dataorderLine.moveToNext()) {
                        String docNo = dataorderLine.getString(1);
                        String ad_org_id1 = dataorderLine.getString(2);
                        String product_name = String.valueOf(dataorderLine.getString(3));
                        String productID = String.valueOf(dataorderLine.getString(4));
                        String qtyordered = String.valueOf(dataorderLine.getInt(5));
                        String uom = String.valueOf(dataorderLine.getString(6));
                        String unit_price = String.valueOf(dataorderLine.getDouble(7));
                        String orderid1 = dataorderLine.getString(8);

                        Vector orderLines = new Vector();
                        orderLines.add(ad_org_id1);
                        orderLines.add(product_name);
                        orderLines.add(productID);
                        orderLines.add(String.valueOf(qtyordered));
                        orderLines.add("KG");
                        orderLines.add(String.valueOf(unit_price));
                        salesOrder.add(orderLines);

                    }
                    String lineString = "";
                    for (Object s : salesOrder) {
                        if (lineString.isEmpty()) {
                            lineString = lineString + s;
                        } else {
                            lineString = lineString + "_" + s;
                        }
                    }
                    HttpURLConnection con = (HttpURLConnection) (new URL("" + apiUrl + "/SalesOrder")).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("" + apiUrl + "/SalesOrder");

                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("type", "salesorder"));
                    list.add(new BasicNameValuePair("header", headerString));
                    list.add(new BasicNameValuePair("lines", lineString));
                    httpPost.setEntity(new UrlEncodedFormEntity(list));
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();
                    String s = readResponse(httpResponse);

                    if (s.isEmpty() || !s.equals("Success")) {
                        // result = s;
                        ss=s;
                        //return result;
                    } else if (s.equals("Success")) {
                        //2022-10-10
                        mDatabaseHelper.deleteSalesOrderDocno(documentno);
                        mDatabaseHelper.deleteSalesOrderLineDocno(documentno);
                        ss=s;
                    }

                }
                if (ss.isEmpty() || !ss.equals("Success")) {
                    result = ss;
                    return result;
                } else if (ss.equals("Success")) {
                    return "Success";
                }
            }
            catch(Exception e){
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute (String s){
            super.onPostExecute(s);
            try {
                if (s.equals("Success")) {
                    int allorder=0;
                    String countorderno="0";
                    //2022-10-01
                    Cursor dataorderno = mDatabaseHelper.getSalesOrderNoUser(LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
                    if (dataorderno.moveToNext()) {
                        countorderno = dataorderno.getString(0);
                        allorder=(Integer.parseInt(countordernoall))-(Integer.parseInt(countorderno));
                    }
                    if(countorderno.equals("1")) {
                        if (allorder == 1) {
                            ShowAlert1( allorder + " Order Insert Successfully. \n" + countorderno + " Order Insertion Failed.");
                        } else {
                            ShowAlert1( allorder + " Orders Insert Successfully. \n" + countorderno + " Order Insertion Failed.");
                        }
                    }
                    else if(countorderno.equals("0"))
                    {
                        if (allorder == 1) {
                            ShowAlert1(allorder + " Order Insert Successfully.");
                        }
                        else{
                            ShowAlert1( allorder + " Orders Insert Successfully.");
                        }

                    }
                    else{
                        ShowAlert1(countorderno+" Orders Insertion Failed.");
                    }
                    //2022-10-01
                    //ShowAlert1("Insert Successfully");
                    //cancelalert();

                }else if (s.equals("Insertion Failed")) {
                    ShowAlert1("Insertion Failed.");

                }
                else if (s.equals("Insertion Failed into Order Line")) {
                    ShowAlert1("Insertion Failed into Order Line.");
                }
                else {
                    ShowAlert1("No Data to Send. ");
                }
            } catch (NullPointerException ex) {

            }
        }

    }

    public void ShowAlert1(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        //builder1.setPositiveButton("OK", null);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i=new Intent(PendingViewOrder.this, SelectionAcitvity.class);
                startActivity(i);
                finish();
            }
        });
        AlertDialog alert11 = builder1.create();
        //this is done in an effort to make the dialog always appear on the topmost screen
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(PendingViewOrder.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setNegativeButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }



}