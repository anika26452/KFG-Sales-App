package com.sysnova.kfg_sales_appv903.Activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.sysnova.kfg_sales_appv10.R;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//import static com.sysnova.kfg_sales_app.Activity.Cutomer_Selection_Activity.selectedSalesType;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.employeeid;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.ad_user_id;
import static com.sysnova.kfg_sales_appv903.Activity.OrgBranchSelection.selectedactivityID;
/*
 Created by Shanzida
 */
public class Sales_Order_Activity1 extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    TextView customerName_New;//addressText,balanceText,nameText,numberText,billingLocationTextView,
    EditText editText_dateTime,qty_editText,price_editText,search_editText,total_editText;
   // Button selectChemistButton;
    //ImageButton datepickerImageButton_1,datePickerButton_2;
    HashMap<String,String> hashUOM  = new HashMap<>();
    HashMap<String,String> hashProductID  = new HashMap<>();
    HashMap<String,String> hashProdID  = new HashMap<>();
    HashMap<String, String> priceHash = new HashMap<>();
    /*HashMap<String, String> searchkeyHash = new HashMap<>();
    ArrayList<String> customerArrayList=new ArrayList<>();
    ArrayList<String> BPGroupArayList=new ArrayList<>();*/
    ArrayList<String> productCategoryArrayList = new ArrayList<String>();
    ArrayList<String> productArrayList = new ArrayList<String>();
    ArrayList<String> salesTypeArrayList=new ArrayList<>();
    //String dateStr;
    //String chemistCode = "",productorderhead_Id="";
    String selectedProductID="",selectedProductPrice="",selectedProduct="";
    public static String selectedProdId="";
    ArrayList<String> intentList;
    String documentNo="";
    String customerName_Code="";
    public static String TrxType="",docNo="";
    List<String> unitArrayList = new ArrayList<>();
    //double Productprice;
    //int quantityOfProduct;
    //BigDecimal total_price1;
    TextView orgTV,unitOfMeasure;
    //String DocumentNo;
    //GPSTracker gps;
    String selectedProductSearchkey="";
    Button buttonAdd,buttonView,buttonSendToServer,buttonGetErpValue;


    Spinner salesTypeSpinner,customerGroupSpinner,CustomerSpinner,ProductCategorySpinner,ProductSpinner;

    String[] permissionsRequired = new String[]{
            //Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order1);
        /**designing code**/
        getSupportActionBar().setTitle("Create Order Line");
        //getSupportActionBar().setSubtitle("Brac Fisheries App");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        //EditText
        customerName_Code=getIntent().getStringExtra("CUSTOMERNAMECODE");
        customerName_New=findViewById(R.id.customernamecode); // customerName_New newly added by shanzida 2021-11-15
        customerName_New.setText(customerName_Code);

        mDatabaseHelper=new DatabaseHelper(Sales_Order_Activity1.this);
        qty_editText=(EditText)findViewById(R.id.editText_quantity);
        price_editText=(EditText)findViewById(R.id.editText_price);
        total_editText=(EditText)findViewById(R.id.total_editText);
        unitOfMeasure=(TextView)findViewById(R.id.uom);
        //search_editText=(EditText)findViewById(R.id.search_customer_editText);
        buttonAdd = (Button) findViewById(R.id.button_Add);
        //buttonView = (Button) findViewById(R.id.button_View);
        boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
        //2022-09-15 isDarkThemeOn added by Shanzida
        //2022-09-16
        if(isDarkThemeOn==true){
            qty_editText.setTextColor(Color.parseColor("#FFFFFF"));
            price_editText.setTextColor(Color.parseColor("#FFFFFF"));
            total_editText.setTextColor(Color.parseColor("#FFFFFF"));
            unitOfMeasure.setTextColor(Color.parseColor("#FFFFFF"));
            //setTextColor("#0b79bf");
        }
        //2022-09-16

        long now = System.currentTimeMillis();
        String strLong = Long.toString(now);
        documentNo = employeeid + strLong;
        ProductCategorySpinner=findViewById(R.id.product_category_spinner);
        ProductSpinner=findViewById(R.id.product_spinner);
        docNo=getIntent().getStringExtra("documentNumber");
        TrxType=getIntent().getStringExtra("TrxType");//Edit 2024-11-05
       // loadProductCategorySpinner();
        //loadProductSpinner();
        onQuantityEditTextFocusChange();
        getProductCategory();
        //OnAddButtonPress();
        //getProduct();
        //2024-08-14
        if (savedInstanceState != null) {
            // Restore data
            customerName_New.setText(savedInstanceState.getString("customer"));
            price_editText.setText(savedInstanceState.getString("price"));
            qty_editText.setText(savedInstanceState.getString("qty"));
            total_editText.setText(savedInstanceState.getString("total"));
            unitOfMeasure.setText(savedInstanceState.getString("uom"));

            ProductSpinner.setSelection(savedInstanceState.getInt("prod"));
            ProductCategorySpinner.setSelection(savedInstanceState.getInt("prodCat"));
        }
        //2024-08-14
    }
    public void callConfirmNew(View view) {
        callAddProduct();
    }

    public void callCancel(View view) {
        Intent i=new Intent(Sales_Order_Activity1.this,OrgBranchSelection.class);
        startActivity(i);

    }

    public void callAddProduct( ) {

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        int id =sh.getInt("NUM",0);
        int id1=id+1;
        String quantity="0";
        //Integer qty=0;
        double qty=0.00;
        quantity=(qty_editText.getText().toString());
        //int qty=Integer.parseInt(quantity);
        //qty  = Integer.parseInt(quantity);

        if (ProductCategorySpinner.getCount()==0){
            toastMessage("No Product Category Selected !!");
            return;
        }
        if (ProductSpinner.getCount()==0){
            toastMessage("No Product Selected !!");
            return;
        }
        //if(!qty_editText.getText().toString().equals("null") && qty_editText.getText().toString()!= null && !qty_editText.getText().toString().isEmpty())
        if (TextUtils.isEmpty(qty_editText.getText().toString()) || qty_editText.getText().toString().equals(""))
        {
            qty_editText.requestFocus();
            qty_editText.setError("Please Enter Quantity ");
            return;
        }

        if (qty_editText.getText().toString().equals("0") || qty_editText.getText().toString().equals("")){
            //toastMessage("Please Enter Quantity !!");
            qty_editText.requestFocus();
            qty_editText.setError("Please Enter Quantity !!");
            return;
        }
        /*if (qty_editText.getText().toString().equals(".") ){
            toastMessage("Please Enter Quantity !!");
            return;}*/

        //2022-09-20
        Cursor cursor1 = mDatabaseHelper.getProductOrderLineProduct(ad_user_id,selectedProdId);
        if (!cursor1.moveToFirst()) {

            boolean result=false;
           // qty  = Integer.parseInt(quantity);
            qty= Double.parseDouble(quantity);
            if (TrxType != null && TrxType.equals("Edit")) {
                result=mDatabaseHelper.insertIntoProductOrderLineEdit(id1,"1000911",selectedProduct,qty,qty,qty,docNo,selectedProdId,ad_user_id,TrxType,"0"); //,productorderhead_Id);

            }else{
                result=mDatabaseHelper.insertIntoProductOrderLine(id1,"1000911",selectedProduct,qty,qty,qty,documentNo,selectedProdId,ad_user_id); //,productorderhead_Id);

            }
            //result=mDatabaseHelper.insertIntoProductOrderLine(id1,"1000911",selectedProduct,qty,qty,qty,documentNo,selectedProdId,ad_user_id); //,productorderhead_Id);

            if(result)
            {
                toast("Product Inserted");
                SharedPreferences.Editor editor = sh.edit();
                editor.putInt("NUM",id1);
                editor.commit();
                if (TrxType != null && TrxType.equals("Edit")){
                    Intent i = new Intent(Sales_Order_Activity1.this, ProductOrderLine.class);
                    i.putExtra("DOCNO", docNo);
                    i.putExtra("TrxType", TrxType);
                    i.putExtra("CUSTOMERNAMECODE", customerName_Code);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Sales_Order_Activity1.this, ProductOrderLine.class);
                    i.putExtra("DOCNO", documentNo);
                    i.putExtra("CUSTOMERNAMECODE", customerName_Code);
                    startActivity(i);
                }

            }
            else {
                toast("Could not insert into internal database");
            }
        }
        else{
            ShowAlert("Warning", "This Product is already added.Please Add or Remove Qty from Product list");
        }
        //2022-09-20
    }
    //2022-08-16
    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            int ll=grantResults.length;
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
               // initializeFragments();
            } else {
                //openAlertDialog();
               // ShowAlert("Warning","Please Allow GPS/Location..");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void openAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app requires your location to function!");
        alertDialogBuilder.setPositiveButton("Try again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        checkForPermissions();
                    }
                });

        alertDialogBuilder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //2022-08-16
    public void callShowAddProduct(View view){
        //boolean result=false;
        Cursor cursor = mDatabaseHelper.getProductOrderLine(ad_user_id);
        if (cursor.moveToFirst()) {
            Intent i=new Intent(Sales_Order_Activity1.this, ProductOrderLine.class);
            i.putExtra("DOCNO",documentNo);
            i.putExtra("CUSTOMERNAMECODE",customerName_Code);
            startActivity(i);
        }
        else {
            toast("No Product is Added");
        }
    }

    public void onClickDatePicker(View view) {

        final Calendar c = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //dateStr = df.format(c.getTime());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        @SuppressLint("WrongViewCast") DatePickerDialog datePicker = new DatePickerDialog(findViewById(R.id.date_editText).getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                //   daynum = (TextView) getActivity().findViewById(R.id.daynum);
                // editText_dateTime.setText(df.format(view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth()));
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String d2 = view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, (month));
                c.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                editText_dateTime.setText(sdf.format(c.getTime()));

                //dobEditText.setEnabled(false);
                //  new ReadJSON().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
                //  new ReadJSON1().execute("url" + "day=" + view.getDayOfMonth() + "&" + "month=" + (view.getMonth() + 1) + "&" + "year=" + view.getYear());
            }
        }, day, month, year);
        //subtract -3000 to select date before current date
        // c.add(Calendar.YEAR, -2);
        datePicker.getDatePicker().setSpinnersShown(true);
        datePicker.getDatePicker().setCalendarViewShown(false);
        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.YEAR, 4);
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePicker.setTitle("Select Date");
        datePicker.show();
    }


    private void getProductCategory()
    {
        String ConditionAct="";
        ConditionAct="(C_Activity_ID='"+selectedactivityID+"' or catagoryID='1')";
        Cursor cursor = mDatabaseHelper.getProductCategoryCursor(ConditionAct);
        if(cursor.moveToFirst()){
            do{
                String   id   = cursor.getString(0);
                String   name   = cursor.getString(1);
                productCategoryArrayList.add(name);
                hashProductID.put(name,id);

            }while(cursor.moveToNext());
        }
        loadProductCategorySpinner();

    }
    private void getProduct()
    {
        hashProdID.clear();
        if(!productArrayList.isEmpty())
        {
            productArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCursor(selectedProductID);

        if(cursor.moveToFirst()){
            do{
                //String   name   = cursor.getString(1);
               // String orgName          = cursor.getString(2);
                String   id   = cursor.getString(0);
                String   name   = cursor.getString(1);
                productArrayList.add(name);
                hashProdID.put(name,id);
                Log.d("DEBUG","prroductArrayList = "+name);

            }while(cursor.moveToNext());
        }
        loadProductSpinner();

    }

    private void loadProductCategorySpinner()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productCategoryArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ProductCategorySpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectProductCategorySpinner();
    }
    private void loadProductSpinner()
    {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ProductSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectProductSpinner();
    }
    private void onQuantityEditTextFocusChange(){
        qty_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!price_editText.getText().toString().equals("") && !qty_editText.getText().toString().equals("")){
                    if(!price_editText.getText().toString().equals(".") && !qty_editText.getText().toString().equals(".")) {
                        //double price = Double.parseDouble(price_editText.getText().toString());
                       // double quantity = Double.parseDouble(qty_editText.getText().toString());
                        //double total_price = price * quantity;
                        BigDecimal price = new BigDecimal(price_editText.getText().toString());
                        BigDecimal quantity = new BigDecimal(qty_editText.getText().toString());
                        BigDecimal total_price = price.multiply(quantity);
                        total_editText.setText(String.valueOf(total_price));
                    }
                }else{
                    total_editText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        price_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!qty_editText.getText().toString().equals("") && !price_editText.getText().toString().equals("")){
                    if(!qty_editText.getText().toString().equals(".") && !price_editText.getText().toString().equals(".")) {
                        //double price = Double.parseDouble(price_editText.getText().toString());
                        //double quantity = Double.parseDouble(qty_editText.getText().toString());
                        BigDecimal price = new BigDecimal(price_editText.getText().toString());
                        BigDecimal quantity = new BigDecimal(qty_editText.getText().toString());
                        BigDecimal total_price = price.multiply(quantity);
                        //total_price1 = BigDecimal.valueOf(total_price); //2021-11-17  total_price1 added by shanzida
                        //total_price1=new BigDecimal("406770000244E+12").toBigInteger()
                        total_editText.setText(String.valueOf(total_price));
                        //total_editText.setText(String.valueOf(total_price));
                    }
                }else{
                    total_editText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
            a = "Error" + e.getMessage();
            return a;
        }
        return a;

    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void callConfirm(View view) {
        toast("Confirm Order Clicked ");
    }
    void toast(String msg){
        Toast.makeText(Sales_Order_Activity1.this,msg,Toast.LENGTH_SHORT).show();
    }
    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(Sales_Order_Activity1.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public void ShowAlertLoc(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        //builder1.setPositiveButton("OK", null);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                openAlertDialog();
                //checkForPermissions();
            }
        });
        AlertDialog alert11 = builder1.create();
        //this is done in an effort to make the dialog always appear on the topmost screen
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void selectProductCategorySpinner(){
        ProductCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String name = parent.getSelectedItem().toString();
                    selectedProductID = hashProductID.get(name);
                    getProduct();



                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectProductSpinner(){
        ProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String name = parent.getSelectedItem().toString();
                    selectedProductSearchkey=hashProdID.get(name);//searchkeyHash.get(name);
                    selectedProduct=name; //selectedProductSearchkey;
                    selectedProdId=hashProdID.get(name);
                    String selectedProductUOM=hashUOM.get(name);
                    selectedProductPrice = priceHash.get(name);
                    unitOfMeasure.setText(selectedProductUOM);
                    price_editText.setText(selectedProductPrice);

                    price_editText.setEnabled(false);
                    //toastMessage("productname="+name+" price="+selectedProductPrice+"value="+selectedProductSearchkey);



                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void OnViewButtonPress(View view) {
        //get the data and append to a list
        Cursor data = mDatabaseHelper.getSalesOrderLineDataTemp(documentNo);
        if(data.moveToNext()) {
            Intent activityintent = new Intent(Sales_Order_Activity1.this, OrderListActivity.class);
            activityintent.putExtra("DOCNO",documentNo);
            activityintent.putExtra("CUSTOMERNAMECODE",customerName_Code);
            startActivity(activityintent);
            finish();
        }
        else {
            toastMessage("No OrderLine Exists to Show");
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //mDatabaseHelper.deleteSalesOrderLine();
       // toast("Order Cancelled");

        Intent i=new Intent(Sales_Order_Activity1.this,OrgBranchSelection.class);

        startActivity(i);
    }
    public void callCancelLogOut(View view) {
        cancelalert();
        //Intent i=new Intent(ProductOrderLine.this,LoginActivity.class);
        //startActivity(i);
    }

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Sales_Order_Activity1.this);
        builder1.setMessage("Are you sure you want to LogOut?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i=new Intent(Sales_Order_Activity1.this,LoginActivity.class);
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
    //2024-08-14 newly added for save and restore
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save EditText content
        outState.putString("customer", customerName_New.getText().toString());
        outState.putString("price", price_editText.getText().toString());
        outState.putString("qty", qty_editText.getText().toString());
        outState.putString("total", total_editText.getText().toString());
        outState.putString("uom", unitOfMeasure.getText().toString());
        // Save selected spinner values
        outState.putInt("prod", ProductSpinner.getSelectedItemPosition());
        outState.putInt("prodCat", ProductCategorySpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore data
        customerName_New.setText(savedInstanceState.getString("customer"));
        price_editText.setText(savedInstanceState.getString("price"));
        qty_editText.setText(savedInstanceState.getString("qty"));
        total_editText.setText(savedInstanceState.getString("total"));
        unitOfMeasure.setText(savedInstanceState.getString("uom"));

        ProductSpinner.setSelection(savedInstanceState.getInt("prod"));
        ProductCategorySpinner.setSelection(savedInstanceState.getInt("prodCat"));
    }
//2024-08-14
}