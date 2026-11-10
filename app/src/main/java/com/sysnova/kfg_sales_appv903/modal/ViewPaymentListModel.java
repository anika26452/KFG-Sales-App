package com.sysnova.kfg_sales_appv903.modal;

public class ViewPaymentListModel {
    String docno;
    String dealer;
    String status;
    String kfgbank;
    String paymentamt;
    String t_app_payment_id;
    String paymentdate;

    public ViewPaymentListModel(String docno, String dealer, String status, String kfgbank, String paymentamt, String t_app_payment_id,String paymentdate) {
        this.docno = docno;
        this.dealer = dealer;
        this.status = status;
        this.kfgbank = kfgbank;
        this.paymentamt = paymentamt;
        this.t_app_payment_id = t_app_payment_id;
        this.paymentdate = paymentdate;
    }

    public String getOrderno() {
        return docno;
    }

    public void setOrderno(String orderno) {
        this.docno = orderno;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getKfgbank() {
        return kfgbank;
    }

    public void setKfgbank(String kfgbank) {
        this.kfgbank = kfgbank;
    }

    public String getPaymentamt() {
        return paymentamt;
    }

    public void setPaymentamt(String paymentamt) {
        this.paymentamt = paymentamt;
    }

    public String getT_app_payment_id() {
        return t_app_payment_id;
    }

    public void setT_app_payment_id(String t_app_payment_id) {
        this.t_app_payment_id = t_app_payment_id;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }
}
