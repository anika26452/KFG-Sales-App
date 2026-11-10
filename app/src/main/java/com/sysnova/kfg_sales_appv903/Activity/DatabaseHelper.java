package com.sysnova.kfg_sales_appv903.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "product";
    private static final String COL1 = "productID";
    private static final String COL2 = "productName";
    private static final String COL3 = "catagoryID";
    private static final String COL4 = "price";
    private static final String COL5 = "unit";
    private static final String COL6 = "availableQty";

    private static final String DATABASE_NAME = "KFG_Sales";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 10); //by anika

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createProductTable = "CREATE TABLE " + TABLE_NAME + " (productID TEXT Unique, " +
                COL2 +" TEXT, " +
                COL3 +" INTEGER, " +
                COL4 +" TEXT,"+COL5+" TEXT,"+COL6+" TEXT,onhandQty Text)";

        String createProductOrderHeadNewTable="CREATE TABLE ProductOrderHeadNew(prdId INTEGER PRIMARY KEY AUTOINCREMENT,ids TEXT ," +
                "orderdate TEXT,productType Text,activityType Text,District Text,Thana Text,org Text,wh Text,dealer Text," +
                "truck TEXT,pricelist TEXT,userID TEXT,trxType TEXT,DocumentNo TEXT)";

        String createProductOrderLineTable = "CREATE TABLE productorderline (productId INTEGER PRIMARY KEY, productCode TEXT , " +
                "productName TEXT,productQTY REAL,pricePerUnit REAL, totalCost REAL,docno Text,mproduct_Id text,userID TEXT,trxType TEXT,T_SalesOrderLine_id Text)";

        String createProductCatagoryTable="CREATE TABLE product_catagory(catagoryID INTEGER PRIMARY KEY," +
                "catagoryName TEXT,C_Activity_ID Text)";

        String createSalesOrderTable="CREATE TABLE sales_order(orderid INTEGER PRIMARY KEY AUTOINCREMENT,ids INTEGER," +
                "ad_org_id TEXT,m_warehouse_id TEXT,chemistid TEXT,userid TEXT,salestype Text," +
                "grandtotal TEXT,dateordered TEXT,documentno TEXT,C_Region_ID TEXT,C_City_ID Text," +
                "description Text,C_Activity_ID Text ,producttype Text,bpname Text,PriceListType Text,truckno Text,latitude Text,longitude Text,Geo_Location Text)";

        String createSalesOrderLineTable="CREATE TABLE sales_orderline(orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "ad_org_id TEXT,product_name Text,productID Text,qtyordered Text,uom Text,unit_price Text,orderid INTEGER,erpdocNo Text)";

        String createChemistProposalTable="CREATE TABLE chemist_proposal(chemistID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "shopName TEXT,shopAddress TEXT,tradeLicenseNo TEXT,drugLicenseNo TEXT,contactPerson TEXT," +
                "mobile TEXT,email TEXT,nearbyHospital TEXT,estimatedDailySales TEXT,ownerName TEXT,ownerAddress TEXT)";

        String createDrTable="CREATE TABLE dr_information(dr_code TEXT ," + "drName TEXT,dr_address TEXT)";

        String createDrInfoTable="CREATE TABLE dr_proposal(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,qualification TEXT" +
                ",institution TEXT,specialisation TEXT,designation TEXT,Phone TEXT,EMail TEXT," +
                "mailingAddress TEXT,DOB TEXT,homeDistrict TEXT,spouseName TEXT,marriageDay TEXT,likings TEXT,dislikings TEXT,chamberAddress TEXT,practicingDay TEXT," +
                "MobileNumber TEXT,visitingHour TEXT,visitingTimeMPO TEXT,avgPatientNo_day TEXT,call_month TEXT,dr_status TEXT,productname TEXT)";

        String createEmployeeTable="CREATE TABLE employee(emp_id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id TEXT,user_level TEXT,name Text,currentdate Text,feed_org_id Text,isEmployee Text)";

        String createChemistTable="CREATE TABLE chemist(chemist_code TEXT,chemist_name TEXT,chemist_address TEXT,customer_balance TEXT,phone Text)";

        String createPrescriptionTable="CREATE TABLE prescription(dr_code TEXT,product_name TEXT,image_name TEXT)";

        String createDCRTable="CREATE TABLE dcr(code TEXT,promo_tools TEXT,work_station TEXT)";

        String createPromoToolsTable="CREATE TABLE promo_tools(name TEXT,description TEXT)";

        String CreatePromoDetailsTable="CREATE TABLE promo(promo_id INTEGER PRIMARY KEY AUTOINCREMENT,bpcode TEXT,bpname TEXT,product_code TEXT," +
                "product_name TEXT,m_locatorto_id TEXT,m_warehouse_id TEXT,ad_user_id TEXT,movementqty INTEGER)";

        String createPromoOrderTable="CREATE TABLE promo_order(docNo TEXT not null unique," +
                "date TEXT,dr_code TEXT,status TEXT default 'draft',userid TEXT)";

        String createPromoOrderLineTable="CREATE TABLE promo_orderline(promo_orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "productID TEXT,quantity INTEGER)";

        String createOrganizationTable = "Create Table organization (id INTEGER PRIMARY KEY AUTOINCREMENT,ad_org_id Text,org_name Text,userid TEXT)";
        String createWarehouseTable="Create Table warehouse (id INTEGER PRIMARY KEY AUTOINCREMENT,warehouse_id Text,ad_org_id Text,warehouse_name Text,Region_ID Text,City_ID Text)";

        String createSalesTypeTable = "Create Table salesType(salesType_id INTEGER PRIMARY KEY AUTOINCREMENT,salesType_name Text,prodType text, businessProductType text, t_businessType_id text)";

        //String createOrderApprovalTable = "Create Table orderApproval(orderApproval_id INTEGER PRIMARY KEY AUTOINCREMENT,order_doc Text,erp_orderNo Text,grand_total Text,prepared_by text,sales_type Text,sent_date Text,doctype_id Text,orgid Text)";
        String createOrderApprovalTable = "Create Table orderApproval(orderApproval_id INTEGER PRIMARY KEY AUTOINCREMENT,order_doc Text,erp_orderNo Text,grand_total Text,prepared_by text,sales_type Text,sent_date Text,doctype_id Text,orgid Text)";
        String createApprovalTable = "Create Table tapproval (tapproval_id INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT,orgid Text, forwardto_id Text,discount Text,ispercentdiscount Text,msg text)";
        String createApprovalLineTable = "Create Table tapprovalline (tapprovalline_id INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT,erp_orderNo Text,proID Text,uom Text,productID TEXT)";

        String createSalesOrderRecord="CREATE TABLE sales_order_record(docNo TEXT," +
                "date TEXT,customer_name TEXT,grand_total Text,product_name Text,uom Text,ordered_qty Text default '0',pricePerProduct Text default '0',status Text,orgid text,description text)";
        String createSupervisorTable = "Create Table supervisor(userid Text,docid Text,supid Text,supname Text)";
        String createCustomerTable="Create Table customer(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id Text,customer_name  Text,code Text,customer_location Text,customer_phone Text, region_id text, t_businesstype_id text)";
        String createBPGroupTable = "Create Table bpgroup (id INTEGER PRIMARY KEY AUTOINCREMENT,c_bp_group_id Text,name Text)";
        //anika
        String createViewOrdertable = "Create Table ViewOrder(id INTEGER PRIMARY KEY AUTOINCREMENT,doc_no TEXT, date TEXT, grandtotal TEXT, user_id TEXT,bpname Text)";
        String createViewPaymenttable = "Create Table ViewPayment(id INTEGER PRIMARY KEY AUTOINCREMENT,doc_no TEXT, dealer TEXT, dealerbank TEXT, kfg_bankacc TEXT,paymentamt TEXT, createddate text, t_app_payment_id text, user_id text)";
        //anika
        String createProductTypeTable="CREATE TABLE ProductType(id INTEGER PRIMARY KEY AUTOINCREMENT,ProductType_ID TEXT,name TEXT)";

        String createDistrictTable="CREATE TABLE District(id INTEGER PRIMARY KEY AUTOINCREMENT,District_ID TEXT,name TEXT,wh_loc TEXT,bank_loc TEXT)";

        String createThanaTable="CREATE TABLE Thana(id INTEGER PRIMARY KEY AUTOINCREMENT,Thana_ID TEXT,name TEXT,Region_ID Text,wh_loc TEXT,bank_loc TEXT)";
        String createDBSyncTable="CREATE TABLE DB_Sync(id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id TEXT,sync TEXT)";
        String createBankNameTable="CREATE TABLE BankName(id INTEGER PRIMARY KEY AUTOINCREMENT,Thana_ID TEXT,name TEXT)";
        String createBranchNameTable="CREATE TABLE BranchName(id INTEGER PRIMARY KEY AUTOINCREMENT,Thana_ID TEXT,name TEXT)";
        String createDealerbankName="CREATE TABLE Dealerbankname(id INTEGER PRIMARY KEY AUTOINCREMENT,bank_id TEXT,bank_name TEXT)";
        String createkfgbankname="CREATE TABLE kfgbankname(id INTEGER PRIMARY KEY AUTOINCREMENT,bank_id TEXT,bank_name TEXT)";

        //2022-09-13

        db.execSQL(createProductTable);
        db.execSQL(createProductCatagoryTable);
        db.execSQL(createSalesOrderTable);
        db.execSQL(createSalesOrderLineTable);
        db.execSQL(createChemistProposalTable);
        db.execSQL(createDrTable);
        db.execSQL(createDrInfoTable);
        db.execSQL(createEmployeeTable);
        db.execSQL(createChemistTable);
        db.execSQL(createPrescriptionTable);
        db.execSQL(createDCRTable);
        db.execSQL(createPromoToolsTable);
        db.execSQL(CreatePromoDetailsTable);
        db.execSQL(createPromoOrderTable);
        db.execSQL(createPromoOrderLineTable);
        db.execSQL(createOrganizationTable);
        db.execSQL(createSalesTypeTable);
        db.execSQL(createOrderApprovalTable);
        db.execSQL(createApprovalTable);
        db.execSQL(createApprovalLineTable);
        db.execSQL(createSalesOrderRecord);
        db.execSQL(createSupervisorTable);
        db.execSQL(createWarehouseTable);
        db.execSQL(createCustomerTable);
        db.execSQL(createBPGroupTable);
        db.execSQL(createViewOrdertable); //anika
        db.execSQL(createViewPaymenttable); //anika
        db.execSQL(createProductOrderLineTable);
        db.execSQL(createProductOrderHeadNewTable);
        //db.execSQL(createProductOrderLineEditTable);
        //db.execSQL(createProductOrderHeadEditTable);
        db.execSQL(createProductTypeTable);
        db.execSQL(createDistrictTable);
        db.execSQL(createThanaTable);
        db.execSQL(createDBSyncTable);
        db.execSQL(createBankNameTable);     //2022-09-13
        db.execSQL(createBranchNameTable);     //2022-09-13
        db.execSQL(createDealerbankName);
        db.execSQL(createkfgbankname);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP  TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP  TABLE IF EXISTS  product_catagory");
        db.execSQL("DROP  TABLE IF EXISTS sales_order");
        db.execSQL("DROP  TABLE IF EXISTS sales_orderline");
        db.execSQL("DROP  TABLE IF EXISTS chemist_proposal");
        db.execSQL("DROP  TABLE IF EXISTS dr_information");
        db.execSQL("DROP  TABLE IF EXISTS dr_proposal");
        db.execSQL("DROP  TABLE IF EXISTS employee");
        db.execSQL("DROP  TABLE IF EXISTS chemist");
        db.execSQL("DROP  TABLE IF EXISTS prescription");
        db.execSQL("DROP  TABLE IF EXISTS dcr");
        db.execSQL("DROP  TABLE IF EXISTS promo_tools");
        db.execSQL("DROP  TABLE IF EXISTS promo");
        db.execSQL("DROP  TABLE IF EXISTS promo_order");
        db.execSQL("DROP  TABLE IF EXISTS promo_orderline");
        db.execSQL("DROP  TABLE IF EXISTS organization");
        db.execSQL("DROP  TABLE IF EXISTS salesType");
        db.execSQL("DROP  TABLE IF EXISTS orderApproval");
        db.execSQL("DROP  TABLE IF EXISTS tapproval");
        db.execSQL("DROP  TABLE IF EXISTS tapprovalline");
        db.execSQL("DROP  TABLE IF EXISTS sales_order_record");
        db.execSQL("DROP  TABLE IF EXISTS supervisor");
        db.execSQL("DROP  TABLE IF EXISTS warehouse");
        db.execSQL("DROP  TABLE IF EXISTS customer");
        db.execSQL("DROP  TABLE IF EXISTS bpgroup");
        db.execSQL("DROP  TABLE IF EXISTS createViewOrdertable"); //anika
        db.execSQL("DROP  TABLE IF EXISTS createViewPaymenttable"); //anika
        db.execSQL("DROP  TABLE IF EXISTS ProductOrderLine");
        db.execSQL("DROP  TABLE IF EXISTS ProductOrderHeadNew");
        //db.execSQL("DROP  TABLE IF EXISTS ProductOrderLineEdit");
        //db.execSQL("DROP  TABLE IF EXISTS ProductOrderHeadEdit");
        db.execSQL("DROP  TABLE IF EXISTS ProductType");
        db.execSQL("DROP  TABLE IF EXISTS District");
        db.execSQL("DROP  TABLE IF EXISTS Thana");
        db.execSQL("DROP  TABLE IF EXISTS DB_Sync");
        db.execSQL("DROP  TABLE IF EXISTS BankName"); //2022-09-13
        db.execSQL("DROP  TABLE IF EXISTS BranchName");  //2022-09-13
        onCreate(db);
    }
    //anika
    public Boolean insertIntoViewOrder(String doc_no, String date, String grandtotal, String user_id,String bpname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doc_no", doc_no);
        contentValues.put("date", date);
        contentValues.put("grandtotal", grandtotal);
        contentValues.put("user_id", user_id);
        contentValues.put("bpname", bpname);

        long result = db.insert("ViewOrder", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertIntoViewPayment(String doc_no, String dealer, String dealerbank, String kfg_bankacc, String paymentamt, String createddate, String t_app_payment_id, String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("doc_no", doc_no);
        contentValues.put("dealer", dealer);
        contentValues.put("dealerbank", dealerbank); //status
        contentValues.put("kfg_bankacc", kfg_bankacc);
        contentValues.put("paymentamt", paymentamt);
        contentValues.put("createddate", createddate);
        contentValues.put("t_app_payment_id", t_app_payment_id);
        contentValues.put("user_id", user_id);

        long result = db.insert("ViewPayment", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteViewOrder(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ViewOrder";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteViewPayment(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ViewPayment";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getPaymentFromViewPayment(String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ViewPayment where user_id='"+user_id+"'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getOrderFromViewOrder(String date, String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ViewOrder where date='"+date+"' and user_id='"+user_id+"'" ;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDistrictForPayment(String region_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where District_ID='"+region_id+"' and bank_loc is not null ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getDistrictForPaymentNew(String region_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where (District_ID='"+region_id+"' and bank_loc is not null) ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    //anika


    public boolean insertIntoBPGroup(String c_bp_group_id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("c_bp_group_id", c_bp_group_id);
        contentValues.put("name", name);

        long result = db.insert("bpgroup",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoOCustomer(String customer_id,String customer_name,String code,String customer_location,String customer_phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        contentValues.put("customer_location", customer_location);
        contentValues.put("customer_phone", customer_phone);
        long result = db.insert("customer",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOCustomerNew0512(String customer_id,String customer_name,String code,String balance,String creditlimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        contentValues.put("customer_location", balance);
        contentValues.put("customer_phone", creditlimit);

        long result = db.insert("customer",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOCustomerNew1(String customer_id,String customer_name,String code,String balance,String creditlimit, String region_id, String t_businesstype_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        contentValues.put("customer_location", balance);
        contentValues.put("customer_phone", creditlimit);
        contentValues.put("region_id", region_id);  //anika
        contentValues.put("t_businesstype_id", t_businesstype_id);  //anika

        long result = db.insert("customer",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOCustomerNew(String customer_id,String customer_name,String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);

        long result = db.insert("customer",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOrderRecord(String docNo,String date,String customer_name,String grand_total,String product_name,String uom,String ordered_qty,String pricePerProduct,String status,String orgid,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("customer_name", customer_name);
        contentValues.put("grand_total", grand_total);
        contentValues.put("product_name", product_name);
        contentValues.put("uom", uom);
        contentValues.put("ordered_qty", ordered_qty);
        contentValues.put("pricePerProduct", pricePerProduct);
        contentValues.put("status", status);
        contentValues.put("orgid", orgid);
        contentValues.put("description",description);


        long result = db.insert("sales_order_record",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOrderApproval(String order_doc,String erp_orderNo,String grand_total,String prepared_by,String sales_type,String sent_date,String doctype_id,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_doc", order_doc);
        contentValues.put("erp_orderNo", erp_orderNo);
        contentValues.put("grand_total", grand_total);
        contentValues.put("prepared_by", prepared_by);
        contentValues.put("sales_type", sales_type);
        contentValues.put("sent_date", sent_date);
        contentValues.put("doctype_id",doctype_id);
        contentValues.put("orgid",orgid);

        long result = db.insert("orderApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoSalesTypeNew(String typename,String id,String prodType, String businessProductType,
                                          String t_businessType_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);
        contentValues.put("salesType_id", id);
        contentValues.put("prodType", prodType);
        contentValues.put("businessProductType", businessProductType);
        contentValues.put("t_businessType_id", t_businessType_id);

        long result = db.insert("salesType", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoSalesType(String typename,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);
        contentValues.put("salesType_id", id);


        long result = db.insert("salesType", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoOrganization(String ad_org_id,String org_name,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("org_name", org_name);
        contentValues.put("userid", userid);

        long result = db.insert("organization", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2022-07-20
    public boolean insertIntoDB_Sync(String Sync,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Sync", Sync);
        contentValues.put("ad_user_id", userid);

        long result = db.insert("DB_Sync", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //end 2022-07-20

    public boolean insertIntoWarehouse(String warehouse_id,String ad_org_id,String warehouse_name,String Region_ID ,String City_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse_id", warehouse_id);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("warehouse_name", warehouse_name);
        contentValues.put("Region_ID", Region_ID);
        contentValues.put("City_ID", City_ID);


        long result = db.insert("warehouse", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoWarehouseOld(String warehouse_id,String ad_org_id,String warehouse_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse_id", warehouse_id);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("warehouse_name", warehouse_name);



        long result = db.insert("warehouse", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoEmployee(String ad_user_id,String user_level,String name,String currentdate,String feed_org_id,String isEmployee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("user_level", user_level);
        contentValues.put("name", name);
        contentValues.put("currentdate", currentdate);
        contentValues.put("feed_org_id", feed_org_id); //2022-10-03
        contentValues.put("isEmployee", isEmployee); //2024-10-06

        long result = db.insert("employee", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodrInfo(String dr_code,String name,String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("drName", name);
        contentValues.put("dr_address", address);



        long result = db.insert("dr_information",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoTools(String name,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", desc);



        long result = db.insert("promo_tools",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoSupervisor(String userid,String docid,String supid,String supname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("docid", docid);
        contentValues.put("supid", supid);
        contentValues.put("supname", supname);


        long result = db.insert("supervisor",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    /*
     * 2022-06-22 */

    public boolean insertIntoSalesOrderLine(String docNo,String ad_org_id,
                                            String product_name,String productID,String qtyordered,String uom,String unit_price,int orderid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("product_name", product_name);
        contentValues.put("productID", productID);
        contentValues.put("qtyordered",qtyordered);
        contentValues.put("uom",uom);
        contentValues.put("unit_price",unit_price);
        contentValues.put("orderid",orderid);
        //contentValues.put("erpdocNo",erpdocNo);

        long result = db.insert("sales_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoSalesOrder(int ids,String ad_org_id,String m_warehouse_id,String chemistid,String userid,String salestype,
                                        String grandtotal,String dateordered,String documentno,String C_Region_ID,String C_City_ID,String description,
                                        String C_Activity_ID,String producttype,String bpname,String PriceListType,String truckno
            ,String latitude,String longitude,String Geo_Location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", ids);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("chemistid", chemistid);
        contentValues.put("userid", userid);
        contentValues.put("salestype", salestype);
        contentValues.put("grandtotal", grandtotal);
        contentValues.put("dateordered", dateordered);
        contentValues.put("documentno", documentno);
        contentValues.put("C_Region_ID", C_Region_ID);

        contentValues.put("C_City_ID", C_City_ID);
        contentValues.put("description", description);
        contentValues.put("C_Activity_ID", C_Activity_ID);
        contentValues.put("producttype", producttype);
        contentValues.put("truckno", truckno);
        contentValues.put("bpname", bpname);
        contentValues.put("PriceListType", PriceListType);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("Geo_Location", Geo_Location);

        long result = db.insert("sales_order", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoTApproval(String docNo,String forwardto_id,String ispercentdiscount,String msg,String discount,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("discount", discount);
        contentValues.put("forwardto_id", forwardto_id);
        contentValues.put("orgid", orgid);
        contentValues.put("msg", msg);
        contentValues.put("isforward", ispercentdiscount);

        long result = db.insert("TApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoTApprovalLine(String erp_orderNo,String uom,String docNo,String proID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("erp_orderNo",erp_orderNo);
        contentValues.put("docNo", docNo);
        contentValues.put("unit_price",uom);
        contentValues.put("proID",proID);
        long result = db.insert("TApprovalline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoProduct(String productID,String productName,int catagoryID,String price,String unit,String avlQty,String onhandQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productID", productID);
        contentValues.put("productName", productName);
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("price", price);
        contentValues.put("unit", unit);
        contentValues.put("availableQty", avlQty);
        contentValues.put("onhandQty", onhandQty);
        long result = db.insert("product", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }




    public boolean insertIntoProductCatagory(int catagoryID,String catagoryName,String C_Activity_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("catagoryName", catagoryName);
        contentValues.put("C_Activity_ID", C_Activity_ID);

        long result = db.insert("product_catagory", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderHead1(String id,String productType,String activityType,String DistrictName,String ThanaName,String org,String wh,String dealer,String truckno,String pricelist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", id);
        contentValues.put("productType", productType);
        contentValues.put("activityType", activityType);
        contentValues.put("DistrictName", DistrictName);
        contentValues.put("ThanaName", ThanaName);
        contentValues.put("org", org);
        contentValues.put("wh", wh);
        contentValues.put("dealer",dealer);
        contentValues.put("truckno",truckno);
        contentValues.put("pricelist",pricelist);

        long result = db.insert("ProductOrderHead",null,contentValues);


        //if data as inserted incorrectly it will return -1
        if (result == -1) {


            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderHeadNew(String id,String productType,String activityType,String DistrictName,
                                                 String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String orderdate,String userID
            ,String trxType,String DocumentNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids", id);
        contentValues.put("orderdate",orderdate);
        contentValues.put("productType", productType);
        contentValues.put("activityType", activityType);
        contentValues.put("District", DistrictName);
        contentValues.put("Thana", ThanaName);
        contentValues.put("org", org);
        contentValues.put("wh", wh);
        contentValues.put("dealer",dealer);
        contentValues.put("truck",truckno);
        contentValues.put("pricelist",pricelist);
        contentValues.put("userID",userID);
        contentValues.put("trxType",trxType); //2024-10-19 added by shanzida for edit option
        contentValues.put("DocumentNo",DocumentNo); //2024-10-19 added by shanzida for edit option


        long result = db.insert("ProductOrderHeadNew",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoProductOrderLine(int id,String code,String name,double qty,double price,double total,String docno,String mproduct_Id,String userID){ //,String productorderhead_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("productCode", code);
        contentValues.put("productName", name);
        contentValues.put("productQTY", qty);
        contentValues.put("pricePerUnit", price);
        contentValues.put("totalCost", total);
        contentValues.put("docno",docno);
        contentValues.put("mproduct_Id",mproduct_Id);
        contentValues.put("userID",userID);

        long result = db.insert("ProductOrderLine",null,contentValues);
        //if data as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2024-11-07
    public boolean insertIntoProductOrderLineEdit(int id,String code,String name,double qty,double price,double total,String docno,String mproduct_Id,String userID,String TrxType,String T_SalesOrderLine_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("productCode", code);
        contentValues.put("productName", name);
        contentValues.put("productQTY", qty);
        contentValues.put("pricePerUnit", price);
        contentValues.put("totalCost", total);
        contentValues.put("docno",docno);
        contentValues.put("mproduct_Id",mproduct_Id);
        contentValues.put("userID",userID);
        contentValues.put("TrxType",TrxType);
        contentValues.put("T_SalesOrderLine_id",T_SalesOrderLine_id);

        long result = db.insert("ProductOrderLine",null,contentValues);
        //if data as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2024-11-07
    public boolean insertIntoProductType(String ProductType_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductType_ID", ProductType_ID);
        contentValues.put("name", name);

        long result = db.insert("ProductType",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoDistrict(String District_ID,String name,String wh_loc,String bank_loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("District_ID", District_ID);
        contentValues.put("name", name);
        contentValues.put("wh_loc", wh_loc);
        contentValues.put("bank_loc", bank_loc);

        long result = db.insert("District",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //2022-09-13
    public boolean insertIntoBranchName(String Thana_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);

        long result = db.insert("BranchName",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntokfgbankname(String bank_id,String bank_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bank_id);
        contentValues.put("bank_name", bank_name);

        long result = db.insert("kfgbankname",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodealerbankname(String bank_id,String bank_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bank_id", bank_id);
        contentValues.put("bank_name", bank_name);

        long result = db.insert("Dealerbankname",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getDealerbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT bank_id,bank_name FROM Dealerbankname order by bank_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteDealerbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  Dealerbankname";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletekfgbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  kfgbankname";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getkfgbankacc(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT bank_id,bank_name FROM kfgbankname order by bank_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public boolean insertIntoBankName(String Thana_ID,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);

        long result = db.insert("BankName",null,contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BankName order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBranchName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name FROM BranchName order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2022-09-13
    public boolean insertIntoThana(String Thana_ID,String name,String Region_ID,String wh_loc,String bank_loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Thana_ID", Thana_ID);
        contentValues.put("name", name);
        contentValues.put("Region_ID", Region_ID);
        contentValues.put("wh_loc", wh_loc);
        contentValues.put("bank_loc", bank_loc);


        long result = db.insert("Thana",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public void deleteProductType()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductType";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    //2022-09-13
    public void deletekfgBankName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BankName";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteBranchName(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  BranchName";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    //2022-09-13
    public Cursor getProductType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductType";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteDistrict()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  District";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    /*public Cursor getDistrict(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District";
        Cursor data = db.rawQuery(query, null);
        return data;
    }*/
    public Cursor getDistrict_Bank(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where bank_loc != '0' order by name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDistrict_Wh(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where wh_loc != '0' order by name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDistrictSelected(String Region){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT District_ID,name FROM District where name like '"+Region+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteThana()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  Thana";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getThana_banknew(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where ((Region_ID='"+Region_ID+"' and bank_loc != '0') or Thana_ID ='1') order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getThana_Bank(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where (Region_ID='"+Region_ID+"') and bank_loc is not null order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getThana_Wh(String Region_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where Region_ID='"+Region_ID+"' and wh_loc != '0' order by Thana_ID";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getThanaSelected(String thanaName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Thana_ID,name,Region_ID FROM Thana where name like '"+thanaName+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    //2022-04-20 added by shanzida
    //2022-07-20 added by shanzida
    public void deleteDB_Sync()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  DB_Sync";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getDB_Sync(String userid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM DB_Sync where ad_user_id like '"+userid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //end 2022-07-20
    public void deleteProductOrderLine()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderLine(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderline where userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2024-11-05
    public Cursor getProductOrderLineEdit(String documentNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderline where docno='"+documentNumber+"' and productQTY>0 order by productId desc";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllProductOrderLineEdit(String TrxType){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline where trxType ='"+TrxType+"'";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteEditProductOrderLine: query: " +query);
        db.execSQL(query);
    }
    //2024-11-05
    public void deleteAllProductOrderLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderLine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline WHERE productId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderLine: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderLine(int id,double newQTY,double newTotal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("productQTY", newQTY);
        args.put("totalCost", newTotal);

        int i = db.update("ProductOrderLine", args, "productId = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    public void deleteProductOrderHeadNew()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    //2024-11-06
    public void deleteProductOrderHeadNewEdit(String TrxType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew where trxType='"+TrxType+"'";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    //2024-11-06
    public Cursor getProductOrderheadNew(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductOrderHeadNew where userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getProductOrderheadNewEdit(String DocumentNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductOrderHeadNew where DocumentNo='"+DocumentNo+"' and trxType='Edit' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteAllProductOrderHeadNew(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderHead: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderHead(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadNew WHERE prodId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderhead: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderHeadNew(String id,String orderdate,String productType,String activityType,String DistrictName,String ThanaName,String org,String wh,String dealer,String truckno,String pricelist,String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("ids", id);
        args.put("orderdate",orderdate);
        args.put("productType", productType);
        args.put("activityType", activityType);
        args.put("District", DistrictName);
        args.put("Thana", ThanaName);
        args.put("org", org);
        args.put("wh", wh);
        args.put("dealer",dealer);
        args.put("truck",truckno);
        args.put("pricelist",pricelist);
        args.put("userID",userID);

        int i = db.update("ProductOrderHeadNew", args, "ids = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    //2022-05-17
    /*public void deleteProductOrderHeadEdit()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  ProductOrderHeadEdit";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderheadEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM ProductOrderHeadEdit";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteProductOrderLineEdit()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getProductOrderLineEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderlineEdit";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteAllProductOrderLineEdit(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderLineEdit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderlineEdit WHERE productId='"+id+"'";
        //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderLine: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderLineEdit(int id,int newQTY,double newTotal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("productQTY", newQTY);
        args.put("totalCost", newTotal);

        int i = db.update("ProductOrderLineEdit", args, "productId = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }*/
    //2022-05-17
    public List<String> getProductCategory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1)+" "+cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public boolean insertIntoDCR(String code,String promotools,String workstation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("promo_tools", promotools);
        contentValues.put("work_station", workstation);



        long result = db.insert("dcr", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoPrescription(String dr_code,String product,String image_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("product_name", product);
        contentValues.put("image_name", image_name);



        long result = db.insert("prescription", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoChemist(String chemist_code,String chemist_name,String address,String balance,String phoneNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chemist_code", chemist_code);
        contentValues.put("chemist_name", chemist_name);
        contentValues.put("chemist_address", address);
        contentValues.put("customer_balance", balance);
        contentValues.put("phone", phoneNo);

        long result = db.insert("chemist", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromo(String bp_code,String bp_name,String pr_code,String pr_name,String m_locator_id,String m_warehouse_id,String ad_userid,String movementqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bpcode", bp_code);
        contentValues.put("bpname", bp_name);
        contentValues.put("product_code", pr_code);
        contentValues.put("product_name", pr_name);
        contentValues.put("m_locatorto_id", m_locator_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("ad_user_id", ad_userid);
        contentValues.put("movementqty", movementqty);



        long result = db.insert("promo", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoChemistProposal(String chemistName,String shopAddress,String tradeLicenseNo,String drugLicenseNo,
                                             String contactPerson,String mobile,String email,String nearbyHospital,String estimatedDailySales,String ownerName,String ownerAddress)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", chemistName);
        contentValues.put("shopAddress", shopAddress);
        contentValues.put("tradeLicenseNo", tradeLicenseNo);
        contentValues.put("drugLicenseNo", drugLicenseNo);
        contentValues.put("contactPerson", contactPerson);
        contentValues.put("mobile", mobile);
        contentValues.put("email", email);
        contentValues.put("nearbyHospital", nearbyHospital);
        contentValues.put("estimatedDailySales", estimatedDailySales);
        contentValues.put("ownerName", ownerName);
        contentValues.put("ownerAddress", ownerAddress);



        long result = db.insert("chemist_proposal", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */


    public List<String> searchProduct(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and  product.productName LIKE '%"+name+"%' Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> searchProductByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product  inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and product.productID LIKE '%"+code+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public List<String> searchCustomerByName(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_name,code,customer_location,customer_phone FROM customer where customer_name LIKE '"+name+"%' Order by customer_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    //2022-04-16 shanzida
    public Cursor getCustomerdetails(String dealerCode) {
        String selectQuery = "SELECT * FROM customer where code||'-'||customer_name LIKE '" + dealerCode + "' limit 1 ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        return data;

        // closing connection
    }

    public Cursor getCustomerdetailsNew(String t_businesstype_id) {

//prev   //     String selectQuery = "SELECT customer_id,code,(case when customer_id='1' then customer_name else code||'-'||customer_name end) CustomerName,customer_location balance,customer_phone creditlimit FROM customer  order by CustomerName";

        //changes are done by anika on 2026-03-15
        String selectQuery = "SELECT customer_id,code,(case when customer_id='1' then customer_name else code||'-'||customer_name end) CustomerName,customer_location balance,customer_phone creditlimit FROM customer where t_businesstype_id= '"+t_businesstype_id+"' order by CustomerName";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }

    public Cursor getCustomer2() {

    String selectQuery = "SELECT customer_id,code,(case when customer_id='1' then customer_name else code||'-'||customer_name end) CustomerName,customer_location balance,customer_phone creditlimit FROM customer  order by CustomerName";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }

    //2021-11-18
    public Cursor getBPGroup() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM bpgroup  order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getProductCategoryCursor(String ConditionAct) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory where "+ConditionAct+" order by catagoryName ";

        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }
    public Cursor getProductCursor(String catID) {

        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT * FROM product order by productName";
        String query = "SELECT * FROM product where catagoryID='"+catID+"'order by productName";

        Log.d("DEBUG","query"+query);
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getCustomer(String t_businesstype_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT (case when customer_id='1' then customer_name else code||'-'||customer_name end) bp,customer_location balance,* FROM customer  where t_businesstype_id= '"+t_businesstype_id+"' order by bp  "; //limit 1
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor searchCustomerByNameNew(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist  where chemist_name LIKE '%"+name+"%' Order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getWarehouseByOrg(String ad_org_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM warehouse where ad_org_id='"+ad_org_id+"' Order by warehouse_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getWarehouseByRegion(String Region_ID,String City_ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM warehouse where Region_ID='"+Region_ID+"' and City_ID='"+City_ID+"' Order by warehouse_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getCustomerAddress(String name) {

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_location,customer_phone FROM customer where customer_id LIKE '" + name + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        return data;

        // closing connection
    }


    public String getCustomerAddressByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String address="";
        String selectQuery = "SELECT chemist_address FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                address=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return address;
    }

    public String getCustomerBalanceByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String balance="";
        String selectQuery = "SELECT customer_balance FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                balance=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return balance;
    }
    public Cursor searchCustomerByCodeNew(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT customer_name FROM " +
                "  where code = '"+code+"' Order by customer_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public List<String> searchCustomerByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        //customer_location,customer_phone newly added by shanzida 2021-11-16
        String selectQuery = "SELECT customer_name,code,customer_location,customer_phone FROM customer where code  LIKE '%"+code+"%' Order by customer_name";



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProduct(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProductCode(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productID FROM product Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<String> getAllPromoTools(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT name FROM promo_tools Order by name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public Cursor getSalesOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();

        // String query = "SELECT product_name,uom,ordered_qty,pricePerProduct,customer_name,orgid FROM sales_order_record where docNo='"+docNo+"'";
        String query = "SELECT * FROM sales_order_record where docNo='"+docNo+"'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataTemp(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataNew(int orderid,String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where orderid ="+orderid+" and docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getSalesOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM orderApproval";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM tApproval where isforward='N' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT productID,quantity FROM promo_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSupervisorData(String userid,String docid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT supid,supnmae FROM supervisor where userid='"+userid+"' and docid='"+docid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesType(String prodType){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name, businessProductType, t_businessType_id from salesType where prodType like '%"+prodType+"%' order by salesType_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesTypeCheck(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name from salesType order by salesType_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //anika
    public Cursor getDealer(String dealercode) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM customer where code = '"+dealercode+"'  order by customer_name  "; //limit 1
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getActivityType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_id,salesType_name, t_businessType_id from salesType";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //anika

    public String getUserIDFromEmployee(){


        String ad_user_id = null;

        // Select All Query
        String selectQuery = "select ad_user_id from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ad_user_id=(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return ad_user_id;
    }

    public List<String> getUserDetailsFromEmployee(){


        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "select ad_user_id,user_level,name from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
                labels.add(cursor.getString(2));
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public String getDrCodeByName(String name){


        String code = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                code=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return code;
    }

    public String getProductName(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT productName FROM product where productID='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }
    public String getProductNameFromPromo(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT product_name FROM promo where product_code='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }


    public String getChemistName(String chemId){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_code='"+chemId+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getDrNameByCode(String drID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information where dr_code='"+drID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getdrCodeByDocNo(String docNo){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM promo_order where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }



    public List<String> getAllDrNames(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public String getChemistID(String docNo){


        String id =null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM sales_order where documentno='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getTotalPrice(String docNo){


        String total = null;
        //total_price convert to new BigDecimal 2021-11-17 by shanzida
        BigDecimal total_price=new BigDecimal(0);
        // Select All Query
        String selectQuery = "select qtyordered  from sales_orderline where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String qty = cursor.getString(0);

                total_price = total_price.add(new BigDecimal(qty)); //total_price convert to new BigDecimal 2021-11-17 by shanzida
            } while (cursor.moveToNext());
            total= String.valueOf(total_price);
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return total;
    }

    public String getOrderedProduct(String docNo){


        String name1 = "";

        // Select All Query
        String selectQuery = "select product_name from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=(cursor.getString(0));
                name1 += name + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name1;
    }

    public String getOrderedProductQty(String docNo){


        String qty = "";

        // Select All Query
        String selectQuery = "select qtyordered from sales_orderline where docno='"+docNo+"' order by product_name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String qty1=(cursor.getString(0));
                qty += qty1 + "\n";
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }


    public int getCatagoryID(String catagoryName){


        int id = 0;

        // Select All Query
        String selectQuery = "SELECT catagoryID FROM product_catagory where catagoryName='"+catagoryName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getProductIDByProductName(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT productID FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductIDByProductNameFromPromo(String productName){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT product_code FROM promo where product_name='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getDrCodeByNameAddress(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"' and dr_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }
    public String getChemistCodeByName(String name,String Address){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"' and chemist_address='"+Address+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }

    public String getChemistIDByName(String name){


        String id = null;

        // Select All Query
        String selectQuery = "SELECT chemist_code FROM chemist where chemist_name='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                id=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return id;
    }


    public String getProductQty(String productName){


        String avlQty=null;

        // Select All Query
        String selectQuery = "SELECT availableQty FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                avlQty=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return avlQty;
    }
    public String getonHandProductQty(String productName){


        String avlQty=null;

        // Select All Query
        String selectQuery = "SELECT onhandQty FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                avlQty=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return avlQty;
    }



    public String getProductPrice(String productName){


        String Price=null;

        // Select All Query
        String selectQuery = "SELECT price FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Price=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return Price;
    }


    public String getProductUnit(String productName){
        String unit=null;
        String selectQuery = "SELECT unit FROM product where productName='"+productName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                unit=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return unit;
    }

    public int getAvailableQty(String productCode){


        int qty=0;

        // Select All Query
        String selectQuery = "SELECT movementqty FROM promo where product_code='"+productCode+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                qty=(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return qty;
    }




    public List<String> getAllProductByCatagory(int catagoryID){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product where catagoryID="+catagoryID+" Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }






    public List<String> getAllProductCatagory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public boolean getSalesOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public boolean getPromoOrderLineByDocAndProduct(String docNo,String productID){

        boolean res=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM promo_orderline where docNo='"+docNo+"' and productID='"+productID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToNext()){

            res=true;
        }
        else{
            res=false;
        }
        return res;

    }

    public Cursor getSalesOrderByDate(String date1,String date2){
        SQLiteDatabase db = this.getWritableDatabase();

        // String query = "SELECT * FROM sales_order where date='"+date+"' and status!='draft' and userid='"+userID+"'";

        // String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date+"'";
        String query = "SELECT DISTINCT docNo,date,customer_name,status,orgid FROM sales_order_record where date between '"+date1+"' and '"+date2+"' ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesOrderByDateAndStatus(String date1,String date2,String status){
        SQLiteDatabase db = this.getWritableDatabase();

        // String query = "SELECT * FROM sales_order where date='"+date+"' and status!='draft' and userid='"+userID+"'";

        // String query = "SELECT DISTINCT docNo,date,customer_name FROM sales_order_record where date between '"+date1+"' and '"+date+"'";
        String query = "SELECT DISTINCT docNo,date,customer_name,status,orgid FROM sales_order_record where date between '"+date1+"' and '"+date2+"' and status='"+status+"'";
        Cursor data = db.rawQuery(query, null);
        return data;

    }


    public List<String> getAllChemists(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */



    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2021-11-20 shanzida

    public boolean updateOrderDes(String docno,String Des){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("description",Des);

        int i = db.update("sales_order", args, "documentno = ?", new String[]{docno});
        if( i > 0){
            return true;
        }
        return false;

    }





    //end
    public void updateName(String newProduct,String newItem,String newQuantity, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newProduct + "',"+COL3+ "= '"+newItem+"', "+COL4+" = '"+newQuantity+ "' WHERE " + COL1 + " = " + id+"" ;

        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newItem);
        db.execSQL(query);


    }

    public boolean updateOrderStatus(String docno){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("status", "complete");

        int i = db.update("sales_order", args, "documentno = ?", new String[]{docno});
        if( i > 0){
            return true;
        }
        return false;

    }

    public boolean updatePromoQty(String product_code,int qty){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("movementqty", qty);

        int i = db.update("promo", args, "product_code = ?", new String[]{product_code});
        if( i > 0){
            return true;
        }
        return false;

    }
    public void deleteBPGroup(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from bpgroup";

        db.execSQL(q);
    }

    public void deleteSupervisorData(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from supervisor";

        Log.d(TAG, "deleteName: query: " + q);
        // Log.d(TAG, "deleteName: Deleting " + item + " from database.");
        db.execSQL(q);
    }

    public void deleteCustomer1(){
        SQLiteDatabase db = this.getWritableDatabase();

        String q="delete from customer";

        db.execSQL(q);
    }
    public void deleteCustomer(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  customer";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }



    public void deleteName(int id, String item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = " + id + "";


        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + item + " from database.");
        db.execSQL(query);
    }

    public void deleteSalesOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoOrderLine(String id, String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_orderline WHERE docNo='"+docNo+"' AND productID='"+id+"'";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteSalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  salesType";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteProductCatagory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product_catagory";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public Cursor getSalesOrder(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_order where documentno='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesOrderUser(String userID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_order where userid='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSalesOrderID(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT orderid FROM sales_order where documentno='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getPromoOrder(String docNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT dr_code,date FROM promo_order where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public Cursor getDrDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM dr_information order by drName ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoProduct(String ad_user_id,String ad_warehouse_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT product_code,product_name,movementqty FROM promo order by product_name ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getChemistDetails() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getOrganization() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT ad_org_id,org_name FROM organization";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrganizationUserID(String UserID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT userid FROM organization where userid like '" + UserID + "' limit 1";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrganizationSelected(String OName) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT ad_org_id,org_name FROM organization where org_name like '"+OName+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getBranch() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT branch_name,branch_id FROM organization where branch_name != 'TRANSIT WH' and branch_name != 'RAW MATERIALS WH'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getSkuWiseStock(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select productName,availableQty,unit from product \n" +
                "inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getTotalOrderPrice(String docNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select sum(unit_price) from sales_orderline where docNo = '" + docNo + "' ", null);
        return res;
    }

    public void deletechemist() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  chemist";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletedr() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  dr_information";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTools() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo_tools";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deletePromoTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  promo";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  organization";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteWarehouse() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  warehouse";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  employee";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrderApprovalLine(String erp_doc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  orderApproval where erp_orderNo = '"+erp_doc+"'  ";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteOrdertApprovalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  tApproval";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteOrdertApprovalLine(String erp_doc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  tApproval where erp_orderNo = '"+erp_doc+"'  ";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteSalesOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order ";
        db.execSQL(query);
    }
    public Cursor Currentdate(String ad_user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select currentdate from employee where ad_user_id='"+ad_user_id+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteSalesOrderLine() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline";
        db.execSQL(query);
    }

    public void deleteSalesOrderRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order_record";
        db.execSQL(query);
    }
    //2022-09-20
    public Cursor getProductOrderLineProduct(String userID,String selectedProdId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Distinct mproduct_Id FROM productorderline where mproduct_Id='"+selectedProdId+"' and userID='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2022-09-25
    public void deleteSalesOrderDocno(String docNo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_order where documentno='"+docNo+"'";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteSalesOrderLineDocno( String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  sales_orderline where docNo='"+docNo+"'";
        // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }
    //2022-10-01
    public Cursor getSalesOrderNoUser(String userID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(orderid) FROM sales_order where userid='"+userID+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //2022-10-13
    public Cursor getPendingViewOrder(String docno_id,String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT   Distinct customer.code as bpCode,customer.customer_name bpName,  \n" +
                "     null AS phone1,\n" +
                "\t sales_order.dateordered\n" +
                "\t ,warehouse.warehouse_name AS warehouse1,  \n" +
                "    null totalopenbalance,  \n" +
                "    null AS rate,  \n" +
                "    null uomsymbol,grandtotal AS old_kg,  \n" +
                "    null product,\n" +
                "    sales_order.documentno,null as des,  \n" +
                "    sales_order.ids  \n" +
                "    ,grandtotal  KG   \n" +
                "    ,grandtotal QTY   \n" +
                "    ,grandtotal as MT_QTY  \n" +
                "    ,customer_location bp_location,\n" +
                "    organization.org_name orgname,chemistid,truckno,\n" +
                "    sales_order.producttype,\n" +
                "    sales_order.description,\n" +
                "    (select District.name from District where District.District_id=sales_order.C_Region_ID limit 1) district1,\n" +
                "    (select Thana.name from Thana  where Thana.Thana_id=sales_order.C_City_ID limit 1) thana1,\n" +
                "    (select salesType_name from salesType where salesType.salesType_id=sales_order.C_Activity_ID limit 1) activity\n" +
                "    ,null erp_docno,sales_order.userid\n" +
                "\n" +
                "FROM  \n" +
                "    sales_order  \n" +
                "    LEFT JOIN sales_orderline ON ( sales_order.documentno = sales_orderline.docNo)   \n" +
                "    LEFT JOIN customer ON ( customer.customer_id = sales_order.chemistid )    \n" +
                "    LEFT JOIN warehouse ON ( sales_order.m_warehouse_id = warehouse.warehouse_id )  \n" +
                "    LEFT JOIN organization on (sales_order.ad_org_id=organization.ad_org_id) \n" +
                "    \n" +
                "WHERE  (sales_orderline.productID is not null)  and sales_order.documentno='"+docno_id+"' \n" +
                " and sales_order.userid='"+userID+"' \n" +
                " order by sales_order.ids";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPendingViewOrderLine(String documentno,String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select  mp.productName as prodName,'KG' as uom,\n" +
                "        sum(coalesce(round(cast(tordrl.qtyordered as int),2),0)) qtyordered,\n" +
                "        coalesce(round(tordrl.unit_price,2),0) price,\n" +
                "        round(sum(coalesce(cast(tordrl.qtyordered as int),0)),2) totaltk\n" +
                "                        \t\n" +
                "From    sales_order tordr\n" +
                "        LEFT Join sales_orderline tordrl on (tordr.documentno=tordrl.docNo)\n" +
                "        LEFT Join product mp on (mp.productID=tordrl.productID)\n" +
                "        \n" +
                "Where   tordr.documentno='"+documentno+"' \n" +
                " and tordr.userid='"+userID+"' " +
                " Group by tordrl.unit_price,prodName,uom\n" +
                " Order by prodName";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderUserdocwise(String userID,String documentno) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_order where userid='"+userID+"' and documentno='"+documentno+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    //

    public void exec(String s) {
    }

}