package com.sysnova.kfg_sales_appv903.Activity;

import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import com.android.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sysnova.kfg_sales_appv903.modal.OrderLineModel;
import com.sysnova.kfg_sales_appv903.adapter.OrderLineAdapater;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.sysnova.kfg_sales_appv10.R;

/*
 Created by Shanzida
 */
public class ProductOrderLine extends AppCompatActivity {
RecyclerView recyclerView;
List<OrderLineModel> model;
    Dialog dialog;
    EditText Remarks_editText; // numberET;
    EditText latitudeEditText, longitudeEditText,editTextLocationNew;
    Location location;
    Button okButton,noButton;
    String customerName_Code="";
    public static String docNo;
    DatabaseHelper mDatabaseHelper;
    TextView qtySum,toatlSum;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String newdate = sdf.format(System.currentTimeMillis());
    int id1=0;
    public static Integer ids1=0;
    public static final String myNumPref2 = "number2" ;
    SharedPreferences NumPreference2;

    String cdate="";
    public static String TrxType="";

    String str_truck = (OrgBranchSelection.truckno.replace(",", "|")).replace("_", "-");
    String str3_bpname = (OrgBranchSelection.selectedBPCustomerName.replace(",", "|")).replace("_", "-");
    String str2_ProdActivity = (OrgBranchSelection.selectedProdActivityName.replace(",", "|")).replace("_", "-");
    String str4_PriceListName = (OrgBranchSelection.selectedPriceListName.replace(",", "|")).replace("_", "-");
    String str5_activityName = (OrgBranchSelection.selectedactivityName.replace(",", "|")).replace("_", "-");

    //String documentNo= LoginActivity.employeeid+strLong;

    GPSTracker gps;
    //2022-08-20
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude_20;
    double latitude_20;
    String userCountry, userAddress;

