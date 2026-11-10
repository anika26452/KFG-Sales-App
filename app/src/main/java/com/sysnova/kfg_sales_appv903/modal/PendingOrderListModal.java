package com.sysnova.kfg_sales_appv903.modal;

public class PendingOrderListModal {
    String productname;
    String productcatID;
    String productID;
    String customernamevalue;
    //int quantity;
    String quantity;
    String total;
    String uom;

    String docno;

    public PendingOrderListModal(String productname, String productcatID, String customernamevalue, String quantity, String total, String uom, String docno, String productID) {
        this.productID=productID;
        this.productname = productname;
        this.productcatID = productcatID;
        this.customernamevalue = customernamevalue;
        this.quantity = quantity;
        this.total = total;
        this.uom = uom;
        this.docno = docno;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductcatID() {
        return productcatID;
    }

    public void setProductcatID(String productcatID) {
        this.productcatID = productcatID;
    }

    public String getCustomernamevalue() {
        return customernamevalue;
    }

    public void setCustomernamevalue(String customernamevalue) {
        this.customernamevalue = customernamevalue;
    }

    /*public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }*/ //2021-12-22

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

}
