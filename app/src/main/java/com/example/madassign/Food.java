package com.example.madassign;

public class Food {
    private String Name;
    private String Address;
    private String Email;
    private int Phoneno;
    private String txtDate;
    private String txtTime;

    public Food() {

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address= address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) { this.Email = email; }

    public int getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(int phoneno) {
        this.Phoneno = Phoneno;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }

    public String getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(String txtTime) {
        this.txtTime = txtTime;
    }

}

