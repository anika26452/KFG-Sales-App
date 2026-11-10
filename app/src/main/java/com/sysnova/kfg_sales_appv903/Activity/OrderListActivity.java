package com.sysnova.kfg_sales_appv903.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.*;

import com.sysnova.kfg_sales_appv903.adapter.OrderListRecyclerViewAdapter;
import com.sysnova.kfg_sales_appv903.modal.OrderListModal;


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
import java.util.List;
import java.util.Vector;
import com.sysnova.kfg_sales_appv10.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import static com.sysnova.kfg_sales_app.Activity.Cutomer_Selection_Activity.selectedSalesType;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedOrgId;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedWHId;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedWHName;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedactivityName;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedactivityID;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.ad_user_id;
/*import static com.sysnova.kfg_sales_app.Activity.OrgBranchSelection.selectedDealerBankName;
import static com.sysnova.kfg_sales_app.Activity.OrgBranchSelection.selectedKFGBankName;

import static com.sysnova.kfg_sales_app.Activity.OrgBranchSelection.PaymentAmt;
import static com.sysnova.kfg_sales_app.Activity.OrgBranchSelection.mrno;*/
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.truckno;
import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

/*
 Created by Shanzida
 */
public class OrderListActivity extends AppCompatActivity {

    static String docNo,chemName,chemId;
    String customerName;
    //double total_price = 0.00;
    BigDecimal total_price;
    static TextView totalPriceTextView,totalPriceInWordsTextView,SelectedOrgName;
    Button sendToServerButton;
    Toolbar toolbar;
    RecyclerView orderListRecyclerView;
    DatabaseHelper mDatabaseHelper;
    String customerName_Code="";
    CheckBox codeCheckbox;
    TextView customerName_CodeTV;
    ArrayList<OrderListModal> orderListModalArrayList = new ArrayList<>();
    EditText discout_editText,Remarks_editText;
    static EditText total_editTextET;
    CheckBox discountCheckBox;
    EditText reference_no_et;
    boolean ispercentdiscount=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_activty);
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

       docNo=getIntent().getStringExtra("DOCNO");
         customerName_Code=getIntent().getStringExtra("CUSTOMERNAMECODE");

        orderListRecyclerView = findViewById(R.id.order_list_recyclerview);
        customerName_CodeTV=findViewById(R.id.customernamecode);
        customerName_CodeTV.setText(customerName_Code);
        total_editTextET= findViewById(R.id.grandtotal);
        totalPriceTextView = findViewById(R.id.tPrice_textView);
        totalPriceInWordsTextView = findViewById(R.id.tPrice_textView_inwords);
        sendToServerButton = findViewById(R.id.send_order_btn);
        reference_no_et=(EditText)findViewById(R.id.reference_editText);

        discout_editText=(EditText)findViewById(R.id.discount_editText);
        Remarks_editText=(EditText)findViewById(R.id.Remarks_editText); // Remarks_editText newly added by shanzida 2021-11-20


        discountCheckBox = (CheckBox)findViewById(R.id.discount_checkbox);


        mDatabaseHelper = new DatabaseHelper(this);
        loadOrderList();
        loadOrderPrice(OrderListActivity.this);
      //  sendToServer();
        ondiscountEditTextFocusChange();
        ispercentdiscount=false;
    }


    public void loadOrderPrice(Context mContext){

        try{
            DatabaseHelper mDatabaseHelper = new DatabaseHelper(mContext);
            //total_price=Double.parseDouble(mDatabaseHelper.getTotalPrice(docNo));
            //total_price convert to new BigDecimal 2021-11-17
            total_price=new BigDecimal(mDatabaseHelper.getTotalPrice(docNo));
            Log.d("DEBUG",String.valueOf(total_price));
            System.out.println(String.valueOf(total_price));

            totalPriceTextView.setText(String.valueOf(total_price));

            //int intTotalPrice = (int) total_price;
            long intTotalPrice = total_price.longValue() ;
            String return_val_in_words =   NumberToWords2.convert(intTotalPrice);
            totalPriceInWordsTextView.setText(return_val_in_words +" Taka");

            //total_editTextET.setText(String.valueOf(Double.parseDouble(mDatabaseHelper.getTotalPrice(docNo))));
            total_editTextET.setText(String.valueOf(new BigDecimal(mDatabaseHelper.getTotalPrice(docNo))));
        }
        catch (Exception e)
        {
           // toastMessage(e.getMessage().toString());
            Log.d("DEBUG",String.valueOf(e.getMessage()));
            System.out.println(String.valueOf(e.getMessage()));
        }

        /**Cursor totalPrice = mDatabaseHelper.getTotalPrice(docNo);
        if(totalPrice.moveToFirst()){
            do{
                total_price = totalPrice.getDouble(0);
            }while(totalPrice.moveToNext());
        }**/

//        Cursor check = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
//        if(!check.moveToNext()) {
//            sendToServerButton.setEnabled(false);
//        }
    }
    void loadTotalPrice()
    {

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i=new Intent(OrderListActivity.this,Sales_Order_Activity1.class);
                i.putExtra("DOCNO",docNo);
                i.putExtra("CUSTOMERNAMECODE",customerName_CodeTV.getText().toString());
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadOrderList(){
        Cursor data = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
        if(data.moveToFirst()){
            do{
               // int p_key = data.getInt(0);
                String productcat_ID=data.getString(2);
                String customername_value =String.valueOf(data.getString(3));
                //int qty= data.getInt(4);
                String qty= data.getString(4);
                //price =Double.parseDouble(new DecimalFormat("##.####").format(price));
                String productName = data.getString(1);
                String total = data.getString(5);
                String uom       = data.getString(6);
                String doc       = data.getString(7);
                String product_ID=data.getString(8);
                String pID=data.getString(10);
                //String erpdocNo=

               // total_price = total_price + price;
                OrderListModal orderListModal = new OrderListModal(productName,productcat_ID,customername_value,
                        qty,total,uom,doc,product_ID);
                orderListModalArrayList.add(orderListModal);
            }while(data.moveToNext());
        }
        if(orderListModalArrayList == null || orderListModalArrayList.isEmpty()){
            toast("No Order Found");
        }else{
            initRecyclerView();
            /*double t=0.0;
            t=getTotal();
            int Tqty=0;
            Tqty=getTotalQty();
            qtySum.setText(String.valueOf(Tqty));

            toatlSum.setText(String.valueOf(t));*/

        }
        //initRecyclerView();

    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        OrderListRecyclerViewAdapter adapter = new OrderListRecyclerViewAdapter(orderListModalArrayList,OrderListActivity.this);
        orderListRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

   /** private void sendToServer(){
            sendToServerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToServerButton.setEnabled(false);
                    AsyncSendToServer task = new AsyncSendToServer();
                    task.execute();
                }
            });
    }**/


    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "";


            boolean isActive=true;

            Cursor check = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
            String Remarks= Remarks_editText.getText().toString(); //2021-11-20
            if(!Remarks.equals("null") && Remarks!= null && !Remarks.isEmpty()) {
                mDatabaseHelper.updateOrderDes(docNo, Remarks);
            }
            if(check.moveToNext()) {

            } else {
                // toastMessage("No OrderLine Exists ");
                 return "noOrderLine";
            }
            try {

                ArrayList salesHeader=new ArrayList();

                  String customer_code="";

                  String discount=discout_editText.getText().toString();
                  int pos=customerName_CodeTV.getText().toString().indexOf('-');
                  customer_code=customerName_CodeTV.getText().toString(); //.substring(pos+2,customerName_CodeTV.getText().toString().length());
                    Vector headerLines=new Vector();
                    headerLines.add(selectedOrgId);

                    headerLines.add(selectedWHId);
                    headerLines.add(docNo);
                    headerLines.add(ad_user_id);//employeeid);
                    headerLines.add(selectedactivityName);
                    headerLines.add(customer_code);
                    headerLines.add(total_editTextET.getText().toString());
                    headerLines.add(reference_no_et.getText().toString());
                    //headerLines.add(Remarks_editText.getText().toString()); //2021-11-20
                    if(discount.isEmpty())
                    {
                        discount="0";
                    }

                    headerLines.add(discount);
                if(ispercentdiscount)
                    {
                        headerLines.add("Y");
                    }
                    else
                    {
                        headerLines.add("N");
                    }
                headerLines.add(Remarks_editText.getText().toString()); //2021-11-20
                headerLines.add(selectedactivityID);

                //headerLines.add(selectedDealerBankName);
               // headerLines.add(selectedKFGBankName);
               // headerLines.add(PaymentAmt);
                headerLines.add(truckno);
                //headerLines.add(mrno);


                Log.d("DEBUG","selectedOrgId"+selectedOrgId);
                Log.d("DEBUG","selectedWHName"+selectedWHName);
                Log.d("DEBUG","docNo"+docNo);
                Log.d("DEBUG","ad_user_id"+ad_user_id);
                Log.d("DEBUG","customerName_CodeTV"+customerName_CodeTV.getText().toString());
                Log.d("DEBUG","total_editTextET"+total_editTextET.getText().toString());
                Log.d("DEBUG","reference_no_et"+reference_no_et.getText().toString());
                //Log.d("DEBUG","Remarks_editText"+Remarks_editText.getText().toString());


                    salesHeader.add(headerLines);





                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;

                    }
                }

                Cursor dataorderLine = mDatabaseHelper.getSalesOrderLineDataTemp(docNo);
                String replacedProductname="";
                ArrayList salesOrder=new ArrayList();
                while (dataorderLine.moveToNext()) {


                    String product_name = dataorderLine.getString(1);
                    replacedProductname=product_name.replaceAll("_","-");
                    //int quantity = dataorderLine.getInt(4); //2021-12-22
                    String quantity = dataorderLine.getString(4);
                    String unit_price  = dataorderLine.getString(3);
                    String uom=dataorderLine.getString(6);
                    String searchkey=dataorderLine.getString(9);
                    Vector orderLines=new Vector();
                    orderLines.add(selectedOrgId);
                    orderLines.add(replacedProductname);
                    orderLines.add(searchkey);
                    orderLines.add(String.valueOf(quantity));
                    orderLines.add(uom);
                    orderLines.add(String.valueOf(unit_price));

                    Log.d("DEBUG","selectedOrgId"+selectedOrgId);
                    Log.d("DEBUG","product_name"+replacedProductname);
                    Log.d("DEBUG","Search"+"key");
                    Log.d("DEBUG","quantity"+quantity);
                    Log.d("DEBUG","uom"+uom);
                    Log.d("DEBUG","unit_price"+unit_price);


                    salesOrder.add(orderLines);


                }


                String lineString = "";
                for (Object s : salesOrder) {
                    if (lineString.isEmpty()) {
                        lineString =lineString+s;
                    } else {
                        lineString =lineString+"_" + s;

                    }
                }

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("header", headerString));
                list.add(new BasicNameValuePair("lines",lineString));
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

            } catch (Exception e){
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
                    mDatabaseHelper.deleteSalesOrder();
                    mDatabaseHelper.deleteSalesOrderLine();
                    toastMessage("Order Placed Successfully");
                   // sendToServerButton.setBackgroundColor(getColor(R.color.colorGreen));
                    Intent intent = new Intent(OrderListActivity.this,SelectionAcitvity.class);
                    startActivity(intent);
                    finish();
                }else if(s.equals("noOrderLine")){
                    toastMessage("No order line exits");
                } else{
                    //sendToServerButton.setEnabled(true);
                    //toastMessage("Server Connection lost Please Send Again");
                    ShowAlert("Error","Server Connection Failed Please Send Again, "+s);
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


    private void toastMessage(String message){
        Toast.makeText(OrderListActivity.this,message, Toast.LENGTH_SHORT).show();
    }

    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(OrderListActivity.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderListActivity.this,OrgBranchSelection.class); //Sales_Order_Activity1.class); stop by shanzida
        intent.putExtra("DOCNO",docNo);
        intent.putExtra("CUSTOMERNAMECODE",customerName_CodeTV.getText().toString());
        startActivity(intent);
        finish();
    }
    public void callAddMoreProducts(View view) {

        Intent i=new Intent(OrderListActivity.this, Sales_Order_Activity1.class);
        startActivity(i);
    }
    public void callCancel(View view) {
        //databaseHelper.deleteAllProductOrderLine();
        Intent i=new Intent(OrderListActivity.this, Sales_Order_Activity1.class);
        startActivity(i);
    }

    public void sendtoservertemp(View view) {
       /** mDatabaseHelper.deleteSalesOrderLine();
        toastMessage("order Placed");
        Intent i=new Intent(OrderListActivity
        .this,Home.class);

        startActivity(i);**/
       AsyncSendToServer asyncSendToServer=new AsyncSendToServer();
       asyncSendToServer.execute();
    }

    private void ondiscountEditTextFocusChange(){

        discout_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!totalPriceTextView.getText().toString().equals("") && !discout_editText.getText().toString().equals("") ){
                    if(!totalPriceTextView.getText().toString().equals(".") && !discountCheckBox
                            .getText().toString().equals(".") ) {
                        //double price = Double.parseDouble(totalPriceTextView.getText().toString());
                        //double to BigDecimal
                        BigDecimal price = new BigDecimal(totalPriceTextView.getText().toString());

                        String d=discout_editText.getText().toString();
                        BigDecimal total_price=new BigDecimal(0);
                        if(!d.isEmpty())
                        {
                            double discount=0.0;
                            if(discountCheckBox.isChecked())
                            {
                                ispercentdiscount=true;
                                //discount=((price )*Double.parseDouble(discout_editText.getText().toString()))/100;
                                BigDecimal discount1=(price.multiply(new BigDecimal(discout_editText.getText().toString()))).divide(new BigDecimal(100));
                                //total_price=(price )-discount;
                                total_price=price.subtract(discount1);
                                /**  if(total_price<0)
                                 {
                                 toast("discount amount is higher than total price");
                                 //total_editText.setText(String.valueOf(0.0));

                                 }**/

                            }
                            else
                            {
                                ispercentdiscount=false;
                                //discount=Double.parseDouble(discout_editText.getText().toString());
                                BigDecimal discount1=new BigDecimal(discout_editText.getText().toString());



                                //total_price = (price )-discount;
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
                            toast("discount amount is higher than total price");
                            //total_editText.setText(String.valueOf(0.0));
                            total_editTextET.setText(String.valueOf(0.0));
                        }
                        else
                        {
                            total_editTextET.setText(String.valueOf(total_price));
                        }

                    }
                }else{
                    total_editTextET.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void toast(String msg)
    {
        Toast.makeText(OrderListActivity.this,msg,Toast.LENGTH_SHORT).show();
    }




}
