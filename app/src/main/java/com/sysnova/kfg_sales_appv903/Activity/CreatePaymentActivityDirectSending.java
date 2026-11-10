package com.sysnova.kfg_sales_appv903.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.sysnova.kfg_sales_appv10.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.ad_user_id;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.currentdate;
import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.isEmployee;
import static com.sysnova.kfg_sales_appv903.Database.PostgreSQL.apiUrl;

/*
 Created by Anika
 */
public class CreatePaymentActivityDirectSending extends AppCompatActivity {
    Spinner activityspinner, orgspinner, dealerspinner, dealerdistrictspinner, dealerbankspinner, thanaspinner, kfgbankspinner;
    EditText paymentedittext, mredittext, dealerbankdit, depositorname, depositorno, billno;
    DatabaseHelper mDatabaseHelper;
    public static String selectedBPName = "",selectedOrgId="",selectedOrgName="",selectedBPCustomerID = "",selectedDealerBankName="",selectedDealerBankId="",selectedKFGBankName="",selectedKFGBankId="",selectedactivityName="",selectedactivityID="", selectedabusinesstypeID="", selectedregionid = "", selectedcityid="", thananame= "";
    String districtname, selectdate = "",styledate = "",dealerloc = "",otherno = "",res = "",supText="", bpid, region_id, paymentamt, mrno, defaultdistrict,selectedbpcode ="", bpcode, bpcustomer, deponame, depono, bill;
    Button browse, savebutton, cancelbtn, trndate;
    TextView filepath;
    Uri uri;
    InputStream iStream = null;
    byte[] inputData;
    Bitmap bitmap;
    ImageView viewimage;
    int checkphoto = 0;
    EditText dealeraddress;
    private DatePickerDialog.OnDateSetListener dateSetListener2;

    HashMap<String, String> hashBPID = new HashMap<>();
    HashMap<String, String> hashBPCode = new HashMap<>();
    HashMap<String, String> hashBPName = new HashMap<>();
    ArrayList<String> customerArrayList = new ArrayList<>();

    HashMap<String, String> hashregionID = new HashMap<>();

    List<String> OrderTypeArrayList = new ArrayList<String>();
    HashMap<String,String> hashOrderTypeId  = new HashMap<>();
    HashMap<String,String> hashbusinessTypeId  = new HashMap<>();

    List<String> orgNameArrayList = new ArrayList<String>();
    HashMap<String,String> hashOrgId  = new HashMap<>();

    HashMap<String, String> hashDealerBankID = new HashMap<>();
    ArrayList<String> DealerBankArrayList = new ArrayList<>();

    HashMap<String, String> hashDistrictid = new HashMap<>();
    ArrayList<String> DistrictArrayList = new ArrayList<>();

    HashMap<String, String> hashKFGBankID = new HashMap<>();
    ArrayList<String> KFGBankArrayList = new ArrayList<>();

