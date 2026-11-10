package com.sysnova.kfg_sales_appv903.modal;

public class ApprovalProductDetailsModal {
    String productName;
    String kgQty;
    String Uom;
    String bagQty;
    String rate;
    String totalTaka;

    public ApprovalProductDetailsModal(String productName, String Uom, String bagQty, String rate, String totalTaka) {
        this.productName = productName;
        //this.kgQty = kgQty;
        this.Uom=Uom;
        this.bagQty = bagQty;
        this.rate = rate;
        this.totalTaka = totalTaka;
    }

    public String getProductName() {
        return productName;
    }

    /*public String getKgQty() {
        return kgQty;
    }*/
    public String getUom() {
        return Uom;
    }

    public String getBagQty() {
        return bagQty;
    }

    public String getRate() {
        return rate;
    }

    public String getTotalTaka() {
        return totalTaka;
    }
}
