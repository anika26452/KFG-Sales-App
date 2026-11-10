package com.sysnova.kfg_sales_appv903.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.sysnova.kfg_sales_appv903.adapter.ApprovalProductsListRecyclerViewAdapter;
import com.sysnova.kfg_sales_appv903.modal.ApprovalProductDetailsModal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.ad_user_id;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.isEmployee;
import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

/*
 Created by Shanzida
 */
public class FullViewOrderActivity extends AppCompatActivity {

    String bpCode,bpName,phone,dateOrdered,warehouse,total_openbalance,bp_location,description,orgname;
    String  old_kg,documentNo,des,promised_date,s_name,s_phone,c_order_id,C_Doctype_ID;
    String wordsAmtBags; //uomsymbol,undelivered_amt,promisedAmt
    Spinner ForwardToSpinner;
    TextView partyCodeTextView,partyNameTextView,addressTextView,mobileNoTextView,soNumberTextView,soIssueDateTextView,srNameTextView,srMobileTextView;
    TextView total_mtTextView,total_bagsTextView,total_moneyTextView,promisedDateTextView,qty_wordsTextView,orgnameTexView;
    TextView mtAmtTextView,deliveryFromTextView,desTextView,totalPriceTextView,truckno_text,prodactivity_name,activitytype_name;//,proAmtTextView,discout_editText1,partyBalanceTextView,undeliveredAmtTextView;
    EditText discout_EditText,total_editTextET;//discout_editText,msg_text
    Button orgEdit_button;
    RecyclerView recyclerViewList;
    double totalMetricTon = 0,totalBagQty = 0,totalTakaAmt = 0;
    BigDecimal total_price1=new BigDecimal(0);
    ArrayList<ApprovalProductDetailsModal> productDetailsModals = new ArrayList<>();
    String msg = "",isApproved = "",documentNumber = "",erpDocumentNumber = "",odiscount,supText="",percentdis="",ispdis="",tmsg="";
    String selectedForwardToId="",selectedForwardToName="",userid="",pricelisttype="";

    CheckBox discountCheckBox;
    DatabaseHelper dbHelper;
    List<String> ForwardToArrayList = new ArrayList<String>();
    HashMap<String,String> hashForwardToId  = new HashMap<>();
    //ArrayList<String> dataList = new ArrayList<>();
    ArrayList<Vector<String>> dataList = new ArrayList<>();