    //2022-08-20

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_line);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        getSupportActionBar().setTitle("Product Order Line");
//toast(documentNo);
        //status bar
        if (Build.VERSION.SDK_INT >= 23)
        {
            Window window = ProductOrderLine.this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(ProductOrderLine.this,R.color.grey));}


        mDatabaseHelper=new DatabaseHelper(ProductOrderLine.this);
        model=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductOrderLine.this);
        recyclerView.setLayoutManager(layoutManager);
        //getList();

        docNo=getIntent().getStringExtra("DOCNO");
        customerName_Code=getIntent().getStringExtra("CUSTOMERNAMECODE");
        TrxType=getIntent().getStringExtra("TrxType");//Edit 2024-11-05

        dialog = new Dialog(ProductOrderLine.this);
        dialog.setContentView(R.layout.confirm_order_dialog_box_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        noButton = (Button)dialog.findViewById(R.id.no);
        okButton=(Button)dialog.findViewById(R.id.ok);
        dialog.setCancelable(false);

        //numberET=(EditText)findViewById(R.id.numET);
        Remarks_editText=(EditText)findViewById(R.id.Remarks_editText);

        latitudeEditText=(EditText)findViewById(R.id.loc_lat);
        longitudeEditText=(EditText)findViewById(R.id.loc_long);
        editTextLocationNew=(EditText)findViewById(R.id.editTextNewLocation);

        qtySum=(TextView)findViewById(R.id.listQtySum);
        toatlSum=(TextView)findViewById(R.id.listTotalSum);

        toatlSum.setText("");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        //turnGPSOn();
        loadProductOrderLine();
        //OngetLocationButtonClick();
        //Check GPS Status true/false 2022-08-16

        NumPreference2 = getSharedPreferences(myNumPref2, Context.MODE_PRIVATE);
        Log.d("DATE",newdate );
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //2022-08-20 added by Shanzida

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps = new GPSTracker(ProductOrderLine.this);
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (network_loc != null) {
            final_loc = network_loc;
            latitude_20 = final_loc.getLatitude();
            longitude_20 = final_loc.getLongitude();
        }
        else if (gps != null) {
            //final_loc = gps;
            latitude_20 = gps.getLatitude();
            longitude_20 = gps.getLongitude();
        }
        else if (location != null) {
            final_loc = location;
            latitude_20 = final_loc.getLatitude();
            longitude_20 = final_loc.getLongitude();
        }
        else {
            //OngetLocationButtonClick();
            latitude_20 = 0.0;
            longitude_20 = 0.0;
        }

        latitudeEditText.setText(String.valueOf(latitude_20));
        longitudeEditText.setText(String.valueOf(longitude_20));
        //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude_20, longitude_20, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                //userAddress = addresses.get(0).getAddressLine(0);
                editTextLocationNew.setText(userAddress);
            }
            else {
                userCountry = "Unknown";
                editTextLocationNew.setText(userCountry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //2022-08-20
    }

    private void OngetLocationButtonClick() {
        gps = new GPSTracker(ProductOrderLine.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            //checkPermission();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if(latitude<=0){
                toastMessage("Latitude and longitude not found.");
                //ShowAlertAll("Warning","Please Allow GPS/Location..");
                //return;
            }
            latitudeEditText.setText(String.valueOf(latitude));
            longitudeEditText.setText(String.valueOf(longitude));

            /*----------to get City-Name from coordinates ------------- */
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(ProductOrderLine.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    editTextLocationNew.setText(address); // + " " + city + " " + country);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //savealert(); //2022-09-14

        } else {
            gps.showSettingsAlert();
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            //double ItemName = intent.getDoubleExtra("TOTAL",0.0);
            int itemName2=intent.getIntExtra("TOTALQTY",0);
            //toatlSum.setText(String.valueOf(ItemName));
            toatlSum.setText(String.valueOf(itemName2));
            qtySum.setText(String.valueOf(itemName2));

           // Toast.makeText(ProductOrderLine.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();
        }
    };

    private void loadProductOrderLine(){
        //2024-11-05 added by shanzida for Edit Check docNo
        Cursor data = null;
        if (TrxType != null && TrxType.equals("Edit"))  {
            data = mDatabaseHelper.getProductOrderLineEdit(docNo);
        }
        else{
            data = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
        }
        //2024-11-05

        //Cursor data = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
        if(data.moveToFirst()){
            do{
                // int p_key = data.getInt(0);
                int id= data.getInt(0);
                String code=data.getString(1);
                String name=data.getString(2);
                int qty=data.getInt(3);
                double price=data.getDouble(4);
                double total=data.getDouble(5);
                OrderLineModel orderListModal = new OrderLineModel(id,code,name,qty,price,total);
                model.add(orderListModal);
            }while(data.moveToNext());
        }
        if(model == null || model.isEmpty()){
            toast("No Order Found");
        }else{
            initRecyclerView();
            //double t=0.0;
            //t=getTotal();
            double Tqty=0.0;
            Tqty=getTotalQty();
            qtySum.setText(String.valueOf(Tqty));
            toatlSum.setText(String.valueOf(Tqty));

           //toatlSum.setText(String.valueOf(t));

        }

    }
    void toast(String msg)
    {
        Toast.makeText(ProductOrderLine.this,msg,Toast.LENGTH_SHORT).show();
    }
    private void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductOrderLine.this);
        recyclerView.setLayoutManager(layoutManager);
        OrderLineAdapater adapter = new OrderLineAdapater(model,ProductOrderLine.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //getTotal();


    }
    public double getTotal()
    {
        double total=0.0;
        for(int i=0;i<model.size();i++)
        {
            total=total+model.get(i).getTotal();
          //  System.out.println("Total@@@@@@@@@@@@@@@"+total);
            //Log.d("TOTAL",String.valueOf(i+" "+total));
        }
        return total;
    }
    public double getTotalQty()
    {
        double total=0;
        for(int i=0;i<model.size();i++)
        {
            total=total+model.get(i).getQty();
           // System.out.println("Total@@@@@@@@@@@@@@@"+total);
            //Log.d("TOTAL",String.valueOf(i+" "+total));
        }
        return total;
    }


    public void SendToServerTemp(View view) {
        OngetLocationButtonClick();
        if (TrxType != null && TrxType.equals("Edit")) {
            savealertEdit();
        }else{
            savealert();
        }
    }
    public void callAddMoreProducts(View view) {

        Intent i=new Intent(ProductOrderLine.this, Sales_Order_Activity1.class);
        i.putExtra("documentNumber", docNo);
        i.putExtra("TrxType", TrxType);
        i.putExtra("CUSTOMERNAMECODE", customerName_Code);
        startActivity(i);
    }
    public void callCancel(View view) {
        //databaseHelper.deleteAllProductOrderLine();
        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
        Intent i=new Intent(ProductOrderLine.this, Sales_Order_Activity1.class);
        startActivity(i);
    }
    public void callCancelLogOut(View view) {
        //cancelalert();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductOrderLine.this);
        builder1.setMessage("Are you sure you want to LogOut?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                        Intent i=new Intent(ProductOrderLine.this,LoginActivity.class);
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

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductOrderLine.this);
        builder1.setMessage("Are you sure you want to LogOut?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                        Intent i=new Intent(ProductOrderLine.this,LoginActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
        Intent i=new Intent(ProductOrderLine.this, Sales_Order_Activity1.class);
        i.putExtra("DOCNO",docNo);
        i.putExtra("CUSTOMERNAMECODE",customerName_Code);
        startActivity(i);
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
    public void savealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Order Send to ERP");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        boolean r = false;
                        String[] sourceSplit= (OrgBranchSelection.showdateNew.replace(" ", "")).split("-");
                        int yyyy= Integer.parseInt(sourceSplit[2]);
                        int mm= Integer.parseInt(sourceSplit[1]);
                        int dd= Integer.parseInt(sourceSplit[0]);
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.set(yyyy,mm-1,dd);
                        Date data1= calendar.getTime();
                        cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data1);
                        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                        int id2 =sh.getInt("NUM",0);
                        id1=id1+id2+1;
                        ids1=id1;

                        String total =toatlSum.getText().toString();
                        String str1 =((Remarks_editText.getText().toString().replace(",", "|")).replace("_", "-")); //.setTypeface(Typeface.createFromAsset(Context.getAssets(), "fonts/Durga.ttf"));
                        String GEOstr = (editTextLocationNew.getText().toString().replace(",", "|")).replace("_", "-");

                        /*r = mDatabaseHelper.insertIntoSalesOrder(id1, OrgBranchSelection.selectedOrgId, OrgBranchSelection.selectedWHId, OrgBranchSelection.selectedBPCustomerID, LoginActivity.ad_user_id,str5_activityName,total,cdate,docNo,
                                OrgBranchSelection.selectedDistrictId, OrgBranchSelection.selectedLocationId,str1, OrgBranchSelection.selectedactivityID,str2_ProdActivity,str3_bpname,str4_PriceListName,str_truck
                                ,latitudeEditText.getText().toString(), longitudeEditText.getText().toString(),GEOstr);*/
                        //2022-10-23
                        if(OrgBranchSelection.selectedProdActivityName.equals("Feed")) {
                            r = mDatabaseHelper.insertIntoSalesOrder(id1, LoginActivity.feed_org_id, OrgBranchSelection.selectedWHId, OrgBranchSelection.selectedBPCustomerID, LoginActivity.ad_user_id,str5_activityName,total,cdate,docNo,
                                    OrgBranchSelection.selectedDistrictId, OrgBranchSelection.selectedLocationId,str1, OrgBranchSelection.selectedactivityID,str2_ProdActivity,str3_bpname,str4_PriceListName,str_truck
                                    ,latitudeEditText.getText().toString(), longitudeEditText.getText().toString(),GEOstr);

                        }
                        else {
                            r = mDatabaseHelper.insertIntoSalesOrder(id1, OrgBranchSelection.selectedOrgId, OrgBranchSelection.selectedWHId, OrgBranchSelection.selectedBPCustomerID, LoginActivity.ad_user_id, str5_activityName, total, cdate, docNo,
                                    OrgBranchSelection.selectedDistrictId, OrgBranchSelection.selectedLocationId, str1, OrgBranchSelection.selectedactivityID, str2_ProdActivity, str3_bpname, str4_PriceListName, str_truck
                                    , latitudeEditText.getText().toString(), longitudeEditText.getText().toString(), GEOstr);
                        }
                        //2022-10-23
                        if (r) {
                            boolean rl = false;
                            Cursor dataorder = mDatabaseHelper.getSalesOrderID(docNo); //getSalesOrderLineDataTemp(docNo);
                            String replacedProductname="";
                            int orderid=0;
                            while (dataorder.moveToNext()) {orderid = dataorder.getInt(0);}

                            Cursor dataorderLine = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
                            while (dataorderLine.moveToNext()) {

                                String productName = dataorderLine.getString(2);
                                replacedProductname=productName.replaceAll("_","-");
                                String productQTY = String.valueOf(dataorderLine.getInt(3));
                                String unit_price  = String.valueOf(dataorderLine.getDouble(4));
                                String total1=String.valueOf(dataorderLine.getDouble(5));
                                String mproduct_id=String.valueOf(dataorderLine.getString(7));

                                rl = mDatabaseHelper.insertIntoSalesOrderLine(docNo, OrgBranchSelection.selectedOrgId,replacedProductname,mproduct_id,productQTY,"PIECE",unit_price,orderid);

                                if (rl) {
                                    //mDatabaseHelper.deleteProductOrderHeadNew();
                                    //mDatabaseHelper.deleteAllProductOrderLine();
                                    //ShowAlert1("VPN Not Connected.Temporary Data Saved.");
                                    //toastMessage("Temporary Data Saved");
                                }
                                else {
                                    //toast("Not Saved");
                                    toastMessage("Temporary Data Not Saved");
                                    //ShowAlert1("Temporary Data Not Saved.");
                                }
                            }
                        }
                        else {
                            //toast("Not Saved");
                            toastMessage("Temporary Data Not Saved");
                            //ShowAlert("Temporary Data Not Saved.");
                        }
                        boolean vpn=internetIsConnected();
                        if (!vpn){
                            mDatabaseHelper.deleteProductOrderHeadNew();
                            mDatabaseHelper.deleteAllProductOrderLine();
                            //ShowAlert1("Temporary Data Saved."); //VPN Not Connected.
                            ShowAlert1("Could not send to ERP, Temporary Saved in Mobile");
                        }else {
                            AsyncSendToServer asyncSendToServer=new AsyncSendToServer();
                            asyncSendToServer.execute();
                        }

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
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        // String result = "";
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            boolean isActive=true;

            Cursor check = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
            String Remarks= Remarks_editText.getText().toString(); //2021-11-20
            //databaseHelper.updateOrderDes(docNo, Remarks);
            if(check.moveToNext()) {

            } else {
                return "noOrderLine";
            }
            try {


                ArrayList salesHeader=new ArrayList();

                String customer_code=customerName_Code;
                String[] sourceSplit= (OrgBranchSelection.showdateNew.replace(" ", "")).split("-");
                int yyyy= Integer.parseInt(sourceSplit[2]);
                int mm= Integer.parseInt(sourceSplit[1]);
                int dd= Integer.parseInt(sourceSplit[0]);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(yyyy,mm-1,dd);
                Date data1= calendar.getTime();
                cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data1);

                Vector headerLines=new Vector();
                if(OrgBranchSelection.selectedOrgId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    //headerLines.add(OrgBranchSelection.selectedOrgId);
                    //2022-10-23
                    if(OrgBranchSelection.selectedProdActivityName.equals("Feed")){
                        headerLines.add(LoginActivity.feed_org_id);
                    }
                    else{
                        headerLines.add(OrgBranchSelection.selectedOrgId);
                    }
                    ////2022-10-23
                }
                if(OrgBranchSelection.selectedWHId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedWHId);
                }
                headerLines.add(docNo);
                headerLines.add(LoginActivity.ad_user_id);

                //String str5 = (selectedactivityName.replace(",", "|")).replace("_", "-");
                headerLines.add(str5_activityName);
                if(OrgBranchSelection.selectedBPCustomerID.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedBPCustomerID);
                }
                headerLines.add(toatlSum.getText().toString());
                String str1 =((Remarks_editText.getText().toString().replace(",", "|")).replace("_", "-"));
                headerLines.add(str1);
                headerLines.add(OrgBranchSelection.selectedactivityID);
                headerLines.add(cdate);
                //String str = (truckno.replace(",", "|")).replace("_", "-");
                headerLines.add(str_truck);
                //String str2 = (OrgBranchSelection.selectedProdActivityName.replace(",", "|")).replace("_", "-");
                headerLines.add(str2_ProdActivity);
                if(OrgBranchSelection.selectedDistrictId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedDistrictId);
                }
                if(OrgBranchSelection.selectedLocationId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedLocationId);
                }
                //String str3 = (selectedBPCustomerName.replace(",", "|")).replace("_", "-");
                headerLines.add(str3_bpname);
                //String str4 = (selectedPriceListName.replace(",", "|")).replace("_", "-");
                headerLines.add(str4_PriceListName);

                headerLines.add(latitudeEditText.getText().toString());
                headerLines.add(longitudeEditText.getText().toString());
                String GEOstr = (editTextLocationNew.getText().toString().replace(",", "|")).replace("_", "-");
                headerLines.add(GEOstr);
                headerLines.add(LoginActivity.employeeid+"|"+LoginActivity.Version); //2022-10-23.
                headerLines.add(LoginActivity.userbp_id); //2022-10-26.

                salesHeader.add(headerLines);

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;
                    }
                }
                Cursor dataorderLine = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
                String replacedProductname="";

                ArrayList salesOrder=new ArrayList();
                while (dataorderLine.moveToNext()) {

                    String productName = dataorderLine.getString(2);
                    replacedProductname=productName.replaceAll("_","-");
                    String productQTY = String.valueOf(dataorderLine.getInt(3));
                    String unit_price  = String.valueOf(dataorderLine.getDouble(4));
                    String total=String.valueOf(dataorderLine.getDouble(5));
                    String mproduct_id=String.valueOf(dataorderLine.getString(7));

                    Vector orderLines=new Vector();
                    if(OrgBranchSelection.selectedOrgId.isEmpty()) {
                        orderLines.add("0");
                    }
                    else {
                        //orderLines.add(OrgBranchSelection.selectedOrgId);
                        //2022-10-23
                        if(OrgBranchSelection.selectedProdActivityName.equals("Feed")){
                            orderLines.add(LoginActivity.feed_org_id);
                        }
                        else{
                            orderLines.add(OrgBranchSelection.selectedOrgId);
                        }
                        ////2022-10-23
                    }
                    orderLines.add(replacedProductname);
                    orderLines.add(mproduct_id);
                    orderLines.add(String.valueOf(productQTY));
                    orderLines.add("KG");
                    orderLines.add(String.valueOf(unit_price));

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
                } else {
                    mDatabaseHelper.deleteProductOrderHeadNew();
                    mDatabaseHelper.deleteAllProductOrderLine();
                    mDatabaseHelper.deleteSalesOrderDocno(docNo);
                    mDatabaseHelper.deleteSalesOrderLineDocno(docNo);
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
        protected void onPostExecute(String s) throws NullPointerException {
            super.onPostExecute(s);
            if(s.equals("Success")){
                mDatabaseHelper.deleteProductOrderHeadNew();
                mDatabaseHelper.deleteAllProductOrderLine();
                mDatabaseHelper.deleteSalesOrderDocno(docNo);
                mDatabaseHelper.deleteSalesOrderLineDocno(docNo);

                ShowAlert1("Order Placed Successfully.");

            }else if(s.equals("noOrderLine")){
                ShowAlert1("No order line exits.");
            }
            else if(s.equals("Already Order Place")){
                mDatabaseHelper.deleteProductOrderHeadNew();
                mDatabaseHelper.deleteAllProductOrderLine();
                mDatabaseHelper.deleteSalesOrderDocno(docNo);
                mDatabaseHelper.deleteSalesOrderLineDocno(docNo);
                ShowAlert1("Already Order in ERP.");
            }
            else{
                mDatabaseHelper.deleteProductOrderHeadNew();
                mDatabaseHelper.deleteAllProductOrderLine();
                ShowAlert1("Temporary Data Saved."); //Server Connection Failed.
                /*boolean r = false;
                SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                int id2 =sh.getInt("NUM",0);
                //id1=ids1+id1+id2+1;
                id1=id1+id2+1;
                ids1=id1;

                String total =toatlSum.getText().toString();
                String str = (OrgBranchSelection.truckno.replace(",", "|")).replace("_", "-");
                String str3 = (OrgBranchSelection.selectedBPCustomerName.replace(",", "|")).replace("_", "-");
                String str1 =((Remarks_editText.getText().toString().replace(",", "|")).replace("_", "-")); //.setTypeface(Typeface.createFromAsset(Context.getAssets(), "fonts/Durga.ttf"));
                String GEOstr = (editTextLocationNew.getText().toString().replace(",", "|")).replace("_", "-");

                r = mDatabaseHelper.insertIntoSalesOrder(id1, OrgBranchSelection.selectedOrgId, OrgBranchSelection.selectedWHId, OrgBranchSelection.selectedBPCustomerID, LoginActivity.ad_user_id, OrgBranchSelection.selectedactivityName,total,cdate,docNo,
                        OrgBranchSelection.selectedDistrictId, OrgBranchSelection.selectedLocationId,str1, OrgBranchSelection.selectedactivityID, OrgBranchSelection.selectedProdActivityName,str3, OrgBranchSelection.selectedPriceListName,str
                        ,latitudeEditText.getText().toString(), longitudeEditText.getText().toString(),GEOstr);

                if (r) {
                    boolean rl = false;
                    Cursor dataorder = mDatabaseHelper.getSalesOrderID(docNo); //getSalesOrderLineDataTemp(docNo);
                    String replacedProductname="";
                    int orderid=0;
                    while (dataorder.moveToNext()) {orderid = dataorder.getInt(0);}

                    Cursor dataorderLine = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
                    while (dataorderLine.moveToNext()) {

                        String productName = dataorderLine.getString(2);
                        replacedProductname=productName.replaceAll("_","-");
                        String productQTY = String.valueOf(dataorderLine.getInt(3));
                        String unit_price  = String.valueOf(dataorderLine.getDouble(4));
                        String total1=String.valueOf(dataorderLine.getDouble(5));
                        String mproduct_id=String.valueOf(dataorderLine.getString(7));

                        rl = mDatabaseHelper.insertIntoSalesOrderLine(docNo, OrgBranchSelection.selectedOrgId,replacedProductname,mproduct_id,productQTY,"PIECE",unit_price,orderid);

                        if (rl) {
                            mDatabaseHelper.deleteProductOrderHeadNew();
                            mDatabaseHelper.deleteAllProductOrderLine();
                            ShowAlert1("Server Connection Failed.Temporary Data Saved.");

                        }
                        else {
                            //toast("Not Saved");
                            ShowAlert("Server Connection Failed.Temporary Data Not Saved.");
                        }
                    }
                }
                else {
                    //toast("Not Saved");
                    ShowAlert("Server Connection Failed.Temporary Data Not Saved.");
                }*/
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
    void toastMessage(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void ShowAlert1(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        //builder1.setPositiveButton("OK", null);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                Intent i=new Intent(ProductOrderLine.this, SelectionAcitvity.class);
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

    public void ShowAlert(String msg){
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
    //2024-08-15
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save RecyclerView scroll position
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt("recyclerScrollPosition", scrollPosition);
        // Save the content of EditTexts
        outState.putString("remarks", Remarks_editText.getText().toString());
        outState.putString("latitude", latitudeEditText.getText().toString());
        outState.putString("longitude", longitudeEditText.getText().toString());
        outState.putString("newLocation", editTextLocationNew.getText().toString());

        // Save the TextView content (if necessary)
        outState.putString("qtySum", qtySum.getText().toString());
        outState.putString("totalSum", toatlSum.getText().toString());
        // Save other important data like document number and customer name/code
        outState.putString("docNo", docNo);
        outState.putString("customerName_Code", customerName_Code);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore RecyclerView scroll position
        int scrollPosition = savedInstanceState.getInt("recyclerScrollPosition");
        recyclerView.scrollToPosition(scrollPosition);
        // Restore the content of EditTexts
        Remarks_editText.setText(savedInstanceState.getString("remarks"));
        latitudeEditText.setText(savedInstanceState.getString("latitude"));
        longitudeEditText.setText(savedInstanceState.getString("longitude"));
        editTextLocationNew.setText(savedInstanceState.getString("newLocation"));

        // Restore the TextView content (if necessary)
        qtySum.setText(savedInstanceState.getString("qtySum"));
        toatlSum.setText(savedInstanceState.getString("totalSum"));

        // Restore other important data like document number and customer name/code
        docNo = savedInstanceState.getString("docNo");
        customerName_Code = savedInstanceState.getString("customerName_Code");
    }
    //2024-08-15
    //2024-11-10 added for edit data update save
    private final class AsyncSendToServerForEdit extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            boolean isActive=true;
            //Cursor check = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id);
            Cursor check = mDatabaseHelper.getProductOrderLineEdit(docNo);
            String Remarks= Remarks_editText.getText().toString(); //2021-11-20
            if(!Remarks.equals("null") && Remarks!= null && !Remarks.isEmpty()) {
                //databaseHelper.updateOrderDes(docNo, Remarks);
            }
            if (!check.moveToNext()) {
                return "noOrderLine";
            }
            try {
                ArrayList salesHeader=new ArrayList();
                String customer_code=customerName_Code;
                String[] sourceSplit= (OrgBranchSelection.showdateNew.replace(" ", "")).split("-");
                int yyyy= Integer.parseInt(sourceSplit[2]);
                int mm= Integer.parseInt(sourceSplit[1]);
                int dd= Integer.parseInt(sourceSplit[0]);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.set(yyyy,mm-1,dd);
                Date data1= calendar.getTime();
                cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data1);

                Vector headerLines=new Vector();
                if(OrgBranchSelection.selectedOrgId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    if(OrgBranchSelection.selectedProdActivityName.equals("Feed")){
                        headerLines.add(LoginActivity.feed_org_id);
                    }
                    else{
                        headerLines.add(OrgBranchSelection.selectedOrgId);
                    }
                }
                if(OrgBranchSelection.selectedWHId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedWHId);
                }
                headerLines.add(docNo);
                headerLines.add(LoginActivity.ad_user_id);

                //String str5 = (selectedactivityName.replace(",", "|")).replace("_", "-");
                headerLines.add(str5_activityName);
                if(OrgBranchSelection.selectedBPCustomerID.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedBPCustomerID);
                }
                headerLines.add(toatlSum.getText().toString());
                String str1 =((Remarks_editText.getText().toString().replace(",", "|")).replace("_", "-"));
                headerLines.add(str1);
                headerLines.add(OrgBranchSelection.selectedactivityID);
                headerLines.add(cdate);
                headerLines.add(str_truck);
                String str2 = (OrgBranchSelection.selectedProdActivityName.replace(",", "|")).replace("_", "-");
                headerLines.add(str2_ProdActivity);
                if(OrgBranchSelection.selectedDistrictId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedDistrictId);
                }
                if(OrgBranchSelection.selectedLocationId.isEmpty()) {
                    headerLines.add("0");
                }
                else {
                    headerLines.add(OrgBranchSelection.selectedLocationId);
                }
                //String str3 = (selectedBPCustomerName.replace(",", "|")).replace("_", "-");
                headerLines.add(str3_bpname);
                //String str4 = (selectedPriceListName.replace(",", "|")).replace("_", "-");
                headerLines.add(str4_PriceListName);

                headerLines.add(latitudeEditText.getText().toString());
                headerLines.add(longitudeEditText.getText().toString());
                String GEOstr = (editTextLocationNew.getText().toString().replace(",", "|")).replace("_", "-");
                headerLines.add(GEOstr);
                headerLines.add(LoginActivity.employeeid+"|"+LoginActivity.Version);
                headerLines.add(LoginActivity.userbp_id);

                salesHeader.add(headerLines);

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;
                    }
                }
                //Cursor dataorderLine = mDatabaseHelper.getProductOrderLine(LoginActivity.ad_user_id); //getSalesOrderLineDataTemp(docNo);
                Cursor dataorderLine = mDatabaseHelper.getProductOrderLineEdit(docNo);
                String replacedProductname="";
                ArrayList salesOrder=new ArrayList();
                while (dataorderLine.moveToNext()) {

                    String productName = dataorderLine.getString(2);
                    replacedProductname=productName.replaceAll("_","-");
                    String productQTY = String.valueOf(dataorderLine.getInt(3));
                    String unit_price  = String.valueOf(dataorderLine.getDouble(4));
                    String total=String.valueOf(dataorderLine.getDouble(5));
                    String mproduct_id=String.valueOf(dataorderLine.getString(7));
                    String T_SalesOrderLine_id = String.valueOf(dataorderLine.getString(10));

                    Vector orderLines=new Vector();
                    if(OrgBranchSelection.selectedOrgId.isEmpty()) {
                        orderLines.add("0");
                    }
                    else {
                        if(OrgBranchSelection.selectedProdActivityName.equals("Feed")){
                            orderLines.add(LoginActivity.feed_org_id);
                        }
                        else{
                            orderLines.add(OrgBranchSelection.selectedOrgId);
                        }
                    }
                    orderLines.add(replacedProductname);
                    orderLines.add(mproduct_id);
                    orderLines.add(String.valueOf(productQTY));
                    orderLines.add("KG");
                    orderLines.add(String.valueOf(unit_price));
                    orderLines.add(LoginActivity.ad_user_id);
                    orderLines.add(T_SalesOrderLine_id);

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

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrderEdit")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrderEdit");

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
                } else {
                    mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                    mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
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
                    mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                    mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                    ShowAlert1("Order Updated Successfully.");

                }else if(s.equals("noOrderLine")){
                    ShowAlert1("Not Updated.");
                }
                else{
                    mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                    mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                    ShowAlert1("Not Updated.");
                }
            }catch (NullPointerException ignored){
            }
        }
    }

    public void savealertEdit(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Send Order to ERP System for Update.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AsyncSendToServerForEdit asyncSendToServerForEdit=new AsyncSendToServerForEdit();
                        asyncSendToServerForEdit.execute();
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
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }

}