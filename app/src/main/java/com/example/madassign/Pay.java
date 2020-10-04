package com.example.madassign;

public class Pay {
    private String Cardname;
    private String Paymenttype;
    private String Cardtype;
    private int Cardnumber;
    private String txtDate;

    public Pay() {
    }

    public String getCardname() {
        return Cardname;
    }

    public void setCardname(String cardname) {
        Cardname = cardname;
    }

    public String getPaymenttype() {
        return Paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        Paymenttype = paymenttype;
    }

    public String getCardtype() {
        return Cardtype;
    }

    public void setCardtype(String cardtype) {
        Cardtype = cardtype;
    }

    public int getCardnumber() {
        return Cardnumber;
    }

    public void setCardnumber(int cardnumber) {
        Cardnumber = cardnumber;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }
}