    boolean ispercentdiscount=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_vieworder);

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
        orgEdit_button =findViewById(R.id.orgEdit_button);
        if(isEmployee.equals("N")) {
            orgEdit_button.setClickable(false);
            orgEdit_button.setVisibility(View.GONE);
        }

        //proAmtTextView         = findViewById(R.id.promAmt_id);
        // undeliveredAmtTextView = findViewById(R.id.dis_so_amt);

        //ForwardToSpinner =findViewById(R.id.ForwardTo_id_spinner);
        //discout_EditText= findViewById(R.id.discout_editText);
        //discountCheckBox = (CheckBox)findViewById(R.id.discount_checkbox);
        //totalPriceTextView = findViewById(R.id.grandtotal);
        //total_editTextET= findViewById(R.id.grandtotal);

        //msg_text = findViewById(R.id.msg_id);
        // yes_button = findViewById(R.id.yes_btn);
        //no_button = findViewById(R.id.no_btn);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            documentNumber   = getIntent().getStringExtra("order_doc_id");
            //erpDocumentNumber= getIntent().getStringExtra("erp_doc");
        }
        getSupportActionBar().setTitle("Order View Activity");
        //getSupportActionBar().setSubtitle("Brac Fisheries App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));

        dbHelper = new DatabaseHelper(this);
        //ViewOrderNew.class
        AsyncLoadData asyncLoadData = new AsyncLoadData();
        asyncLoadData.execute();
    }
    public void callCancelLogOut(View view) {
        Intent i=new Intent(FullViewOrderActivity.this, ViewOrder.class);
        i.putExtra("dateOrdered", dateOrdered);
        startActivity(i);
    }
    public void ShowAlert(String title,String msg){
        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(FullViewOrderActivity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setNegativeButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
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

            pd = new ProgressDialog(FullViewOrderActivity.this);
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
                //wordsAmtBags = NumberToWords1.convert((int)totalBagQty);
                /*long intTotalPrice = (new Double(totalBagQty)).longValue();
                wordsAmtBags =   NumberToWords2.convert(intTotalPrice);
                setTextField();

                initRecyclerView();*/
                //loadOrderList();
                //loadOrderPrice(FullViewOrderActivity.this);
                // getForwardTo();
            }
            else {
                toastMessage("Something Went Wrong!!!!");
            }
        }

    }

    private String dbSYNC() {
        String res = "";
        res=FetchDataFromServer();
        //res=FetchForwardToDataFromServer();
        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;

    }

    private void setTextField(){
        String bp_location1="",phone1="",s_phone1="",s_name1="",promised_date1="",promisedAmt1="",percentdis1="",documentNo1="";
        if(!bp_location.equals("null") && bp_location!= null && !bp_location.isEmpty()) {
            bp_location1=bp_location;
        }
        if(!phone.equals("null") && phone!= null && !phone.isEmpty()) {
            phone1 = phone;
        }
        if(!s_phone.equals("null") && s_phone!= null && !s_phone.isEmpty()) {
            s_phone1 = s_phone;
        }
        if(!s_name.equals("null") && s_name!= null && !s_name.isEmpty()) {
            s_name1 = s_name;
        }
        if(!documentNo.equals("null") && documentNo!= null && !documentNo.isEmpty()) {
            documentNo1 = documentNo;
        }
        orgnameTexView.setText(orgname);
        partyCodeTextView.setText(bpCode);
        partyNameTextView.setText(bpName);
        addressTextView.setText(bp_location1);
        mobileNoTextView.setText(phone1);
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
        //undeliveredAmtTextView.setText(odiscount);
        //discout_EditText.setText(String.valueOf(odiscount));
        //undeliveredAmtTextView.setText(percentdis1); //ispdis
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        ApprovalProductsListRecyclerViewAdapter adapter = new ApprovalProductsListRecyclerViewAdapter(productDetailsModals, FullViewOrderActivity.this);
        recyclerViewList.setAdapter(adapter);
    }

    private final class AsyncSender2 extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="",tt="N",fid="0",dis="0";
            boolean rt = false;

            try {
                ArrayList salesHeader=new ArrayList();
                String changeDiscount=discout_EditText.getText().toString();
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/ApprovalOrder")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                if(!changeDiscount.isEmpty()) {
                    if(odiscount!=changeDiscount){
                        dis=changeDiscount;
                    }
                    //changeDiscount="0";
                }
                else{
                    dis=odiscount;
                }
                if(!selectedForwardToId.equals("null") && selectedForwardToId!= null && !selectedForwardToId.isEmpty()) {
                    fid=selectedForwardToId;
                }
                Vector headerLines=new Vector();
                headerLines.add(OrgBranchSelection.selectedOrgId);

                headerLines.add(LoginActivity.ad_user_id);
                headerLines.add(isApproved);
                headerLines.add(erpDocumentNumber);
                headerLines.add(fid);
                headerLines.add(dis);
                if(discountCheckBox.isChecked())
                {
                    headerLines.add("Y");
                }
                else
                {
                    headerLines.add(ispdis);
                }
                headerLines.add(msg);
                salesHeader.add(headerLines);

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;

                    }
                }

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/ApprovalOrder");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "approvalOrder"));
                list.add(new BasicNameValuePair("header", headerString));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.equals("InsertedSuccessfully")){
                    result=s;
                    return result;
                }else{
                    return "Success";
                }
            }
            catch (Exception e){
                error=e.getMessage();
                return  error;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(FullViewOrderActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Updating....");
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
                toastMessage("Success");
                //Intent intent  = new Intent(UpdateApprovalActivity.this,Home.class);
                //startActivity(intent);
                //finish();
            }
            else {
                toastMessage("Something Went Wrong!!!!");
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
        Intent intent=new Intent(FullViewOrderActivity.this, ViewOrder.class);
        intent.putExtra("dateOrdered", dateOrdered);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //2024-10-07 callEdit added by shanzida
    public void callEdit(View view) {
        String TrxType="Edit";
        dbHelper.deleteProductOrderHeadNewEdit(TrxType);
        dbHelper.deleteAllProductOrderLineEdit(TrxType);
        boolean result=false,result1=false;
        int saved=0;
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        int id =sh.getInt("NUM",0);
        int id1=id+1;
        double qty=0.00,total=0.00,rate=0.00;
        String bpCodeName= bpCode+"-"+bpName;
        result = dbHelper.insertIntoProductOrderHeadNew("1001", odiscount, percentdis, s_name, bp_location,
                orgname, warehouse, bpCodeName, ispdis, pricelisttype, dateOrdered, userid, TrxType,c_order_id);
      if (result) {
            for (Vector<String> productDetails : dataList) {
                String Productcode = productDetails.get(0);
                String Productname = productDetails.get(1);
                String uom = productDetails.get(2);
                String qty1 = productDetails.get(3);
                String rate1 = productDetails.get(4);
                String total1 = productDetails.get(5);
                String id2 = productDetails.get(6);
                String product_id = productDetails.get(7);
                String T_SalesOrderLine_id = productDetails.get(8);

                qty= Double.parseDouble(qty1);
                rate= Double.parseDouble(rate1);
                total= Double.parseDouble(total1);
                id1=id1+1;
                result1 = dbHelper.insertIntoProductOrderLineEdit(id1, Productcode, Productname, qty, rate, total, c_order_id, product_id, userid,TrxType,T_SalesOrderLine_id); //,productorderhead_Id);
                if (result1) {
                    saved=saved+1;
                }
            }
            if (saved > 0) {
                Cursor cursor1 = dbHelper.getDB_Sync(ad_user_id);
                if (cursor1.moveToFirst()) {
                    Intent i = new Intent(FullViewOrderActivity.this, OrgBranchSelection.class);
                    i.putExtra("documentNumber", c_order_id);
                    i.putExtra("TrxType", TrxType);
                    startActivity(i);
                }
                else{
                    ShowAlert("Warning", "DB Sync is Required.");
                }
            }
        }
        else {
            dbHelper.deleteProductOrderHeadNewEdit(TrxType);
            dbHelper.deleteAllProductOrderLineEdit(TrxType);
            toast("Could not insert into internal database");
        }
    }


    public String FetchDataFromServer() {
        int catagoryID;
        boolean result = false;
        boolean result1 = false;
        String  res = "";
        HttpURLConnection con =null;

        try {
            //HttpURLConnection
            con = (HttpURLConnection) ( new URL(""+apiUrl+"/SyncViewOrderDetails2")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/SyncViewOrderDetails2");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("erpdoc", documentNumber));
            //list.add(new BasicNameValuePair("erpdoc", erpDocumentNumber));
            //list.add(new BasicNameValuePair("m_warehouse_id",String.valueOf(branchId)));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="No Data Found";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String test = parts[i];
                String[] parts1 = parts[i].split(",",29);
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);

                }
                list4.add(line);
            }

            for(int i1=0;i1<list4.size();i1++) {
                Vector line = (Vector) list4.get(i1);
                bpCode = ((String) line.get(0));
                bpName = (String) line.get(1);
                phone = ((String) line.get(2));
                dateOrdered = ((String) line.get(3));
                warehouse = ((String) line.get(4));
                total_openbalance = ((String) line.get(5));
                String rate = ((String) line.get(6));
                //uomsymbol  = ((String)line.get(7));
                old_kg = ((String) line.get(8));
                //String productName  = ((String)line.get(9));
                documentNo = ((String) line.get(10));
                des = ((String) line.get(11));
                promised_date = ((String) line.get(12));
                //undelivered_amt = ((String) line.get(13));
                s_name = ((String) line.get(13));
                s_phone = ((String) line.get(14));
                c_order_id = ((String) line.get(15));
                String kg = ((String) line.get(16));
                String qty = ((String) line.get(17));
                String mt_qty = ((String) line.get(18));
                description = ((String) line.get(19));
                //promisedAmt = ((String) line.get(21));
                bp_location = ((String) line.get(20));
                orgname = ((String) line.get(21));
                C_Doctype_ID = ((String) line.get(22));
                odiscount = ((String) line.get(23));//prodtype
                percentdis=((String) line.get(24));//acttype
                ispdis=((String) line.get(25)); //truckno
                //tmsg=((String)line.get(26));
                //2024-10-19
                pricelisttype = ((String) line.get(26));
                userid = ((String) line.get(27));
                String docstatus= ((String) line.get(28));
                if(!isEmployee.equals("N")) {
                    orgEdit_button.setClickable(!docstatus.equals("CO") && !docstatus.equals("CL"));
                }else{
                    orgEdit_button.setClickable(false);
                    orgEdit_button.setVisibility(View.GONE);
                }

//                if (docstatus.equals("CO") || docstatus.equals("CL")){
//                    orgEdit_button.setClickable(false);
//                }
//                else{
//                    orgEdit_button.setClickable(true);
//                }
                //2024-10-19
                result = true;
            }
            if (result) {
                res = "Success";
            }


        } catch (Exception e) {
            res = e.getMessage();
        }
        finally {
            try {
                if (con != null)
                    con.disconnect();
                con = null;
            } catch (Exception e) {
            }
        }
        return res;

    }
    //2022-09-27
    private final class AysncOrderlineData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result1="",error="";boolean r=false;
            boolean result = false;
            HttpURLConnection con=null;
            try {
                // mDatabaseHelper.deleteEmployee();
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "salesorderlineList"));
                //list.add(new BasicNameValuePair("erpdoc", erpDocumentNumber));
                list.add(new BasicNameValuePair("erpdoc", documentNumber));
                //list.add(new BasicNameValuePair("m_warehouse_id",String.valueOf(branchId)));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);
                if (s.isEmpty() || !s.startsWith("[")) {
                    //res = "No Data Found";
                }

                ArrayList list4 = new ArrayList();

                String[] parts = s.split("_");
                for (int i = 0; i < parts.length; i++) {
                    Vector line = new Vector();
                    String test = parts[i];
                    String[] parts1 = parts[i].split(",", 9);
                    for (int j = 0; j < parts1.length; j++) {
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                        value = value.replaceAll("[\\[\\]]", "");
                        line.add(value);
                    }
                    list4.add(line);
                }
                for (int i1 = 0; i1 < list4.size(); i1++) {
                    Vector line = (Vector) list4.get(i1);
                    String productcode = ((String) line.get(0));
                    String productName = ((String) line.get(1));
                    String uom = ((String) line.get(2));
                    String qty = ((String) line.get(3));
                    String rate = ((String) line.get(4));
                    String total = ((String) line.get(5));
                    String product_id = ((String) line.get(6));
                    String T_SalesOrderLine_id = ((String) line.get(7));
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
                    //2024-10-21
                    // Create a new Vector to hold the product details
                    Vector<String> productDetails = new Vector<>();
                    productDetails.add(productcode);
                    productDetails.add(productName);
                    productDetails.add(uom);
                    productDetails.add(qty);
                    productDetails.add(rate);
                    productDetails.add(String.valueOf(total));
                    productDetails.add(String.valueOf(i1+1));
                    productDetails.add(String.valueOf(product_id));
                    productDetails.add(String.valueOf(T_SalesOrderLine_id));

                    // Add the product details Vector to the dataList
                    dataList.add(productDetails);
                    //2024-10-21

                    ApprovalProductDetailsModal modal = new ApprovalProductDetailsModal(productName, uom, qty, rate, String.valueOf(total));
                    productDetailsModals.add(modal);
                    result = true;
                }
                if (result) {

                    result1 = "Success";
                    //toast("Success");
                }
            }
            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error="Failed To Connect";
                return  error;
            }
            finally {
                try {
                    if (con != null)
                        con.disconnect();
                    con = null;
                } catch (Exception e) {
                }
            }
            return result1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(FullViewOrderActivity.this);
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

            }
        }
    }
    /*public String readResponse(HttpResponse res) {
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
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }*/



    //2022-09-27
    public void FetchOrderlineDataFromServer() {
        int catagoryID;
        boolean result = false;
        String  res = "";
        HttpURLConnection con =null;

        try {
            try {
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();

                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "salesorderlineList"));
                //list.add(new BasicNameValuePair("erpdoc", erpDocumentNumber));
                list.add(new BasicNameValuePair("erpdoc", documentNumber));
                //list.add(new BasicNameValuePair("m_warehouse_id",String.valueOf(branchId)));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);
                if (s.isEmpty() || !s.startsWith("[")) {
                    res = "No Data Found";
                    toast("No Forwardto found for this Order");
                }

                ArrayList list4 = new ArrayList();

                String[] parts = s.split("_");
                for (int i = 0; i < parts.length; i++) {
                    Vector line = new Vector();
                    String test = parts[i];
                    String[] parts1 = parts[i].split(",", 7);
                    for (int j = 0; j < parts1.length; j++) {

                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                        value = value.replaceAll("[\\[\\]]", "");
                        line.add(value);

                    }
                    list4.add(line);
                }

                for (int i1 = 0; i1 < list4.size(); i1++) {
                    Vector line = (Vector) list4.get(i1);
                    String productcode = ((String) line.get(0));
                    String productName = ((String) line.get(1));
                    String uom = ((String) line.get(2));
                    String qty = ((String) line.get(3));
                    String rate = ((String) line.get(4));
                    String total = ((String) line.get(5));
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

                    ApprovalProductDetailsModal modal = new ApprovalProductDetailsModal(productName, uom, qty, rate, String.valueOf(total));
                    productDetailsModals.add(modal);
                    result = true;
                }
                if (result) {

                    res = "Success";
                    toast("Success");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                toastMessage(e.getMessage());

            }
            finally {
                try {
                    if (con != null)
                        con.disconnect();
                    con = null;
                } catch (Exception e) {
                }
            }


        } catch (Exception e) {
            res = e.getMessage();
            toast(res);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
        Toast.makeText(FullViewOrderActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