    HashMap<String, String> hashThanaID = new HashMap<>();
    ArrayList<String> ThanaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_payment_dirtect_sending);

        getSupportActionBar().setTitle("Create Payment");

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));
        activityspinner = (Spinner)findViewById(R.id.activity_spinner);
        orgspinner = (SearchableSpinner)findViewById(R.id.org_spinner);
        dealerspinner = (SearchableSpinner)findViewById(R.id.dealer_spinner);
        dealerdistrictspinner = (SearchableSpinner)findViewById(R.id.dealerdistrict_spinner);
        thanaspinner = (SearchableSpinner)findViewById(R.id.dealerthana_spinner);
        dealerbankspinner = (SearchableSpinner)findViewById(R.id.dealerbank_spinner);
        kfgbankspinner = (SearchableSpinner)findViewById(R.id.kfg_spinner);
        paymentedittext = (EditText)findViewById(R.id.payment_editText);
        mredittext = (EditText)findViewById(R.id.mr_editText);
        dealerbankdit= (EditText)findViewById(R.id.dealerbankedittext);
        browse = findViewById(R.id.browsebtn);
        filepath = findViewById(R.id.imgfile);
        depositorname = findViewById(R.id.depo_name);
        depositorno = findViewById(R.id.depo_no);
        billno = findViewById(R.id.bill_no);
        viewimage = findViewById(R.id.imageview);
        savebutton = findViewById(R.id.save_button);
        cancelbtn = findViewById(R.id.cancel_button);
        dealeraddress = findViewById(R.id.dealersadd);
        trndate = findViewById(R.id.datefortransaction);
        mDatabaseHelper = new DatabaseHelper(CreatePaymentActivityDirectSending.this);

        boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
        if(isDarkThemeOn==true){
            paymentedittext.setTextColor(Color.parseColor("#FFFFFF"));
            mredittext.setTextColor(Color.parseColor("#FFFFFF"));
            depositorno.setTextColor(Color.parseColor("#FFFFFF"));
            billno.setTextColor(Color.parseColor("#FFFFFF"));
            depositorname.setTextColor(Color.parseColor("#FFFFFF"));
            dealeraddress.setTextColor(Color.parseColor("#FFFFFF"));
        }

        Date dateSql=new Date();
        String strDate = currentdate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateSql = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectdate =currentdate;// new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        styledate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateSql);
        System.out.println(new Date());
        trndate.setText(""+styledate+"");
        Date finalDateSql = dateSql;
        trndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePaymentActivityDirectSending obj = new CreatePaymentActivityDirectSending();
                Calendar calendar;
                calendar = obj.dateToCalendar(finalDateSql);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreatePaymentActivityDirectSending.this,
                        dateSetListener2,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String sdate = dayOfMonth+"/"+month+"/"+year;
                Date date1 = new Date(year-1900, month-1,dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date3= date1;
                //2022-05-12 start
                //Calendar someDate = Calendar.getInstance();
                CreatePaymentActivityDirectSending obj1 = new CreatePaymentActivityDirectSending();
                Calendar someDate= obj1.dateToCalendar(finalDateSql);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                someDate.add(Calendar.DAY_OF_YEAR, -3);
                Date date31 = someDate.getInstance().getTime();
                Date myDate = finalDateSql; //new Date();
                // Date prvDate3 = new Date(myDate.getTime() - 259200000); // 3 * 24 * 60 * 60 * 1000
                Date prvDate3 = new Date(finalDateSql.getTime() - 259200000); // 3 * 24 * 60 * 60 * 1000

                if ((date3.equals(myDate) || date3.before(myDate)) && (date3.after(prvDate3))){
                    selectdate = formatter.format(date1);
                    styledate = formatter1.format((date1));
                    trndate.setText(styledate);
                }else {
                    ShowAlert("Warning", "Transaction Date is not valid. You can Payment only for today and previous 2 days.");
                }
            }
        };
        //end 2022-09-18

        getOrderType();
        getOrg();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(CreatePaymentActivityDirectSending.this)
                        .crop()
                        .compress(300)
                        // .cameraOnly()
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CreatePaymentActivityDirectSending.RESULT_OK) {
            uri = data.getData();
            try {
                iStream = getContentResolver().openInputStream(uri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                viewimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputData = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkphoto = 1;
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void loadDistrictSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DistrictArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        dealerdistrictspinner.setAdapter(dataAdapter);
        if (defaultdistrict != null) {
            int spinnerPosition = dataAdapter.getPosition(defaultdistrict);
            dealerdistrictspinner.setSelection(spinnerPosition);
        }
        dataAdapter.notifyDataSetChanged();
        selectDistrictSpinner();
    }

    private void selectDistrictSpinner(){
        dealerdistrictspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    districtname = parent.getSelectedItem().toString();
                    selectedregionid=hashDistrictid.get(districtname);
                    //   getThanaforPayment(selectedregionid);
                    if(!selectedBPCustomerID.equals("1")) {
                        if(!selectedcityid.equals("1")) {
                            if(!selectedregionid.equals("1")) {
                                //       toastMessage(selectedregionid + "=" + districtname + "=" + selectedcityid + "=" + thananame);

                                AsyncKfgbank asynckfgbank=new AsyncKfgbank();
                                asynckfgbank.execute();
                                //FetchKFGBankACFromServer(selectedregionid, districtname);
                            }
                        }
                    }
                    //
                } catch (NullPointerException ex) {
                    toastMessage(""+ex);

                } catch (ArrayIndexOutOfBoundsException ex) {
                    toastMessage(""+ex);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final class AsyncKfgbank extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            boolean isactive = true;
            try {
                mDatabaseHelper.deletekfgbankacc();
            } catch (Exception e) {
                res = e.getMessage();
                return res;
            }
            try{
                res = FetchKFGBankACFromServer(selectedregionid, districtname);
                //  System.out.println(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CreatePaymentActivityDirectSending.this);
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
                    getkfgBankACSpinner();
                    pd.dismiss();
                }else{
                    System.out.println(s);
                    pd.dismiss();
                }
            }catch (NullPointerException ex){

            }
        }
    }

    private void  getBankName(){
        hashThanaID.clear();
        if(!ThanaArrayList.isEmpty()) {
            ThanaArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getBankName();
        if(cursor.moveToFirst()){
            do{
                String thanaID = cursor.getString(0);
                String thanaName = cursor.getString(1);
                System.out.println(thanaName+"-------------------------"+thanaID);
                ThanaArrayList.add(thanaName);
                hashThanaID.put(thanaName, thanaID);
            }while(cursor.moveToNext());
        }
        loadBankSpinner();
    }

    private void loadBankSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ThanaArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        thanaspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectBankSpinner();
    }

    private void selectBankSpinner(){
        thanaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    thananame = parent.getSelectedItem().toString();
                    selectedcityid=hashThanaID.get(thananame);
                    if(!selectedBPCustomerID.equals("1")) {
                        if(!selectedcityid.equals("1")) {
                            hashDistrictid.clear();
                            DistrictArrayList.clear();
                            loadDistrictSpinner();
                            KFGBankArrayList.clear();
                            hashKFGBankID.clear();
                            loadKFGBankACSpinner();
                            // toastMessage(thananame+"--------------"+selectedcityid);
                            AsyncBranch asyncbranch=new AsyncBranch();
                            asyncbranch.execute();
                            // FetchBranchNameFromServer(selectedcityid);

                        }
                    }
                } catch (NullPointerException ex) {

                } catch (ArrayIndexOutOfBoundsException ex) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final class AsyncBranch extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            boolean isactive = true;
            try {
                mDatabaseHelper.deleteBranchName();
            } catch (Exception e) {
                res = e.getMessage();
                return res;
            }
            try{
                res = FetchBranchNameFromServer(selectedcityid);
                System.out.println(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CreatePaymentActivityDirectSending.this);
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
                    getBranchname();
                    pd.dismiss();
                }else{
                    System.out.println(s);
                    pd.dismiss();
                    savebutton.setEnabled(true);
                }
            }catch (NullPointerException ex){

            }
        }
    }

  /*  private void getdefaultDistrictfromsqlite(String regionid){
        regionid = "1000000";
        Cursor cursor = mDatabaseHelper.getDistrictForPayment(regionid);
        if(cursor.moveToFirst()){
            do{
                String districtid = cursor.getString(0);
                String districtname = cursor.getString(1);
                System.out.println("defaultDistrict = "+districtname+"-------------------------"+districtid);
                defaultdistrict = districtname;
            }while(cursor.moveToNext());
        }
        loadDistrictSpinner(); //ghapla
    }

    private void getDistrictfromsqlite(){
        if(!DistrictArrayList.isEmpty()) {
            DistrictArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getDistrict_Bank();
        if(cursor.moveToFirst()){
            do{
                String districtid = cursor.getString(0);
                String districtname = cursor.getString(1);
                System.out.println("District = "+districtname+"-------------------------"+districtid);
                DistrictArrayList.add(districtname);
                hashDistrictid.put(districtname, districtid);
            }while(cursor.moveToNext());
        }
        loadDistrictSpinner();
    }

   */

    private void getDealer(String businesstypeID) {
        if(!customerArrayList.isEmpty()) {
            customerArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getCustomer(businesstypeID);

        if(cursor.moveToFirst()) {
            do {
                //   bpcode   = cursor.getString(4);
                String bpname = cursor.getString(0);
                //  bpid = cursor.getString(2);
                //    region_id = cursor.getString(7);
                dealerloc = cursor.getString(1);
                bpid = cursor.getString(3);
                bpcode = cursor.getString(5);
                region_id = cursor.getString(8);
                //System.out.println(bpcode+"-------------------------"+bpname+","+bpid+" region = "+region_id);
                customerArrayList.add(bpname);
                hashBPID.put(bpname, bpid);
                hashBPCode.put(bpname, bpcode);
                hashBPName.put(bpid, bpname);
                hashregionID.put(bpname, region_id);
            } while (cursor.moveToNext());
        }
        loadCustomerSpinner();
    }

    private void loadCustomerSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, customerArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dealerspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        if (isEmployee.equals("N")) {
            dealerspinner.setSelection(1);
        }
        selectBPCustomerSpinner();
    }

    private void selectBPCustomerSpinner() {
        dealerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    bpcustomer = parent.getSelectedItem().toString();
                    selectedBPCustomerID=hashBPID.get(bpcustomer);
                    selectedbpcode = hashBPCode.get(bpcustomer);
                    String region_id1=hashregionID.get(bpcustomer); //2022-08-22
                    selectedBPName = hashBPName.get(selectedBPCustomerID);

                    System.out.println(bpcustomer+"!!!!!!!!!!!!!!!!!!!!!!!!!"+selectedBPCustomerID+"!!!!!!!!!!!!!!!!!!!!!!!!!"+selectedBPName );

                    if(!selectedBPCustomerID.equals("1")) {
                        dealeraddress.setText(dealerloc);
                        Asyncdealerbank asyncdealerbank=new Asyncdealerbank();
                        asyncdealerbank.execute();
                    }

                    Cursor cursor = mDatabaseHelper.getCustomerAddress(selectedBPCustomerID);

                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String bpaddress = cursor.getString(0);
                            String bpphone = cursor.getString(1);

                            if (!bpaddress.equals("null") && bpaddress != null && !bpaddress.isEmpty()) {
                                dealeraddress.setText(bpaddress);
                            } else {
                                dealeraddress.getText().clear();
                            }

                        } while (cursor.moveToNext());
                    }

                } catch (NullPointerException ex) {
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private final class Asyncdealerbank extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            boolean isactive = true;
            try {
                mDatabaseHelper.deleteDealerbankacc();
            } catch (Exception e) {
                res = e.getMessage();
                return res;
            }
            try{
                res = FetchDealerBankACFromServer(selectedBPCustomerID);
                System.out.println(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CreatePaymentActivityDirectSending.this);
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
                    getDealerBankACSpinner();
                    pd.dismiss();
                }else{
                    System.out.println(s);
                    pd.dismiss();
                }
            }catch (NullPointerException ex){

            }
        }
    }

    private void getBranchname(){
        hashDistrictid.clear();
        if(!DistrictArrayList.isEmpty()) {
            DistrictArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getBranchName();

        if(cursor.moveToFirst()){
            do{
                String orgId   = cursor.getString(0);
                String orgName          = cursor.getString(1);
                DistrictArrayList.add(orgName);
                hashDistrictid.put(orgName,orgId);
            }while(cursor.moveToNext());
        }
        loadDistrictSpinner();
    }

    private void getDealerBankACSpinner(){
        hashDealerBankID.clear();
        if(!DealerBankArrayList.isEmpty()) {
            DealerBankArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getDealerbankacc();

        if(cursor.moveToFirst()){
            do{
                String orgId   = cursor.getString(0);
                String orgName          = cursor.getString(1);
                DealerBankArrayList.add(orgName);
                hashDealerBankID.put(orgName,orgId);
            }while(cursor.moveToNext());
        }
        loadDealerBankACSpinner();
    }

    private void getkfgBankACSpinner() {
        hashKFGBankID.clear();
        if (!KFGBankArrayList.isEmpty()) {
            KFGBankArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getkfgbankacc();

        if (cursor.moveToFirst()) {
            do {
                String orgId = cursor.getString(0);
                String orgName = cursor.getString(1);
                KFGBankArrayList.add(orgName);
                hashKFGBankID.put(orgName, orgId);
            } while (cursor.moveToNext());
        }
        loadKFGBankACSpinner();
    }


    private void getOrderType() {
        if(!OrderTypeArrayList.isEmpty()) {
            OrderTypeArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getActivityType();

        if(cursor.moveToFirst()){
            do{
                String orgId = cursor.getString(0);
                String orgName = cursor.getString(1);
                String t_businessType_id = cursor.getString(2);
                OrderTypeArrayList.add(orgName);
                hashOrderTypeId.put(orgName,orgId);
                hashbusinessTypeId.put(orgName,t_businessType_id);
            }while(cursor.moveToNext());
        }
        loadOrderTypeSpinner();
    }

    private void loadOrderTypeSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, OrderTypeArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activityspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectOrderTypeSpinner();
    }

    private void selectOrderTypeSpinner(){
        activityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedactivityName=selectedOrg;
                    selectedactivityID = hashOrderTypeId.get(selectedactivityName);
                    selectedabusinesstypeID = hashbusinessTypeId.get(selectedactivityName);

                    if(!selectedactivityID.equals("1")){
                        hashDealerBankID.clear();
                        if(!DealerBankArrayList.isEmpty()) {
                            DealerBankArrayList.clear();
                        }
                        getDealer(selectedabusinesstypeID);

                    }
                }catch (NullPointerException ex){
                }catch (ArrayIndexOutOfBoundsException ex){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getOrg() {
        Cursor cursor = mDatabaseHelper.getOrganization();
        if(cursor.moveToFirst()){
            do{
                String   orgId   = cursor.getString(0);
                String orgName          = cursor.getString(1);
                selectedOrgId=orgId;
                orgNameArrayList.add(orgName);
                hashOrgId.put(orgName,orgId);
            }while(cursor.moveToNext());
        }
        loadOrganizationSpinner();
    }

    private void loadOrganizationSpinner(){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, orgNameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orgspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectOrgSpinner();
    }

    private void selectOrgSpinner(){
        orgspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedOrgName=selectedOrg;
                    selectedOrgId = hashOrgId.get(selectedOrg);
                    System.out.println("org="+selectedOrg+" id="+selectedOrgId);
                }catch (NullPointerException ex){
                }catch (ArrayIndexOutOfBoundsException ex){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }
    private void loadDealerBankACSpinner() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DealerBankArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dealerbankspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectDealerBankACSpinner();
    }

    private void selectDealerBankACSpinner(){
        dealerbankspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedbank = parent.getSelectedItem().toString();
                    selectedDealerBankName=selectedbank;
                    selectedDealerBankId = hashDealerBankID.get(selectedbank);

                    if(selectedbank.equals("Others")){
                        dealerbankdit.setVisibility(View.VISIBLE);
                    }else{
                        dealerbankdit.setVisibility(View.GONE);
                    }
                    if(!selectedDealerBankId.equals("1")){
                        getBankName();
                    }

                }catch (NullPointerException ex){
                }catch (ArrayIndexOutOfBoundsException ex){
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String  FetchBranchNameFromServer(String bank_id){
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
            list.add(new BasicNameValuePair("name", "kfgBranchName"));
            list.add(new BasicNameValuePair("c_bank_id", bank_id));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Branch Not Found";
                supText1="1";
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
                result = true;
                if (supText1.equals("")) { //!supText.isEmpty() && !supText.equals("1") && supText!= "1") {
                    result = mDatabaseHelper.insertIntoBranchName(supid, supname);
                }else{
                }
            }
            if (result) {
                res = "Success";
                //  toastMessage("Branch Name fetched successfully");
            }
        }catch (Exception e) {
            res = e.getMessage();
            DistrictArrayList.clear();
            hashDistrictid.clear();
            loadDistrictSpinner();
        }

/*
        Cursor cursor = mDatabaseHelper.getBranchName();
        if(cursor.moveToFirst()){
            do{
                String districtid = cursor.getString(0);
                String districtname = cursor.getString(1);
                System.out.println("District = "+districtname+"-------------------------"+districtid);
                DistrictArrayList.add(districtname);
                hashDistrictid.put(districtname, districtid);
            }while(cursor.moveToNext());
        }

 */
        //loadDistrictSpinner();
        return res;
    }

    private String FetchKFGBankACFromServer(String bank_id, String branch_name) {
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
            list.add(new BasicNameValuePair("name", "kfgbank"));
            list.add(new BasicNameValuePair("c_bank_id", bank_id));
            list.add(new BasicNameValuePair("branch", branch_name));
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="KFG Bank A/C Not Found";
                supText1="1";
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
                if (supText1.equals("")) { //!supText.isEmpty() && !supText.equals("1") && supText!= "1") {
                    result = mDatabaseHelper.insertIntokfgbankname(supid, supname);
                }else{

                }
            }

            if (result) {
                res = "Success";
                //   toastMessage("KFG Bank A/C fetched successfully");
            }

        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private void loadKFGBankACSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, KFGBankArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        kfgbankspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectKFGBankACSpinner();
    }

    private void selectKFGBankACSpinner(){
        kfgbankspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedKFGBankName=selectedOrg;
                    selectedKFGBankId = hashKFGBankID.get(selectedOrg);
                }catch (NullPointerException ex){
                }catch (ArrayIndexOutOfBoundsException ex){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void callsave(View view){

        if (selectedactivityID.equals("1")){ //actTypeSpinner.getCount()==0){
            toastMessage("No Activity Type Selected !!");
            return;
        }

        if(orgNameArrayList.size()==0){
            toastMessage("No Organization found for this user");
            return;
        }

        if(selectedBPCustomerID.equals("1")){
            toastMessage("Please select Dealer");
            return;
        }

        if(selectedDealerBankId.equals("1")){
            toastMessage("Please select a Dealer Bank");
            return;
        }

        if(selectedDealerBankName.equals("Others")){
            selectedDealerBankName = dealerbankdit.getText().toString();
            otherno = "1";
            return;
        }

        if(selectedDealerBankName.isEmpty()) {
            toastMessage("Please Enter Dealer's Bank Name!");
            return;
        }

        if(selectedcityid.equals("1")){
            toastMessage("Please Select Bank Name");
            return;
        }

        if(selectedregionid.equals("1")){
            toastMessage("Please Select Branch Name");
            return;
        }

        if(KFGBankArrayList.size()==1){
            toastMessage("No KFG bank Account found");
            return;
        }

        if(selectedKFGBankId.equals("1")){
            toastMessage("Please Select KFG bank Name");
            return;
        }

        paymentamt = paymentedittext.getText().toString().trim();
        mrno = mredittext.getText().toString().trim();
        deponame = depositorname.getText().toString().trim();
        depono = depositorno.getText().toString().trim();
        bill = billno.getText().toString().trim();

        if(paymentamt.isEmpty()) {
            paymentedittext.requestFocus();
            paymentedittext.setError("Please Enter Payment amount!");
            return ;
        }

        /*if(mrno.isEmpty()) {
            mredittext.requestFocus();
            mredittext.setError("Please Enter MR No.");
            return ;
        }*/

        if(deponame.isEmpty()) {
            depositorname.requestFocus();
            depositorname.setError("Please Enter Depositor's Name!");
            return ;
        }

        if(depono.isEmpty()) {
            depositorno.requestFocus();
            depositorno.setError("Please Enter Depositor Phone No!");
            return ;
        }
        SaveAlert();
    }
/*
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            boolean isactive = true;
            try{
                res = insertData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CreatePaymentActivityDirectSending.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Payment is creating ........");
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
                    toastMessage("Payment Placed Successfully!!!");
                    Intent intent = new Intent(CreatePaymentActivityDirectSending.this, SelectionAcitvity.class);
                    startActivity(intent);
                }else{
                    System.out.println(s);
                    pd.dismiss();
                    toastMessage("Payment Placed failed");
                    savebutton.setEnabled(true);
                    //Intent intent = new Intent(CreatePaymentActivityDirectSending.this, SelectionAcitvity.class);
                    //startActivity(intent);
                }
            }catch (NullPointerException ex){

            }
        }
    }
*/
    public String insertData() {
        Connection conn = null;
        boolean isCommit = false;
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
        //    conn=DriverManager.getConnection("jdbc:postgresql://172.16.253.10:5432/golocdb?useUnicode=yes&characterEncoding=UTF-8","appuser1","kfg@app@2022");
            conn = DriverManager.getConnection("jdbc:postgresql://172.16.253.22:5433/golocdb_14nov2023?useUnicode=yes&characterEncoding=UTF-8","adempiere","adempiere");
            //conn = DriverManager.getConnection("jdbc:postgresql://172.16.253.241:5454/kfg_05mar2022?useUnicode=yes&characterEncoding=UTF-8","adempiere","adempiere");

            if(conn == null){
                return  result;
            }

            conn.setAutoCommit(false);
            String new_sql="insert into t_app_payment\n" +
                    "(ad_client_id ,ad_org_id ,createdby , c_activity_id , activity_type , c_bpartner_id , c_bp_bankaccount_id ,dealer_code,depost_name, depost_phone,bill_no,\n" +
                    "dealer_bankacc , c_bankaccount_id , kfg_bankacc , paymentamt , mrno,value,datetrx, created,updated,payment_status,BinaryData) \n" + //description,BinaryData
                    "VALUES(1000000,"+selectedOrgId+","+ ad_user_id+","+selectedactivityID+",'"+selectedactivityName+"',"+selectedBPCustomerID+","+selectedDealerBankId+",'"+selectedbpcode+"','"+deponame+"','"+depono+"','"+bill+"','"+
                    selectedDealerBankName+"',"+selectedKFGBankId+",'"+selectedKFGBankName+"',"+paymentamt+",'"+mrno+"','"+selectedbpcode+"','"+selectdate+"',now(),now(),'DR',?)";

//            String withoutbankid = "insert into t_app_payment\n" +
//                    "(ad_client_id ,ad_org_id ,createdby , c_activity_id , activity_type , c_bpartner_id  ,dealer_code,depost_name, depost_phone,bill_no,\n" +
//                    "dealer_bankacc , c_bankaccount_id , kfg_bankacc , paymentamt , mrno,value,datetrx, created,updated,payment_status,BinaryData) \n" + //description,BinaryData
//                    "VALUES(1000000,"+selectedOrgId+","+ ad_user_id+","+selectedactivityID+",'"+selectedactivityName+"',"+selectedBPCustomerID+",'"+selectedbpcode+"','"+deponame+"','"+depono+"','"+bill+"','"+
//                    selectedDealerBankName+"',"+selectedKFGBankId+",'"+selectedKFGBankName+"',"+paymentamt+",'"+mrno+"','"+selectedbpcode+"','"+selectdate+"',now(),now(),'DR',?)"; //,'"+description+"'

            String withoutbankid = "insert into t_app_payment\n" +
                    "(ad_client_id ,ad_org_id ,createdby , c_activity_id , activity_type , c_bpartner_id  ,dealer_code,depost_name, depost_phone,bill_no,\n" +
                    "dealer_bankacc , c_bankaccount_id , kfg_bankacc , paymentamt , mrno,value,datetrx, created,updated,payment_statusa) \n" + //description,BinaryData
                    "VALUES(1000000,"+selectedOrgId+","+ ad_user_id+","+selectedactivityID+",'"+selectedactivityName+"',"+selectedBPCustomerID+",'"+selectedbpcode+"','"+deponame+"','"+depono+"','"+bill+"','"+
                    selectedDealerBankName+"',"+selectedKFGBankId+",'"+selectedKFGBankName+"',"+paymentamt+",'"+mrno+"','"+selectedbpcode+"','"+selectdate+"',now(),now(),'DR')"; //,'"+description+"'

            PreparedStatement statement1;

            if(otherno.equals("1")){
                statement1 = conn.prepareStatement(withoutbankid);
            }else{
                statement1 = conn.prepareStatement(new_sql);
            }

            statement1.setBytes(1,inputData);

            int res= statement1.executeUpdate();
            statement1.close();

            if(res == 1){
                isCommit = true;
            }else{
                isCommit = false;
            }

            if( isCommit){
                conn.commit();
                result = "Success";
                mDatabaseHelper.deletekfgbankacc();
                mDatabaseHelper.deleteDealerbankacc();
                mDatabaseHelper.deleteBranchName();

            } else{
                result = "Intertion failed";
                //toastMessage("Payment Placed failed");
                return result;
            }

        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (NullPointerException e) {
            System.out.println(e);
        }finally {
        }
        return result;
    }

    public void cancel(View view){
        cancelalert();
    }

    private String  FetchDealerBankACFromServer(String bp) {
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
            list.add(new BasicNameValuePair("name", "allbank"));
            list.add(new BasicNameValuePair("c_bpartner_id", bp));
            System.out.println("bp============="+bp);
            list.add(new BasicNameValuePair("ad_user_id", ad_user_id));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            System.out.println("Dealerbank S Response======================= "+s+bp);
            Log.d("DEBUG","S Response= "+s);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Dealer Bank A/C Not Found";
                supText="1";
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
            String bankid = "", bankname = "";
            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                bankname = (String)line.get(0);
                bankid = (String)line.get(1);
                System.out.println(bankname+" bankname !!!!!!!!!!!!!!!!!!!!!!!!!!"+ bankid);
                //    result = true;

                if (supText.equals("")) { //!supText.isEmpty() && !supText.equals("1") && supText!= "1") {
                    //DealerBankArrayList.add(bankname);
                    //hashDealerBankID.put(bankname, bankid);
                    result = mDatabaseHelper.insertIntodealerbankname(bankid,bankname);
                    //loadDealerBankACSpinner();
                } else {
                }
            }

            if (result) {
                res = "Success";
                // toastMessage("Dealer's Bank A/C fetched successfully");
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

    public void SaveAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreatePaymentActivityDirectSending.this);
        builder1.setMessage("Are you sure you want to Save this entry?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        savebutton.setEnabled(false);
                        cancelbtn.setEnabled(false);
                        dialog.cancel();
                        AsyncSendToServer asyncSendToServer=new AsyncSendToServer();
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

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreatePaymentActivityDirectSending.this);
        builder1.setMessage("Are you sure you want to cancel this entry?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(CreatePaymentActivityDirectSending.this, SelectionAcitvity.class);
                        startActivity(intent);
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreatePaymentActivityDirectSending.this);
        builder1.setMessage("Are you sure you want to cancel this entry?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(CreatePaymentActivityDirectSending.this, SelectionAcitvity.class);
                        startActivity(intent);
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

    public boolean internetIsConnected() {
        try {
            //String command = "ping -w 1 -c 1 172.16.253.22";
            String command = "ping -w 1 -c 1 172.16.253.10";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void ShowAlert(String title,String msg){

        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(CreatePaymentActivityDirectSending.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... strings) {

            String res = "";
            boolean isactive = true;

            try {
                ArrayList salesHeader = new ArrayList();

                Vector headerLines = new Vector();

                headerLines.add(selectedOrgId);
                headerLines.add(ad_user_id);
                headerLines.add(selectedactivityID);
                headerLines.add(selectedactivityName);
                headerLines.add(selectedBPCustomerID);
                headerLines.add(selectedDealerBankId);
                headerLines.add(selectedbpcode);
                headerLines.add(deponame);
                headerLines.add(depono);
                headerLines.add(bill);
                headerLines.add(selectedDealerBankName);
                headerLines.add(selectedKFGBankId);
                headerLines.add(selectedKFGBankName);
                headerLines.add(paymentamt);
                headerLines.add(mrno);
                headerLines.add(selectedbpcode);
                headerLines.add(selectdate);
                if (inputData != null) {
                    headerLines.add(android.util.Base64.encodeToString(inputData, android.util.Base64.NO_WRAP));
                }else{
                    headerLines.add("");
                }

                salesHeader.add(headerLines);

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString = headerString + s;
                    } else {
                        headerString = headerString + "_" + s;

                    }
                }

             //   HttpURLConnection con = (HttpURLConnection) (new URL("" + apiUrl + "/CreatePayment")).openConnection();
                HttpURLConnection con = (HttpURLConnection) (new URL("" + apiUrl + "/Payment")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/Payment");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("header", headerString));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);

                if (s.isEmpty() || !s.equals("Success")) {
                    res = s;
                    return res;
                } else if (s.equals("Success")) {
                    return "Success";
                }
            }
            catch(Exception e){
                res = e.getMessage();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CreatePaymentActivityDirectSending.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Payment is creating ........");
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
                    toastMessage("Payment Placed Successfully!!!");
                    Intent intent = new Intent(CreatePaymentActivityDirectSending.this, SelectionAcitvity.class);
                    startActivity(intent);
                }else{
                    System.out.println(s);
                    pd.dismiss();
                    toastMessage("Payment Placed failed");
                    savebutton.setEnabled(true);
                }
            }catch (NullPointerException ex){}
        }
    }
}