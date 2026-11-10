package com.sysnova.kfg_sales_appv903.Activity;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.kfg_sales_appv10.R;
import com.sysnova.kfg_sales_appv903.adapter.PendingProductsListRecyclerViewAdapter;
import com.sysnova.kfg_sales_appv903.modal.PendingProductDetailsModal;

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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/*
 Created by Shanzida
 */
public class FullViewPendingOrderActivity extends AppCompatActivity {

    String bpCode,bpName,phone,dateOrdered,warehouse,total_openbalance,bp_location,description,orgname;
    String  old_kg,documentNo,des,promised_date,s_name,s_phone,c_order_id,C_Doctype_ID;
    String wordsAmtBags; //uomsymbol,undelivered_amt,promisedAmt
    //Toolbar toolbar;
    Spinner ForwardToSpinner;
    TextView partyCodeTextView,partyNameTextView,addressTextView,mobileNoTextView,soNumberTextView,soIssueDateTextView,srNameTextView,srMobileTextView;
    TextView total_mtTextView,total_bagsTextView,total_moneyTextView,promisedDateTextView,qty_wordsTextView,orgnameTexView;
    TextView mtAmtTextView,deliveryFromTextView,desTextView,totalPriceTextView,truckno_text,prodactivity_name,activitytype_name;//,proAmtTextView,discout_editText1,partyBalanceTextView,undeliveredAmtTextView;
    EditText discout_EditText,total_editTextET;//discout_editText,msg_text
    //Button yes_button,no_button,edit_order_button;
    RecyclerView recyclerViewList;
    double totalMetricTon = 0,totalBagQty = 0,totalTakaAmt = 0;
    BigDecimal total_price1=new BigDecimal(0);
    ArrayList<PendingProductDetailsModal> productDetailsModals = new ArrayList<>();
    String msg = "",isApproved = "",documentNumber = "",erpDocumentNumber = "",odiscount,supText="",percentdis="",ispdis="",tmsg="";
    String selectedForwardToId="",selectedForwardToName="";
    //String[] choicesArray;
    //BigDecimal total_price;
    //static TextView totalPriceTextView,totalPriceInWordsTextView,SelectedOrgName;
    CheckBox discountCheckBox;
    DatabaseHelper dbHelper;
    List<String> ForwardToArrayList = new ArrayList<String>();
    HashMap<String,String> hashForwardToId  = new HashMap<>();
    boolean ispercentdiscount=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpending_vieworder);

        orgnameTexView = findViewById(R.id.org_approval);
        partyCodeTextView = findViewById(R.id.party_code_approval);
        partyNameTextView = findViewById(R.id.party_name_approval);
        addressTextView = findViewById(R.id.address_approval);
        mobileNoTextView = findViewById(R.id.mobile_approval);
        soNumberTextView = findViewById(R.id.so_number);
        soIssueDateTextView = findViewById(R.id.so_issue_date);
        srNameTextView      = findViewById(R.id.sr_name);
        srMobileTextView    = findViewById(R.id.sr_mobile);

        recyclerViewList = findViewById(R.id.order_product_list_recyclerview);
        total_bagsTextView    = findViewById(R.id.total_bags);
        total_moneyTextView   = findViewById(R.id.total_price_amt);
        //promisedDateTextView  = findViewById(R.id.promised_date);
        qty_wordsTextView      = findViewById(R.id.quantity_words_bags);
        mtAmtTextView          = findViewById(R.id.mt_amount);
        deliveryFromTextView   = findViewById(R.id.delivery_from);
        //partyBalanceTextView   = findViewById(R.id.party_balance_approval);
        desTextView            = findViewById(R.id.description_id);

        truckno_text = findViewById(R.id.truckno_text);
        prodactivity_name = findViewById(R.id.prodactivity_name);
        activitytype_name  = findViewById(R.id.activitytype_name);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            documentNumber   = getIntent().getStringExtra("order_doc_id");
        }
        getSupportActionBar().setTitle("Pending Order View ");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));

        dbHelper = new DatabaseHelper(this);
        //ViewOrderNew.class
        AsyncLoadData asyncLoadData = new AsyncLoadData();
        asyncLoadData.execute();


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
    public void OrderSendToERP(View view) {
        //boolean vpn=checkVPN();
        boolean vpn = internetIsConnected();
        if (!vpn) {
            ShowAlert("ERROR", "Please Check Internet/VPN Connection");
            return;
        } else {
            /*AsyncSendToServerOrder task = new AsyncSendToServerOrder();
            task.execute();*/
            savealert();
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
    public void deletealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to Delete this Order?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.deleteSalesOrderDocno(documentNumber);
                        dbHelper.deleteSalesOrderLineDocno(documentNumber);
                        Intent i=new Intent(FullViewPendingOrderActivity.this, PendingViewOrder.class);
                        startActivity(i);
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
    public void callDeletePendingOrder(View view) {
        deletealert();
        /*dbHelper.deleteSalesOrderDocno(documentNumber);
        dbHelper.deleteSalesOrderLineDocno(documentNumber);
        Intent i=new Intent(FullViewPendingOrderActivity.this, PendingViewOrder.class);
        startActivity(i);*/

    }

    private final class AsyncSendToServerOrder extends AsyncTask<String,String,String> {
        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "",ss="";
            boolean active = false;
            try {
                ArrayList salesHeader = new ArrayList();
                Cursor dataorder = dbHelper.getSalesOrderUserdocwise(LoginActivity.ad_user_id,documentNumber); //getSalesOrderLineDataTemp(docNo);
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
                    //2022-10-25
                    if(LoginActivity.ad_user_id.isEmpty()) {
                        headerLines.add(ad_user_id1);
                    }
                    else {
                        headerLines.add(LoginActivity.ad_user_id);
                    }//2022-10-25
                    //headerLines.add(LoginActivity.ad_user_id);
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
                    Cursor dataorderLine = dbHelper.getSalesOrderLineDataNew(orderid, documentno); //getSalesOrderLine(orderid);

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
                        dbHelper.deleteSalesOrderDocno(documentno);
                        dbHelper.deleteSalesOrderLineDocno(documentno);
                        ss=s;
                    }

                }
                //}
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
                    dbHelper.deleteSalesOrderDocno(documentNumber);
                    dbHelper.deleteSalesOrderLineDocno(documentNumber);
                    ShowAlert1("Insert Successfully");
                    //cancelalert();

                }else if (s.equals("Insertion Failed")) {
                    ShowAlert1("Insertion Failed");

                }
                else if (s.equals("Insertion Failed into Order Line")) {
                    ShowAlert1("Insertion Failed into Order Line");
                }
                else {
                    ShowAlert1("No Data to Send ");
                }
            } catch (NullPointerException ex) {

            }
        }

    }


    public void callCancelLogOut(View view) {
        Intent i=new Intent(FullViewPendingOrderActivity.this, PendingViewOrder.class);
        //i.putExtra("dateOrdered", dateOrdered);
        startActivity(i);
    }

    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(FullViewPendingOrderActivity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setNegativeButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public void ShowAlert1(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        //builder1.setPositiveButton("OK", null);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i=new Intent(FullViewPendingOrderActivity.this, PendingViewOrder.class);
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

    private final class AsyncLoadData extends AsyncTask<String, String, String>{

        ProgressDialog pd;
        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            // result =  FetchDataFromServer();
            //return result;
            String result = "", error = "";
            try {
                result = dbSYNC();
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

            pd = new ProgressDialog(FullViewPendingOrderActivity.this);
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
                // FetchOrderlineDataFromServer();
                AysncOrderlineData AysncOrderlineData = new AysncOrderlineData();
                AysncOrderlineData.execute();
            }
            else {
                toastMessage("Something Went Wrong!!!!");
            }
        }

    }

    private String dbSYNC() {
        String res = "";
        res=FetchDataFromServer();
        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;

    }

    private void setTextField(){
        String bp_location1="",phone1="",s_phone1="",s_name1="",promised_date1="",promisedAmt1="",percentdis1="",documentNo1="";
        if(bp_location!= null && !bp_location.isEmpty()) {
            bp_location1=bp_location;
        }
        /*if(!phone.equals("null") && phone!= null && !phone.isEmpty()) {
            phone1 = phone;
        }*/
        if(s_phone!= null && !s_phone.isEmpty()) {
            s_phone1 = s_phone;
        }
        if( s_name!= null && !s_name.isEmpty()) {
            s_name1 = s_name;
        }
        if( documentNo!= null && !documentNo.isEmpty()) {
            documentNo1 = documentNo;
        }
        orgnameTexView.setText(orgname);
        partyCodeTextView.setText(bpCode);
        partyNameTextView.setText(bpName);
        addressTextView.setText(bp_location1);
        //mobileNoTextView.setText(phone1);
        soNumberTextView.setText(documentNo1); //documentNo
        dateOrdered = dateOrdered.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
        soIssueDateTextView.setText(dateOrdered);
        srNameTextView.setText(s_name1);
        srMobileTextView.setText(s_phone1);
        //total_mtTextView.setText(String.valueOf(totalMetricTon));
        total_bagsTextView.setText(String.valueOf(totalBagQty));
        total_moneyTextView.setText(String.valueOf(totalBagQty));
        //promised_date1 = promised_date1.replaceAll("\\d\\d:\\d\\d:\\d\\d.\\d","");
        //promisedDateTextView.setText(promised_date);
        mtAmtTextView.setText(String.valueOf(total_price1));
        deliveryFromTextView.setText(warehouse);
        //partyBalanceTextView.setText(total_openbalance);
        //undeliveredAmtTextView.setText(undelivered_amt);
        qty_wordsTextView.setText(wordsAmtBags);
        desTextView.setText(description);
        truckno_text.setText(ispdis);
        prodactivity_name.setText(odiscount);
        activitytype_name.setText(percentdis);
        //proAmtTextView.setText(promisedAmt1);
        // undeliveredAmtTextView.setText(odiscount);
        //discout_EditText.setText(String.valueOf(odiscount));
        //undeliveredAmtTextView.setText(percentdis1); //ispdis
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        PendingProductsListRecyclerViewAdapter adapter = new PendingProductsListRecyclerViewAdapter(productDetailsModals, FullViewPendingOrderActivity.this);
        recyclerViewList.setAdapter(adapter);
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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        Intent intent=new Intent(FullViewPendingOrderActivity.this, PendingViewOrder.class);
        //intent.putExtra("dateOrdered", dateOrdered);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public String FetchDataFromServer() {
        int catagoryID;
        boolean result = false;
        boolean result1 = false;
        String  res = "";
        //Integer documentNumber_id=Integer.parseInt(documentNumber);
        try {
            Cursor dataorder = dbHelper.getPendingViewOrder(documentNumber,LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
            while (dataorder.moveToNext()) {
                bpCode = dataorder.getString(0);;
                bpName = dataorder.getString(1);
                phone = dataorder.getString(2);
                dateOrdered = dataorder.getString(3);
                warehouse = dataorder.getString(4);
                total_openbalance = dataorder.getString(5);
                String rate = (dataorder.getString(6));
                //uomsymbol  = ((String)line.get(7));
                old_kg = (dataorder.getString(8));
                //String productName  = ((String)line.get(9));
                documentNo = (dataorder.getString(10));
                des = (dataorder.getString(11));
                promised_date = (dataorder.getString(12));
                //undelivered_amt = ((String) line.get(13));
                s_name = (dataorder.getString(22));
                s_phone = (dataorder.getString(23));
                c_order_id = (dataorder.getString(18));
                String kg = (dataorder.getString(13));
                String qty = (dataorder.getString(14));
                String mt_qty = (dataorder.getString(15));
                description = (dataorder.getString(21));
                //promisedAmt = (dataorder.getString(21));
                bp_location = (dataorder.getString(16));
                orgname = (dataorder.getString(17));
                C_Doctype_ID = (dataorder.getString(22));
                odiscount = (dataorder.getString(20));//prodtype
                percentdis=(dataorder.getString(24));//acttype
                ispdis=(dataorder.getString(19)); //truckno

                //2022-10-10


                result = true;
            }
            if (result) {
                res = "Success";
            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;

    }
    //2022-09-27
    private final class AysncOrderlineData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
        }*/

        @Override
        protected String doInBackground(String... params) {

            String result1="",error="";boolean r=false;
            boolean result = false;
            //int documentNumber_id=Integer.parseInt(documentNumber);
            try {
                Cursor dataorderLine = dbHelper.getPendingViewOrderLine(documentNumber,LoginActivity.ad_user_id);
                while (dataorderLine.moveToNext()) {
                    String productName = (dataorderLine.getString(0));
                    String uom = (dataorderLine.getString(1));
                    String qty = (dataorderLine.getString(2));
                    String rate = (dataorderLine.getString(3));
                    String total = (dataorderLine.getString(4));
                    double takaAmt = 0;
                    if (!qty.equals("")) {
                        totalBagQty = totalBagQty + Double.parseDouble(qty);
                        if (!rate.equals("")) {
                            takaAmt = Double.parseDouble(qty) * Double.parseDouble(rate);
                            totalTakaAmt = totalTakaAmt + takaAmt;
                        }
                    }
                    if (!qty.equals("")) {
                        totalMetricTon = totalMetricTon + Double.parseDouble(qty);
                    }
                    PendingProductDetailsModal modal = new PendingProductDetailsModal(productName, uom, qty, rate, String.valueOf(total));
                    productDetailsModals.add(modal);
                    result = true;
                }
                if (result) {
                    result1 = "Success";
                }
            } catch (Exception e) {
                result1 = e.getMessage();
            }
            return result1;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(FullViewPendingOrderActivity.this);
            //   pd.setTitle("Please Wait ");
            //  pd.setMessage("KFG Bank Accounts are fetching........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(s.equals("Success")){
                    long intTotalPrice = (new Double(totalBagQty)).longValue();
                    wordsAmtBags =   NumberToWords2.convert(intTotalPrice);
                    setTextField();
                    initRecyclerView();
                    pd.dismiss();
                }else{
                    System.out.println(s);
                    pd.dismiss();
                }
            }catch (NullPointerException ex){
                NullPointerException result1=ex;

            }
        }
    }


    public void loadOrderPrice(Context mContext) {

        try {
            totalPriceTextView.setText(String.valueOf(totalTakaAmt));
            //total_editTextET.setText(String.valueOf(totalTakaAmt));
            total_editTextET.setText(String.valueOf(total_price1));
        } catch (Exception e) {
            Log.d("DEBUG", String.valueOf(e.getMessage()));
            System.out.println(String.valueOf(e.getMessage()));
        }
    }
    private void ondiscountEditTextFocusChange(){

        discout_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!totalPriceTextView.getText().toString().equals("") && !discout_EditText.getText().toString().equals("") ){
                    if(!totalPriceTextView.getText().toString().equals(".") && !discountCheckBox
                            .getText().toString().equals(".") ) {
                        BigDecimal price =new BigDecimal(totalTakaAmt); //new BigDecimal(totalPriceTextView.getText().toString());

                        String d=discout_EditText.getText().toString();
                        BigDecimal total_price=new BigDecimal(0);
                        if(!d.isEmpty())
                        {
                            double discount=0.0;
                            if(discountCheckBox.isChecked())
                            {
                                ispercentdiscount=true;
                                //discount=((price )*Double.parseDouble(discout_editText.getText().toString()))/100;
                                BigDecimal discount1=(price.multiply(new BigDecimal(discout_EditText.getText().toString()))).divide(new BigDecimal(100));
                                //total_price=(price )-discount;
                                total_price=price.subtract(discount1);


                            }
                            else
                            {
                                ispercentdiscount=false;
                                //discount=Double.parseDouble(discout_editText.getText().toString());
                                BigDecimal discount1=new BigDecimal(discout_EditText.getText().toString());
                                total_price=price.subtract(discount1);


                            }


                        }
                        else
                        {
                            total_price=price ;
                        }

                        if(total_price.compareTo(new BigDecimal(0)) <=0 )
                        //if(total_price<=0)
                        {
                            toast("Discount amount is higher than total price");
                            total_editTextET.setText(String.valueOf(0.0));
                        }
                        else
                        {
                            total_editTextET.setText(String.valueOf(total_price));
                        }

                    }
                }else{
                    //total_editTextET.setText(String.valueOf(totalTakaAmt));
                    total_editTextET.setText(String.valueOf(total_price1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void ondiscountCheckBox(){
        discountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BigDecimal price =new BigDecimal(totalTakaAmt); // new BigDecimal(totalPriceTextView.getText().toString());
                String d=discout_EditText.getText().toString();
                BigDecimal total_price=new BigDecimal(0);
                if (buttonView.isChecked()) {
                    if(!d.isEmpty())
                    {
                        double discount=0.0;
                        ispercentdiscount=true;
                        BigDecimal discount1=(price.multiply(new BigDecimal(discout_EditText.getText().toString()))).divide(new BigDecimal(100));
                        total_price=price.subtract(discount1);
                        if(total_price.compareTo(new BigDecimal(0)) <=0 )
                        {
                            toast("Discount amount is higher than total price");
                            total_editTextET.setText(String.valueOf(0.0));
                        }
                        else
                        {
                            total_editTextET.setText(String.valueOf(total_price));
                        }
                    }
                    else
                    {
                        total_price=price ;
                        if(total_price.compareTo(new BigDecimal(0)) <=0 )
                        {
                            toast("Discount amount is higher than total price");
                            total_editTextET.setText(String.valueOf(0.0));
                        }
                        else
                        {
                            total_editTextET.setText(String.valueOf(total_price1));
                        }
                    }

                }
                else
                {
                    ispercentdiscount=false;
                    if(d.isEmpty())
                    {
                        total_editTextET.setText(String.valueOf(total_price1));
                        //BigDecimal discount1=new BigDecimal(discout_EditText.getText().toString());
                        //total_price=price.subtract(discount1);
                    }
                    else
                    {
                        BigDecimal discount1=new BigDecimal(discout_EditText.getText().toString());
                        total_price=price.subtract(discount1);
                        if(total_price.compareTo(new BigDecimal(0)) <=0 )
                        {
                            toast("Discount amount is higher than total price");
                            total_editTextET.setText(String.valueOf(0.0));
                        }
                        else
                        {
                            total_editTextET.setText(String.valueOf(total_price));
                        }

                    }
                    //BigDecimal discount1=new BigDecimal(discout_EditText.getText().toString());
                    //total_price=price.subtract(discount1);

                }
            }

        });
    }

    public void toast(String msg)
    {
        Toast.makeText(FullViewPendingOrderActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
