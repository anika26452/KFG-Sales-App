package com.sysnova.kfg_sales_appv903.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sysnova.kfg_sales_appv10.R;
//import com.sysnova.kfg_sales_app.ViewPaymentActivity;

import android.os.AsyncTask;

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
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

//import android.net.VpnService;

import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.ad_user_id;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.userName;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.isEmployee;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.userbp_id;
import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;
//import static com.sysnova.kfg_sales_app.Activity.OrgBranchSelection.selectedOrgId;

/*
 Created by Shanzida 1525424
 */
public class SelectionAcitvity extends AppCompatActivity {
    Button createorder, vieworder, createpayment, viewpayment,OrderSendToERP;
    DatabaseHelper mDatabaseHelper;
    String username_Code="",countorderno="0";
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_acitvity);
        /**designing code**/
        getSupportActionBar().setTitle("Selection Activity");
        //  getSupportActionBar().setSubtitle("Brac Fisheries App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        /**designing code**/
        //2022-09-25
        username=findViewById(R.id.username);
        username.setText("Login User: "+userName);
        //2022-09-25

        createorder = (Button)findViewById(R.id.createorder);
        vieworder = (Button)findViewById(R.id.vieworder);
        createpayment = (Button)findViewById(R.id.createpayment);
        viewpayment = (Button)findViewById(R.id.viewpayment);
        OrderSendToERP=(Button)findViewById(R.id.OrderSendToERP);

        mDatabaseHelper = new DatabaseHelper(this);
        String TrxType="Edit";
        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
        Cursor dataorderno = mDatabaseHelper.getSalesOrderNoUser(ad_user_id); //getSalesOrderLineDataTemp(docNo);
        if (dataorderno.moveToNext()) {
            countorderno = dataorderno.getString(0);
        }
        if(countorderno.equals("1"))
        {
            OrderSendToERP.setText("Send To ERP,"+countorderno+" Order Pending");
            OrderSendToERP.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else if(countorderno.equals("0"))
        {
            OrderSendToERP.setText("Send To ERP,"+countorderno+" Order Pending");

        }
        else{
            OrderSendToERP.setText("Send To ERP,"+countorderno+" Orders Pending");
            OrderSendToERP.setBackgroundColor(Color.parseColor("#FFA500"));
        }

    }

    public void createpaymentactivity(View view){
        Cursor cursor1 = mDatabaseHelper.getDB_Sync(ad_user_id);
        if (cursor1.moveToFirst()) {
            Intent intent = new Intent(SelectionAcitvity.this, CreatePaymentActivityDirectSending.class);
            startActivity(intent);
        }
        else{
            ShowAlert("Warning", "DB Sync is Required.");
        }
    }

    public void viewpaymentactivity(View view){
        boolean net=internetIsConnected();
        if (!net) {
            ShowAlert("Warning","Please Check Internet/VPN Connection.");
        }
        else {
            Intent intent = new Intent(SelectionAcitvity.this, ViewPayment.class);
            startActivity(intent);
        }
    }

    public void DBsyncView(View view) {
        boolean net=internetIsConnected();
        if (!net) {
            ShowAlert("Warning","Please Check Internet/VPN Connection.");
        }
        else {
            Intent intent = new Intent(SelectionAcitvity.this, ViewOrder.class);
            startActivity(intent);
        }
    }
    public void OrderActivity(View view) {
        Cursor cursor1 = mDatabaseHelper.getDB_Sync(ad_user_id);
        if (cursor1.moveToFirst()) {
            Intent intent = new Intent(SelectionAcitvity.this, OrgBranchSelection.class);
            startActivity(intent);
        }
        else{
            ShowAlert("Warning", "DB Sync is Required.");
        }
    }
    public boolean internetIsConnected() {
        try {
            //String command = "ping -w 1 -c 1 172.16.253.22";
       //     String command = "ping -w 1 -c 1 172.16.253.10";
            String command = "ping -w 1 -c 1 webapps.kflapps.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void DBsyncActivity(View view) {
        boolean net=internetIsConnected();
        if (!net) {
            ShowAlert("Warning","Please Check Internet/VPN Connection.");
        }
        else {
            AsyncSender task = new AsyncSender();
            task.execute();
        }
        //AsyncSender task = new AsyncSender();
        //task.execute();

    }
    //OrderSendToERP 2022-06-21
    public boolean checkVPN(){
        List<String> networkList = new ArrayList<>();
        //ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {
            //Timber.d("isVpnUsing Network List didn't received");
        }

        return networkList.contains("tun0");
    }
    public void OrderSendToERP(View view) {
        /*boolean vpn=internetIsConnected();
        if (!vpn){
            ShowAlert("ERROR","Please Check Internet/VPN Connection");
            return;
        }else{
            AsyncSendToServerOrder task = new AsyncSendToServerOrder();
            task.execute();
        }*/
        //2022-10-11
        Intent intent = new Intent(SelectionAcitvity.this, PendingViewOrder.class);
        startActivity(intent);
    }

    private final class AsyncSendToServerOrder extends AsyncTask<String,String,String> {
        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "",ss="",documentno1="";
            boolean active = false;
            try {
                ArrayList salesHeader = new ArrayList();
                Cursor dataorder = mDatabaseHelper.getSalesOrderUser(ad_user_id); //getSalesOrderLineDataTemp(docNo);
                //if (dataorder.isNull(0)) {
                    /*if(dataorder.moveToNext()) {

                    } else {
                        return "noOrderLine";
                    }*/

               // (orderid INTEGER PRIMARY KEY AUTOINCREMENT,ids INTEGER," +
                //"ad_org_id TEXT,m_warehouse_id TEXT,chemistid TEXT,userid TEXT,salestype Text," +
                   //     "grandtotal TEXT,dateordered TEXT,documentno TEXT,C_Region_ID TEXT,C_City_ID Text," +
                    //    "description Text,C_Activity_ID Text ,producttype Text,bpname Text,PriceListType Text,truckno Text)";
                    while (dataorder.moveToNext()) {
                        String ids = dataorder.getString(1);
                        String ad_org_id = dataorder.getString(2);
                        String m_warehouse_id = dataorder.getString(3);
                        String c_bpartner_id = String.valueOf(dataorder.getString(4));//chemistid
                        String ad_user_id = dataorder.getString(5);//userid
                        String salestype = String.valueOf(dataorder.getString(6));
                        String grandtotal = String.valueOf(dataorder.getString(7));
                        String dateordered = dataorder.getString(8);
                        String documentno = dataorder.getString(9);
                        documentno1=documentno;
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
                        /*boolean nn=FetchCheckToDataFromServer(documentno);
                        if(nn==true){
                            mDatabaseHelper.deleteSalesOrderDocno(documentno);
                            mDatabaseHelper.deleteSalesOrderLineDocno(documentno);
                            continue;
                        }
                        else{*/
                            Vector headerLines = new Vector();
                            headerLines.add(ad_org_id);
                            headerLines.add(m_warehouse_id);

                            headerLines.add(documentno);
                            headerLines.add(ad_user_id);
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
                                ss=s;
                            }

                        }
                    //}
                if (ss.isEmpty() || !ss.equals("Success")) {
                    if(ss.equals("Already Order Place")) {
                        mDatabaseHelper.deleteSalesOrderDocno(documentno1);
                        mDatabaseHelper.deleteSalesOrderLineDocno(documentno1);
                    }
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
                     mDatabaseHelper.deleteSalesOrder();
                     mDatabaseHelper.deleteSalesOrderLine();
                    //2022-10-01
                    Cursor dataorderno = mDatabaseHelper.getSalesOrderNoUser(ad_user_id); //getSalesOrderLineDataTemp(docNo);
                    if (dataorderno.moveToNext()) {
                        countorderno = dataorderno.getString(0);
                    }
                    if(countorderno.equals("1") || countorderno.equals("0"))
                    {
                        OrderSendToERP.setText("Send To ERP,"+countorderno+" Order Pending");
                        OrderSendToERP.setBackgroundColor(Color.parseColor("#0b79bf"));
                    }
                    else{
                        OrderSendToERP.setText("Send To ERP,"+countorderno+" Orders Pending");
                        OrderSendToERP.setBackgroundColor(Color.parseColor("#FFA500"));
                    }
                    //2022-10-01
                    ShowAlert1("Insert Successfully");
                    //cancelalert();

                }else if (s.equals("Insertion Failed")) {
                    ShowAlert1("Insertion Failed");
                }
                else if (s.equals("Insertion Failed into Order Line")) {
                    ShowAlert1("Insertion Failed into Order Line");
                }
                else if(s.equals("Already Order Place")){
                    //mDatabaseHelper.deleteSalesOrderDocno(docNo);
                    //mDatabaseHelper.deleteSalesOrderLineDocno(docNo);
                    ShowAlert1("Already Order in ERP.");
                }
                else {
                    ShowAlert1("No Data to Send ");
                }
            } catch (NullPointerException ex) {

            }
        }

}
    //2022-09-26
    public boolean FetchCheckToDataFromServer(String documentno){
        String  res = "";

        boolean result = false;

        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "SyncOrderDetailsCheck"));
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
            list.add(new BasicNameValuePair("documentno", documentno));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                result=false;
            }
            else {
                result=true;
            }
        } catch (Exception e) {
            res = e.getMessage();
            toastMessage(res);
        }
        return result;
    }

    //2022-09-26

    public void ShowAlert1(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", null);
        AlertDialog alert11 = builder1.create();
        //this is done in an effort to make the dialog always appear on the topmost screen
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }



    //end 2022-06-21
    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(SelectionAcitvity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private final class AsyncSender extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
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

            pd = new ProgressDialog(SelectionAcitvity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("DBSync On Progress ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Successful")) {
                //2022-07-20 added by shanzida
                boolean r = false;
                String Sync="yes";
                r = mDatabaseHelper.insertIntoDB_Sync(Sync,ad_user_id);
                if (r) {
                    toastMessage("DBSYNC Insert Successful");
                }
                //2022-07-20
            } else {
                toastMessage(result);
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
    private String DBSYNC() {

        String res = "";
        try {
            mDatabaseHelper.deleteProductCatagory();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteOrganization();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteWarehouse();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteSalesType();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteCustomer();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteDistrict();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteThana();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteProducts();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteProductType();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteDB_Sync();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }

        try {
            mDatabaseHelper.deletekfgBankName();
        }catch (Exception e){
            res = e.getMessage();
            return res;
        }

        res = FetchbanknameFromServer();
        res = FetchAllDealer();
        res = FetchAllOrganization();
        res = FetchDistrictFromServer();
        res = FetchThanaFromServer();
        res = FetchProductTypeFromServer();
        res = FetchSalesTypeFromServer();
        res = FetchAllWarehouse();
        res = FetchProductsFromServer2();
        res=FetchProductCatagoryFromServer();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;

    }
    //2022-09-13
    private String  FetchbanknameFromServer() {
        boolean result = false;

        String supText1="", productID, price, res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "kfgbankname"));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Bank Name Not Found";
                supText1="1";
                return res;

            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("]_");
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
            String supname = "", supid = "";
            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                supname = ((String) line.get(0));
                supid = (String) line.get(1);
                result =mDatabaseHelper.insertIntoBankName(supid,supname) ;
            }

            if (result) {
                res="Success";
            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
    //2022-09-13
    private String FetchAllOrganization() {

        String ad_org_id="",org_name="",res = "";

        boolean result = false;

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "allorg"));
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
            list.add(new BasicNameValuePair("isEmployee", isEmployee));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Org Not Found";
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

            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                org_name =((String) line.get(0));
                ad_org_id=(String) line.get(1);

                result =mDatabaseHelper.insertIntoOrganization(ad_org_id,org_name,ad_user_id) ;
            }

            if (result) {
                res = "Organization Sync Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
    private String  FetchProductCatagoryFromServer() {


        String catagoryname, res = "";

        int id;
        boolean result = false;
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "product_catagory"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Catagory Not Found";
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
            String ad_user_id="",m_warehouse_id="";
            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                int catagoryId=(Integer.parseInt((String) line.get(0)));
                String catagoryName=(String) line.get(1);
                String C_Activity_ID=(String) line.get(2);
                result = mDatabaseHelper.insertIntoProductCatagory(catagoryId, catagoryName,C_Activity_ID);
                //result = mDatabaseHelper.insertIntoProductCatagory(catagoryId, catagoryName);
            }
            if (result) {
                res = "Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private String  FetchDistrictFromServer() {
        boolean result = false;

        String supText1="", productID, price, res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "district"));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);

            if (s.isEmpty() || !s.startsWith("[")){
                res="District Not Found";
                supText1="1";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("]_");
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
            String supname = "", supid = "",wh_loc="",bank_loc="";
            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                supname = ((String) line.get(0));
                supid = (String) line.get(1);
                wh_loc = ((String) line.get(2));
                bank_loc = (String) line.get(3);

                result =mDatabaseHelper.insertIntoDistrict(supid,supname,wh_loc,bank_loc);
            }

            if (result) {
                res="Success";
            }

        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private String  FetchThanaFromServer() {
        boolean result = false;

        String supText1="", productID, price, res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "thana"));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Thana Not Found";
                supText1="1";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("]_");
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
            String supname = "", supid = "",Region_ID="",wh_loc="",bank_loc="";
            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                supname = ((String) line.get(0));
                supid = (String) line.get(1);
                Region_ID= (String) line.get(2);
                wh_loc = ((String) line.get(3));
                bank_loc = (String) line.get(4);
                result =mDatabaseHelper.insertIntoThana(supid,supname,Region_ID,wh_loc,bank_loc) ;
            }

            if (result) {
                res="Success";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
    private String FetchAllWarehouse() {

        String ad_org_id="",warehouse_id="",res = "",warehouse_name="",c_region_id="",c_city_id="";

        boolean result = false;

        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "allwarehouse"));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="WH Not Found";
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



            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                warehouse_name =((String) line.get(0));
                warehouse_id=(String) line.get(1);
                ad_org_id=(String) line.get(2);
                c_region_id=(String) line.get(3);
                c_city_id=(String) line.get(4);
                //hashBranchId.put(warehouse_name,warehouse_id);
                Log.d("DEBUG","WAREHOUSE: "+warehouse_name+" "+warehouse_id+" "+ad_org_id);
                result =mDatabaseHelper.insertIntoWarehouse(warehouse_id,ad_org_id,warehouse_name,c_region_id,c_city_id) ;

            }


            if (result) {
                res = "Warehouse Sync Successful";
            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();

        }
         return res;
    }

    private String FetchAllDealer() {

        String result = "", error = "";
        boolean r = false;

        try {

            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("name", "Customer"));
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));
            list.add(new BasicNameValuePair("c_bpartner_id", userbp_id));
            list.add(new BasicNameValuePair("isEmployee", isEmployee));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            String s = readResponse(httpResponse);
            Log.d("DEBUG", "S Response Customer " + s);
            int y = s.length();
            if (s.isEmpty() || !s.startsWith("[")) {
                result = s + " No Customer found";
                Log.d("DEBUG", "Empty Result " + result);
                return result;
            }
            ArrayList list4 = new ArrayList();

            String[] parts = s.split("_");
            for (int i = 0; i < parts.length; i++) {
                Vector line1 = new Vector();

                String[] parts1 = parts[i].split(",");
                for (int j = 0; j < parts1.length; j++) {
                    String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                    value = value.replaceAll("[\\[\\]]", "");
                    line1.add(value);

                }
                list4.add(line1);
            }

            for (int i1 = 0; i1 < list4.size(); i1++) {

                Vector line = (Vector) list4.get(i1);
                String customer_id = (String) line.get(0);
                String customer = (String) line.get(1);
                String code = (String) line.get(2);
                String balance = (String) line.get(3);
                String creditlimit = (String) line.get(4);
                String region_id = (String) line.get(5);
                String t_businesstype_id = (String) line.get(6);

                r = mDatabaseHelper.insertIntoOCustomerNew1(customer_id, customer, code, balance, creditlimit,region_id, t_businesstype_id);
            }
            if (r) {
                result = "Customer Sync Successful";
            }
            else {
                error = "Failed to insert into Customer Table";
                return error;
            }
        } catch (Exception e) {
            error = e.getMessage();
            return error;
        }
        return result;
    }
    private String FetchProductTypeFromServer() {

        String ad_org_id="",warehouse_id="",res = "",warehouse_name="";
        boolean result = false;

        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            ArrayList list13=new ArrayList();
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "ProductType"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Type Not Found";
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

            String supname = "", supid = "";

            for (int i1 = 0; i1 < list4.size(); i1++) {

                Vector line = (Vector) list4.get(i1);
                supid  = ((String) line.get(0));
                supname = (String) line.get(1);

                result = mDatabaseHelper.insertIntoProductType(supid,supname);
            }

            if (result) {
                res = "Product Type Sync Successful";
            }

        } catch (Exception e) {
            res = e.getMessage();

        }
        return res;
    }
    private String FetchSalesTypeFromServer() {


        String name,id, prodType, res = "";

        boolean result = false;
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "ActivityType"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="salesType Not Foundd";
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

            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                id=((String) line.get(0));
                name=((String) line.get(1));
                prodType=((String) line.get(2));
                String businessProductType=((String) line.get(3));  //added by anika on 2026-03-12
                String t_businessType_id=((String) line.get(4));    //added by anika on 2026-03-12
            //    result = mDatabaseHelper.insertIntoSalesTypeNew(name,id,prodType);  //prev
                result = mDatabaseHelper.insertIntoSalesTypeNew(name,id,prodType, businessProductType, t_businessType_id);
            }

            if (result) {
                res = "Successful";
            }
        }  catch (Exception e) {
            res = e.getMessage();

        }
        return res;

    }
    private String  FetchProductsFromServer2() {
        mDatabaseHelper.deleteProducts();
        int catagoryID;
        boolean result = false;

        String name, productID, price, res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "allproduct"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Not Foundd";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("]_");
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
            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                productID=((String) line.get(0));
                name=(String) line.get(1);
                catagoryID=Integer.parseInt((String) line.get(2));
                String unitprice=(String)line.get(3);
                String uom_id=(String)line.get(4);
                String searchkey=(String)line.get(5);

                System.out.println("Product id"+productID);

                Log.d("DEBUG","FROM SERVER || ProductName="+name+" PID="+productID+" p_catId="+catagoryID+"price="+unitprice+"UOM="+uom_id+"Search key="+searchkey);
                result = mDatabaseHelper.insertIntoProduct(productID, name, catagoryID, unitprice,uom_id,searchkey,"");

            }

            if (result) {
                res = "Successful";

            }

        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i=new Intent(SelectionAcitvity.this,LoginActivity.class);
        startActivity(i);
    }
    public void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
    public void callCancelLogOut(View view) {
        cancelalert();
        //Intent i=new Intent(ProductOrderLine.this,LoginActivity.class);
        //startActivity(i);
    }

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SelectionAcitvity.this);
        builder1.setMessage("Are you sure you want to LogOut?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i=new Intent(SelectionAcitvity.this,LoginActivity.class);
                        startActivity(i);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
//                        Intent intent = new Intent(CreatePaymentActivity.this, SelectionAcitvity.class);
//                        startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}