package com.sysnova.kfg_sales_appv903.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Handler;
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
import com.sysnova.kfg_sales_appv10.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static com.sysnova.kfg_sales_appv903.Activity.LoginActivity.currentdate;
import static org.apache.http.impl.cookie.DateUtils.parseDate;

public class OrgBranchSelection extends AppCompatActivity {
    Button showdate;
    String cdate,cdate1;
    SearchableSpinner District_spinner1,orgSpinner,branchSpinner,CustomerSpinner,Location_Spinner;
    Spinner actTypeSpinner,prodTypeSpinner,pricelist_spinner;
    EditText truckno_editText,credit_limit_editText,dealer_amt_editText;
    DatabaseHelper mDatabaseHelper;
    boolean doubleBackToExitPressedOnce=false;
    public static String selectedOrgId="",selectedWHId="",selectedOrgName="",selectedWHName="",selectedactivityName="",selectedactivityID="",selectedbusinesstypeID="",showdateNew="";
    TextView location_TextView,District_TextView,org_TextView,pricelist_text;
    private DatePickerDialog.OnDateSetListener dateSetListener1;
    public static String selectedLocationId="",truckno="",selectedLocationName="" ,selectedDistrictName="",selectedDistrictId="";
    public static String selectedBPCustomerID = "",selectedPriceListName="",selectedPriceListId="",selectedProdActivityName="",selectedProdActivityID="" ;
    public static String dealer_amt="",credit_limit="",selectedBPCustomerName="";

    List<String> orgNameArrayList = new ArrayList<String>();
    List<String> branchNameArrayList = new ArrayList<String>();

    HashMap<String,String> hashOrgId  = new HashMap<>();
    HashMap<String,String> hashBranchId  = new HashMap<>();

    List<String> ProductTypeArrayList = new ArrayList<String>();
    HashMap<String,String> hashProductTypeId  = new HashMap<>();

    List<String> OrderTypeArrayList = new ArrayList<String>();
    HashMap<String,String> hashOrderTypeId  = new HashMap<>();
    HashMap<String,String> hashBusinessTypeId  = new HashMap<>();

    HashMap<String, String> hashBPID = new HashMap<>();
    ArrayList<String> customerArrayList = new ArrayList<>();

    HashMap<String, String> hashLocationId = new HashMap<>();
    ArrayList<String> LocationArrayList = new ArrayList<>();

    HashMap<String, String> hashDistrictId = new HashMap<>();
    ArrayList<String> DistrictArrayList = new ArrayList<>();

    HashMap<String, String> hashPriceListID = new HashMap<>();
    ArrayList<String> PriceListArrayList = new ArrayList<>();

    String odate = "",productType = "",actType = "",DistrictName = "",ThanaName = "",org = "",wh = "";
    String dealer = "",truck = "",pricelist ="",edit_userid="";

    String documentNumber="";
    String TrxType="";

    Boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_branch_selection);

        getSupportActionBar().setTitle("Create Sales Order");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#0b79bf")));

        boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
        //2022-09-15 isDarkThemeOn add

        actTypeSpinner    = findViewById(R.id.actType_spinner);
        prodTypeSpinner = (Spinner) findViewById(R.id.type_code);
        CustomerSpinner    = findViewById(R.id.dealer_spinner);
        branchSpinner = findViewById(R.id.branch_spinner);
        District_spinner1 = (SearchableSpinner) findViewById(R.id.District_spinner);
        Location_Spinner=(SearchableSpinner) findViewById(R.id.location_spinner);
        pricelist_spinner=(Spinner) findViewById(R.id.pricelist_spinner);
        orgSpinner = (SearchableSpinner) findViewById(R.id.org_spinner);
        truckno_editText=(EditText)findViewById(R.id.truckno_editText);
        dealer_amt_editText=(EditText)findViewById(R.id.dealer_amt_editText);
        credit_limit_editText=(EditText)findViewById(R.id.credit_limit_editText);

        location_TextView = (TextView) findViewById(R.id.location_TextView);
        District_TextView = (TextView) findViewById(R.id.District_TextView);
        org_TextView = (TextView) findViewById(R.id.org_TextView);
        pricelist_text=(TextView) findViewById(R.id.pricelist_text);

        //2022-09-16
        if(isDarkThemeOn==true){
            truckno_editText.setTextColor(Color.parseColor("#FFFFFF"));
            dealer_amt_editText.setTextColor(Color.parseColor("#FFFFFF"));
        }
        //2022-09-16

        Date dateSql=new Date();
        showdate = (Button) findViewById(R.id.date);

        mDatabaseHelper=new DatabaseHelper(OrgBranchSelection.this);
        String strDate = currentdate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateSql = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        cdate =currentdate;
        cdate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateSql);

        System.out.println(new Date());
        showdate.setText(""+cdate1+"");
        Date finalDateSql = dateSql;
        showdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrgBranchSelection obj = new OrgBranchSelection();
                Calendar calendar;
                calendar = obj.dateToCalendar(finalDateSql);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                //calendar.set(Calendar.DATE, calendar.getActualMaximum(dateSql));

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        OrgBranchSelection.this,
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
                Date date1 = new Date(year-1900, month-1,dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date3= date1;
                //2022-05-12 start
                //Calendar someDate = Calendar.getInstance();
                OrgBranchSelection obj1 = new OrgBranchSelection();
                Calendar someDate= obj1.dateToCalendar(finalDateSql);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                someDate.add(Calendar.DAY_OF_YEAR, -3);
                Date date31 = someDate.getInstance().getTime();
                Date myDate = finalDateSql; //new Date();
                // Date prvDate3 = new Date(myDate.getTime() - 259200000); // 3 * 24 * 60 * 60 * 1000
                Date prvDate3 = new Date(finalDateSql.getTime() - 259200000); // 3 * 24 * 60 * 60 * 1000
                //2022-09-17

                if ((date3.equals(myDate) || date3.before(myDate)) && (date3.after(prvDate3))){
                    cdate = formatter.format(date1);
                    cdate1 = formatter1.format(date1);
                    showdate.setText(cdate1);
                    //showdate.setText(cdate);

                }
                else {
                    ShowAlert("Warning", "Order Date is not valid. You can order only for today and previous 2 days.");
                }
            }
        };
        //2024-10-16
        documentNumber=getIntent().getStringExtra("documentNumber");//edit
        TrxType=getIntent().getStringExtra("TrxType");//Edit

        //i.putExtra("DOCNO",documentNo);
        //i.putExtra("Type","Edit");

        //2024-10-16
        getProductType();
        getOrderType();
        getOrg();
        getDistrict();
        //getThana();
        //getWH();
        getCustomer2();  //closed by anika on 2026-03-15
        selectPriceListSpinner();
        getlist();
        //2024-08-13
        if (savedInstanceState != null) {
            truckno_editText.setText(savedInstanceState.getString("truckno"));
            dealer_amt_editText.setText(savedInstanceState.getString("dealerloc"));
            showdate.setText(savedInstanceState.getString("sdate"));
            actTypeSpinner.setSelection(savedInstanceState.getInt("actType"));
            prodTypeSpinner.setSelection(savedInstanceState.getInt("prodType"));
            CustomerSpinner.setSelection(savedInstanceState.getInt("customer"));
            branchSpinner.setSelection(savedInstanceState.getInt("wh"));
            District_spinner1.setSelection(savedInstanceState.getInt("DistrictName"));
            Location_Spinner.setSelection(savedInstanceState.getInt("ThanaName"));
            orgSpinner.setSelection(savedInstanceState.getInt("org"));
            pricelist_spinner.setSelection(savedInstanceState.getInt("pricelist"));
        }
        //2024-08-13
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private void getlist() {
        Cursor cursor = null;
        if (TrxType != null && TrxType.equals("Edit")) {
            cursor = mDatabaseHelper.getProductOrderheadNewEdit(documentNumber);
        }
        else{
            cursor = mDatabaseHelper.getProductOrderheadNew(LoginActivity.ad_user_id);
        }

        if (cursor.moveToFirst()) {
            do {
                odate = cursor.getString(2);
                productType = cursor.getString(3);
                actType = cursor.getString(4);
                DistrictName = cursor.getString(5);
                ThanaName = cursor.getString(6);
                org = cursor.getString(7);
                wh = cursor.getString(8);
                dealer = cursor.getString(9);
                truck = cursor.getString(10);
                pricelist = cursor.getString(11);
                edit_userid = cursor.getString(12);

                showdate.setText(odate);
                truckno_editText.setText(truck);
                prodTypeSpinner.setSelection(((ArrayAdapter) prodTypeSpinner.getAdapter()).getPosition(productType));
                actTypeSpinner.setSelection(((ArrayAdapter) actTypeSpinner.getAdapter()).getPosition(actType));
                District_spinner1.setSelection(((ArrayAdapter) District_spinner1.getAdapter()).getPosition(DistrictName));

                Cursor cursor1 = mDatabaseHelper.getDistrictSelected(DistrictName);
                if (cursor1.moveToFirst()) {
                    String DistrictId = cursor1.getString(0);
                    getThanaSelectedNew(DistrictId);
                    Cursor cursor2 = mDatabaseHelper.getThanaSelected(ThanaName);
                    Location_Spinner.setSelection(((ArrayAdapter) Location_Spinner.getAdapter()).getPosition(ThanaName));
                    if (cursor2.moveToFirst()) {
                        String ThanaId = cursor2.getString(0);
                        getWHSelected("0", DistrictId, ThanaId, productType);
                        branchSpinner.setSelection(((ArrayAdapter) branchSpinner.getAdapter()).getPosition(wh));
                    }
                }
                Cursor cursor3 = mDatabaseHelper.getOrganizationSelected(org);
                if (cursor3.moveToFirst()) {
                    String orgId = cursor3.getString(0);
                    getWHSelected(orgId, "0", "0", productType);
                }

                orgSpinner.setSelection(((ArrayAdapter) orgSpinner.getAdapter()).getPosition(org));
                Cursor cursor4 = mDatabaseHelper.getCustomerdetails(dealer);
                if (cursor4.moveToFirst()) {
                    CustomerSpinner.setSelection(((ArrayAdapter) CustomerSpinner.getAdapter()).getPosition(dealer));
                }
                else{
                    CustomerSpinner.setSelection(((ArrayAdapter) CustomerSpinner.getAdapter()).getPosition(""));
                }

                pricelist_spinner.setSelection(((ArrayAdapter) pricelist_spinner.getAdapter()).getPosition(pricelist));
                prodTypeSpinner.setEnabled(false);

                if(productType.equals("Feed")) {
                    check = true;
                }

                if (TrxType != null && TrxType.equals("Edit")) {
                    prodTypeSpinner.setEnabled(false);
                    actTypeSpinner.setEnabled(false);
                    showdate.setEnabled(false);
                    prodTypeSpinner.setEnabled(false);
                    District_spinner1.setEnabled(false);
                    Location_Spinner.setEnabled(false);
                    branchSpinner.setEnabled(false);
                    orgSpinner.setEnabled(false);
                    CustomerSpinner.setEnabled(false);
                    pricelist_spinner.setEnabled(false);
                }
            } while (cursor.moveToNext());
        }
    }
    public void callConfirm(View view) {
        boolean result=false;
        int id1=1;
        showdateNew=showdate.getText().toString();
        String ids=String.valueOf(id1);
        truckno=truckno_editText.getText().toString();
        if (selectedProdActivityID.equals("0")){
            toastMessage("No Product Type Selected !!");
            return;
        }
        if (selectedactivityID.equals("1")){
            toastMessage("No Activity Type Selected !!");
            return;
        }
        if(selectedProdActivityName.equals("DOC")){
            if (pricelist_spinner.getCount()==0){
                toastMessage("No PriceList Item Selected !!");
                return;
            }
        }
        if(selectedProdActivityName.equals("Feed")) {
            if (selectedDistrictId.equals("1")) {
                toastMessage("No District is Selected !!");
                return;
            }
            if (selectedWHId.equals("")) {
                toastMessage("No Warehouse is Selected !!");
                return;
            }
        }
        if (selectedBPCustomerID.equals("1")){
            toastMessage("No Dealer is Selected !!");
            return;
        }
        Cursor cursor = mDatabaseHelper.getProductOrderheadNew(LoginActivity.ad_user_id);

        if (cursor.moveToFirst()){
            if(TrxType.equals("Edit")){
                result = mDatabaseHelper.updateProductOrderHeadNew("2", showdateNew, selectedProdActivityName, selectedactivityName, selectedDistrictName, selectedLocationName, selectedOrgName, selectedWHName, selectedBPCustomerName, truckno, selectedPriceListName, edit_userid);

            }else {
                result = mDatabaseHelper.updateProductOrderHeadNew("1", showdateNew, selectedProdActivityName, selectedactivityName, selectedDistrictName, selectedLocationName, selectedOrgName, selectedWHName, selectedBPCustomerName, truckno, selectedPriceListName, LoginActivity.ad_user_id);
            }
            if (result) {
//                toast("Updated");
//                String customerName_Code = selectedBPCustomerName;
//                Intent i = new Intent(OrgBranchSelection.this, Sales_Order_Activity1.class);
//                i.putExtra("CUSTOMERNAMECODE", customerName_Code);
//                startActivity(i);

                if(TrxType.equals("Edit")){
                    //toast("Updated");
                    Intent i = new Intent(OrgBranchSelection.this, ProductOrderLine.class);
                    i.putExtra("TrxType", TrxType);
                    i.putExtra("DOCNO",documentNumber);
                    i.putExtra("CUSTOMERNAMECODE",dealer);
                    startActivity(i);
                }
                else{
                    toast("Updated");
                    String customerName_Code = selectedBPCustomerName;
                    Intent i = new Intent(OrgBranchSelection.this, Sales_Order_Activity1.class);
                    i.putExtra("CUSTOMERNAMECODE", customerName_Code);
                    startActivity(i);
                }

            } else {
                if(TrxType.equals("Edit")){
                    //toast("Updated");
                    Intent i = new Intent(OrgBranchSelection.this, ProductOrderLine.class);
                    i.putExtra("TrxType", TrxType);
                    i.putExtra("DOCNO",documentNumber);
                    i.putExtra("CUSTOMERNAMECODE",dealer);
                    startActivity(i);
                }
                //toast("Could not Updated into internal database");
            }

        }
        else {
            result = mDatabaseHelper.insertIntoProductOrderHeadNew(ids, selectedProdActivityName, selectedactivityName, selectedDistrictName, selectedLocationName, selectedOrgName, selectedWHName, selectedBPCustomerName, truckno, selectedPriceListName, showdateNew, LoginActivity.ad_user_id,"","");
            //result=mDatabaseHelper.insertIntoProductOrderHead(ids,selectedProdActivityName,selectedactivityName,selectedDistrictName,selectedLocationName,selectedOrgName,selectedWHName,selectedBPCustomerName,truckno);
            if (result) {
                toast("ProductOrder Inserted");
                String customerName_Code = selectedBPCustomerName;
                Intent i = new Intent(OrgBranchSelection.this, Sales_Order_Activity1.class);
                i.putExtra("CUSTOMERNAMECODE", customerName_Code);
                startActivity(i);

            } else {
                toast("Could not insert into internal database");
            }
        }
        /*String customerName_Code = selectedBPCustomerName; //distributorCodeEditText.getText().toString();
        Intent i=new Intent(OrgBranchSelection.this,Sales_Order_Activity1.class);
        i.putExtra("CUSTOMERNAMECODE", customerName_Code);
       // i.putExtra("DOCNO",documentNo);
        startActivity(i);*/
    }

    public void callCancel(View view) {
        //Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
        //startActivity(i);
        //2022-09-29
        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
        Cursor cursor = mDatabaseHelper.getProductOrderheadNew(LoginActivity.ad_user_id);
        if (cursor.moveToFirst()){
            Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
            startActivity(i);
        }
        else{
            //savealert();
            //2022-10-13
            if (selectedProdActivityID.equals("1")){
                Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
                startActivity(i);
            }
            else {
                savealert();
            }
        }
    }

    //2022-09-29
    public void savealert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to save this order?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean result=false;
                        int id1=1;
                        showdateNew=showdate.getText().toString();
                        String ids=String.valueOf(id1);
                        truckno=truckno_editText.getText().toString();
                        if (selectedProdActivityID.equals("0")){
                            toastMessage("No Product Type Selected !!");
                            return;
                        }
                        if (selectedactivityID.equals("1")){
                            toastMessage("No Activity Type Selected !!");
                            return;
                        }
                        if(selectedProdActivityName.equals("DOC")){
                            if (pricelist_spinner.getCount()==0){
                                toastMessage("No PriceList Item Selected !!");
                                return;
                            }
                        }
                        if(selectedProdActivityName.equals("Feed")) {
                            if (selectedDistrictId.equals("1")) {
                                toastMessage("No District is Selected !!");
                                return;
                            }
                            if (selectedWHId.equals("")) {
                                toastMessage("No Warehouse is Selected !!");
                                return;
                            }
                        }

                        Cursor cursor = mDatabaseHelper.getProductOrderheadNew(LoginActivity.ad_user_id);

                        if (cursor.moveToFirst()){
                            result = mDatabaseHelper.updateProductOrderHeadNew("1",showdateNew,selectedProdActivityName, selectedactivityName,selectedDistrictName, selectedLocationName,selectedOrgName,selectedWHName, selectedBPCustomerName,truckno,selectedPriceListName, LoginActivity.ad_user_id);
                            if (result) {
                                Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
                                startActivity(i);

                            } else {
                                toast("Could not Updated into internal database");
                            }

                        }
                        else {
                            result = mDatabaseHelper.insertIntoProductOrderHeadNew(ids, selectedProdActivityName, selectedactivityName, selectedDistrictName, selectedLocationName, selectedOrgName, selectedWHName, selectedBPCustomerName, truckno, selectedPriceListName, showdateNew, LoginActivity.ad_user_id,"","");
                            //result=mDatabaseHelper.insertIntoProductOrderHead(ids,selectedProdActivityName,selectedactivityName,selectedDistrictName,selectedLocationName,selectedOrgName,selectedWHName,selectedBPCustomerName,truckno);
                            if (result) {
                                toast("ProductOrder Inserted");
                                String customerName_Code = selectedBPCustomerName;
                                Intent i = new Intent(OrgBranchSelection.this, SelectionAcitvity.class);
                                //i.putExtra("CUSTOMERNAMECODE", customerName_Code);
                                startActivity(i);

                            } else {
                                toast("Could not insert into internal database");
                            }
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(OrgBranchSelection.this, SelectionAcitvity.class);
                        startActivity(i);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        //alert11.show();
        alert11.getWindow().setType(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW);
        alert11.show();
        alert11.getWindow().setGravity(Gravity.BOTTOM);
    }

    //2022-09-29

    private void loadOrganizationSpinner(){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, orgNameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orgSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectOrgSpinner();
    }

    private void loadWarehouseSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branchNameArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        branchSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectWHSpinner();

    }
    private void loadLocationSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, LocationArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        Location_Spinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectLocationSpinner();

    }
    private void loadDistrictSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, DistrictArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        District_spinner1.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectDistrictSpinner();

    }
    private void loadProductTypeSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ProductTypeArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        prodTypeSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectProdTypeSpinner();

    }
    private void loadOrderTypeSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, OrderTypeArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        actTypeSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectOrderTypeSpinner();
    }

    private void loadCustomerSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, customerArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CustomerSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectBPCustomerSpinner();
    }

    private void loadPriceListSpinner()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, PriceListArrayList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        pricelist_spinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectPriceListSpinner();
    }

    private void getOrg() {

        hashOrgId.clear();
        if(!orgNameArrayList.isEmpty()){
            orgNameArrayList.clear();
        }

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

    private void getOrderType() {

        hashOrderTypeId.clear();
        if(!OrderTypeArrayList.isEmpty()) {
            OrderTypeArrayList.clear();
        }

        String prodType=prodTypeSpinner.getSelectedItem().toString();

        Cursor cursor = mDatabaseHelper.getSalesType(prodType);

        if(cursor.moveToFirst()){
            do{
                String   orgId   = cursor.getString(0);
                String orgName          = cursor.getString(1);
                String businessProductType          = cursor.getString(2);
                String t_businessType_id          = cursor.getString(3);
                OrderTypeArrayList.add(orgName);
                hashOrderTypeId.put(orgName,orgId);
                hashBusinessTypeId.put(orgName,t_businessType_id);
            }while(cursor.moveToNext());
        }
        loadOrderTypeSpinner();
    }
    private void getProductType() {

        hashProductTypeId.clear();

        if(!ProductTypeArrayList.isEmpty()) {
            ProductTypeArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductType();

        if(cursor.moveToFirst()){
            do{
                String orgId = cursor.getString(1);
                String orgName = cursor.getString(2);
                ProductTypeArrayList.add(orgName);
                hashProductTypeId.put(orgName,orgId);
            }while(cursor.moveToNext());
        }
        loadProductTypeSpinner();
    }

    private void getWH()
    {   Cursor cursor ;
        hashBranchId.clear();
        if(!branchNameArrayList.isEmpty())
        {
            branchNameArrayList.clear();
        }
        if(selectedProdActivityName.equals("DOC")){
            cursor = mDatabaseHelper.getWarehouseByOrg(selectedOrgId);
        }
        else{
            cursor = mDatabaseHelper.getWarehouseByRegion(selectedDistrictId,selectedLocationId);
        }
        //Cursor cursor = mDatabaseHelper.getWarehouseByOrg(selectedOrgId);

        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(1);
                String warehouseName          = cursor.getString(3);
                branchNameArrayList.add(warehouseName);
                hashBranchId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadWarehouseSpinner();
    }
    private void getWHNew()
    {   Cursor cursor ;
        hashBranchId.clear();
        if(!branchNameArrayList.isEmpty())
        {
            branchNameArrayList.clear();
        }
        cursor = mDatabaseHelper.getWarehouseByRegion(selectedDistrictId,selectedLocationId);

        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(1);
                String warehouseName          = cursor.getString(3);
                branchNameArrayList.add(warehouseName);
                hashBranchId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadWarehouseSpinner();
    }

    private void getWHSelected(String OrgId,String DistrictId,String LocationId,String prodType)
    {   Cursor cursor ;
        hashBranchId.clear();
        if(!branchNameArrayList.isEmpty())
        {
            branchNameArrayList.clear();
        }

        if(prodType.equals("DOC")){
            cursor = mDatabaseHelper.getWarehouseByOrg(OrgId);
        }
        else{
            cursor = mDatabaseHelper.getWarehouseByRegion(DistrictId,LocationId);
        }
        //Cursor cursor = mDatabaseHelper.getWarehouseByOrg(selectedOrgId);
        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(1);
                String warehouseName          = cursor.getString(3);
                branchNameArrayList.add(warehouseName);
                hashBranchId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadWarehouseSpinner();
    }
    private void getThana()
    {   //Cursor cursor ;
        hashLocationId.clear();
        if(!LocationArrayList.isEmpty())
        {
            LocationArrayList.clear();
        }
        //Cursor cursor = mDatabaseHelper.getThana(selectedDistrictId);
        Cursor cursor = mDatabaseHelper.getThana_Wh(selectedDistrictId);

        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(0);
                String warehouseName          = cursor.getString(1);
                LocationArrayList.add(warehouseName);
                hashLocationId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadLocationSpinner();
    }
    private void getThanaSelectedNew(String DistrictId)
    {   //Cursor cursor ;
        hashLocationId.clear();
        if(!LocationArrayList.isEmpty())
        {
            LocationArrayList.clear();
        }
        //Cursor cursor = mDatabaseHelper.getThana(DistrictId);
        Cursor cursor = mDatabaseHelper.getThana_Wh(DistrictId);

        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(0);
                String warehouseName          = cursor.getString(1);
                LocationArrayList.add(warehouseName);
                hashLocationId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadLocationSpinner();
    }
    private void getDistrict() {
        hashDistrictId.clear();
        if(!DistrictArrayList.isEmpty()){
            DistrictArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getDistrict_Wh();

        if(cursor.moveToFirst()){
            do{
                String   warehouseID   = cursor.getString(0);
                String warehouseName          = cursor.getString(1);
                DistrictArrayList.add(warehouseName);
                hashDistrictId.put(warehouseName,warehouseID);
            }while(cursor.moveToNext());
        }
        loadDistrictSpinner();
    }
    private void getCustomer1() {
        if(!customerArrayList.isEmpty())
        {
            customerArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getCustomerdetailsNew(selectedbusinesstypeID);

        if (cursor.moveToFirst()) {
            do {
                String orgId = cursor.getString(0);
                String orgName = cursor.getString(2);
                dealer_amt_editText.setText(cursor.getString(3));
                credit_limit_editText.setText(cursor.getString(4));
                selectedBPCustomerName=cursor.getString(2);
                dealer_amt=cursor.getString(3);
                credit_limit=cursor.getString(4);
                customerArrayList.add(orgName);

            } while (cursor.moveToNext());
        }
        loadCustomerSpinner();

    }
    private void getCustomer(String selectedbusinesstypeID) {

        if (!customerArrayList.isEmpty()) {
            customerArrayList.clear();
            dealer_amt_editText.getText().clear();
            credit_limit_editText.getText().clear();
        }

        if (!hashBPID.isEmpty()) {
            hashBPID.clear();
            dealer_amt_editText.getText().clear();
            credit_limit_editText.getText().clear();
        }

        Cursor cursor = mDatabaseHelper.getCustomerdetailsNew(selectedbusinesstypeID);

        if (cursor.moveToFirst()) {
            do {
                String orgId = cursor.getString(0);
                String orgName1 = cursor.getString(1);
                String orgName = cursor.getString(2);
                String bpaddress = cursor.getString(3);
                String bpphone = cursor.getString(4);

                selectedBPCustomerName=cursor.getString(2);
                dealer_amt=cursor.getString(3);
                credit_limit=cursor.getString(4);
                //check
                if(!bpaddress.equals("null") && bpaddress!= null && !bpaddress.isEmpty()){
                    dealer_amt_editText.setText(bpaddress);
                }
                else{
                    dealer_amt_editText.getText().clear();
                }
                if(!bpphone.equals("null") && bpphone!= null && !bpphone.isEmpty()){
                    credit_limit_editText.setText(bpphone);
                }
                else{
                    credit_limit_editText.getText().clear();
                }
                customerArrayList.add(orgName);
                hashBPID.put(orgName,orgId);

            } while (cursor.moveToNext());
        }

        loadCustomerSpinner();

    }

    private void getCustomer2() {

        if (!customerArrayList.isEmpty()) {
            customerArrayList.clear();
            dealer_amt_editText.getText().clear();
            credit_limit_editText.getText().clear();
        }

        if (!hashBPID.isEmpty()) {
            hashBPID.clear();
            dealer_amt_editText.getText().clear();
            credit_limit_editText.getText().clear();
        }

        Cursor cursor = mDatabaseHelper.getCustomer2();

        if (cursor.moveToFirst()) {
            do {
                String orgId = cursor.getString(0);
                String orgName1 = cursor.getString(1);
                String orgName = cursor.getString(2);
                String bpaddress = cursor.getString(3);
                String bpphone = cursor.getString(4);

                selectedBPCustomerName=cursor.getString(2);
                dealer_amt=cursor.getString(3);
                credit_limit=cursor.getString(4);
                //check
                if(!bpaddress.equals("null") && bpaddress!= null && !bpaddress.isEmpty()){
                    dealer_amt_editText.setText(bpaddress);
                }
                else{
                    dealer_amt_editText.getText().clear();
                }
                if(!bpphone.equals("null") && bpphone!= null && !bpphone.isEmpty()){
                    credit_limit_editText.setText(bpphone);
                }
                else{
                    credit_limit_editText.getText().clear();
                }
                customerArrayList.add(orgName);
                hashBPID.put(orgName,orgId);

            } while (cursor.moveToNext());
        }

        loadCustomerSpinner();

    }

    private void selectOrderTypeSpinner(){
        actTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedactivityName=selectedOrg;
                    selectedactivityID = hashOrderTypeId.get(selectedactivityName);
                    selectedbusinesstypeID = hashBusinessTypeId.get(selectedactivityName);  //added on 2026-03-12

                    if(!selectedbusinesstypeID.equals("")){
                        getCustomer(selectedbusinesstypeID);
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
    private void selectOrgSpinner(){
        orgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedOrgName=selectedOrg;
                    selectedOrgId = hashOrgId.get(selectedOrg);
                    // toastMessage("org="+selectedOrg+" id="+selectedOrgId);
                    // FetchAllWarehouse();
                    getWH();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectWHSpinner(){
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedWHName=selectedOrg;
                    selectedWHId= hashBranchId.get(selectedWHName);
                    // selectedOrgId = hashOrgId.get(selectedOrg);
                    // toastMessage("org="+selectedOrg);
                    //getWH();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectLocationSpinner(){
        Location_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedLocationName=selectedOrg;
                    selectedLocationId= hashLocationId.get(selectedLocationName);
                    getWH();
                    //2022-0721
                    /*if(check) {
                        getWH();
                    }*/
                    /*if(!check) {
                        getWH();
                    }*/
                    //getLocation();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectDistrictSpinner(){
        District_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedDistrictName=selectedOrg;
                    selectedDistrictId= hashDistrictId.get(selectedDistrictName);
                    //FetchLocationFromServer();
                    /*if(!check) {
                        getThana();
                    }*/
                    //2022-07-21
                    getThana();
                    /*if(check) {
                        getThana();
                    }*/

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectProdTypeSpinner(){
        prodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedProdActivityName=selectedOrg;
                    selectedProdActivityID = hashProductTypeId.get(selectedProdActivityName);

                    //  toastMessage("OrderType="+selectedProdActivityName+" id="+selectedProdActivityID);
                    if(selectedOrg.equals("DOC")) {
                        District_TextView.setVisibility(view.GONE);
                        District_spinner1.setVisibility(view.GONE);
                        location_TextView.setVisibility(view.GONE);
                        Location_Spinner.setVisibility(view.GONE);
                        org_TextView.setVisibility(view.VISIBLE);
                        orgSpinner.setVisibility(view.VISIBLE);
                        pricelist_text.setVisibility(view.VISIBLE);
                        pricelist_spinner.setVisibility(view.VISIBLE);
                        getOrg();
                    }
                    else if(selectedOrg.equals("Feed")){
                        District_TextView.setVisibility(view.VISIBLE);
                        District_spinner1.setVisibility(view.VISIBLE);
                        location_TextView.setVisibility(view.VISIBLE);
                        Location_Spinner.setVisibility(view.VISIBLE);
                        org_TextView.setVisibility(view.GONE);
                        orgSpinner.setVisibility(view.GONE);
                        pricelist_text.setVisibility(view.GONE);
                        pricelist_spinner.setVisibility(view.GONE);
                        getWHNew();
                        //getWH(); //2022-05-16
                        // getThana();
                        // branchSpinner.setEmptyView(null); //setVisibility(view.GONE);
                    }
                    else{
                        District_TextView.setVisibility(view.VISIBLE);
                        District_spinner1.setVisibility(view.VISIBLE);
                        location_TextView.setVisibility(view.VISIBLE);
                        Location_Spinner.setVisibility(view.VISIBLE);
                        org_TextView.setVisibility(view.GONE);
                        orgSpinner.setVisibility(view.GONE);
                        pricelist_text.setVisibility(view.GONE);
                        pricelist_spinner.setVisibility(view.GONE);
                        //getWH(); //2022-05-16
                        // getThana();
                        //branchSpinner.setEmptyView(null); //setVisibility(view.GONE);
                    }

                    getOrderType(); //stop 2022-05-17
                    if(!actType.equals("")) {
                        actTypeSpinner.setSelection(((ArrayAdapter) actTypeSpinner.getAdapter()).getPosition(actType));//2022-10-11
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
    private void selectBPCustomerSpinner() {
        CustomerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedBPCustomerName=selectedOrg;
                    selectedBPCustomerID = hashBPID.get(selectedBPCustomerName);
                    Cursor cursor = mDatabaseHelper.getCustomerAddress(selectedBPCustomerID);

                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            String bpaddress = cursor.getString(0);
                            String bpphone = cursor.getString(1);
                            if(!bpaddress.equals("null") && bpaddress!= null && !bpaddress.isEmpty()){
                                dealer_amt_editText.setText(bpaddress);
                            }
                            else{
                                dealer_amt_editText.getText().clear();
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

    private void selectPriceListSpinner(){
        pricelist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedPriceListName=selectedOrg;
                    //selectedPriceListId = hashPriceListID.get(selectedOrg);

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //end selectSpinner()

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void onBackPressed1() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        toastMessage("do you surely want to logout?");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
        Cursor cursor = mDatabaseHelper.getProductOrderheadNew(LoginActivity.ad_user_id);
        if (cursor.moveToFirst()){
            Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
            startActivity(i);
        }
        else{
            //savealert();
            //2022-10-13
            if (selectedProdActivityID.equals("1")){
                Intent i=new Intent(OrgBranchSelection.this,SelectionAcitvity.class);
                startActivity(i);
            }
            else {
                savealert();
            }
        }
    }
    public void toast(String msg)
    {
        Toast.makeText(OrgBranchSelection.this,msg,Toast.LENGTH_SHORT).show();
    }

    public void ShowAlert(String title,String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(OrgBranchSelection.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.kfg_logo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public void callCancelLogOut(View view) {
        cancelalert();
        //Intent i=new Intent(ProductOrderLine.this,LoginActivity.class);
        //startActivity(i);
    }
    //mDatabaseHelper.deleteProductOrderHeadNew();
    //                    mDatabaseHelper.deleteAllProductOrderLine();

    public void callCancelDeleteOrder(View view) {
        cancelOrderalert();
        //mDatabaseHelper.deleteProductOrderHeadNew();
        //                    mDatabaseHelper.deleteAllProductOrderLine();
    }

    public void cancelalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrgBranchSelection.this);
        builder1.setMessage("Are you sure you want to LogOut?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i=new Intent(OrgBranchSelection.this,LoginActivity.class);
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
    public void cancelOrderalert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrgBranchSelection.this);
        builder1.setMessage("Are you sure you want to Delete this Order?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        mDatabaseHelper.deleteProductOrderHeadNew();
                        mDatabaseHelper.deleteAllProductOrderLine();
                        mDatabaseHelper.deleteProductOrderHeadNewEdit(TrxType);
                        mDatabaseHelper.deleteAllProductOrderLineEdit(TrxType);
                        Intent i=new Intent(OrgBranchSelection.this,OrgBranchSelection.class);
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
    //2024-08-13 newly added for save and restore
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save EditText content
        outState.putString("sdate", showdate.getText().toString());
        outState.putString("truckno", truckno_editText.getText().toString());
        outState.putString("dealerloc", dealer_amt_editText.getText().toString());
        // Save selected spinner values
        outState.putInt("actType", actTypeSpinner.getSelectedItemPosition());
        outState.putInt("prodType", prodTypeSpinner.getSelectedItemPosition());
        outState.putInt("customer", CustomerSpinner.getSelectedItemPosition());
        outState.putInt("wh", branchSpinner.getSelectedItemPosition());
        outState.putInt("DistrictName", District_spinner1.getSelectedItemPosition());
        outState.putInt("ThanaName", Location_Spinner.getSelectedItemPosition());
        outState.putInt("org", orgSpinner.getSelectedItemPosition());
        outState.putInt("pricelist", pricelist_spinner.getSelectedItemPosition());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore data
        truckno_editText.setText(savedInstanceState.getString("truckno"));
        dealer_amt_editText.setText(savedInstanceState.getString("dealerloc"));
        showdate.setText(savedInstanceState.getString("sdate"));

        actTypeSpinner.setSelection(savedInstanceState.getInt("actType"));
        prodTypeSpinner.setSelection(savedInstanceState.getInt("prodType"));
        CustomerSpinner.setSelection(savedInstanceState.getInt("customer"));
        branchSpinner.setSelection(savedInstanceState.getInt("wh"));
        District_spinner1.setSelection(savedInstanceState.getInt("DistrictName"));
        Location_Spinner.setSelection(savedInstanceState.getInt("ThanaName"));
        orgSpinner.setSelection(savedInstanceState.getInt("org"));
        pricelist_spinner.setSelection(savedInstanceState.getInt("pricelist"));
    }
//2024-08-14

}