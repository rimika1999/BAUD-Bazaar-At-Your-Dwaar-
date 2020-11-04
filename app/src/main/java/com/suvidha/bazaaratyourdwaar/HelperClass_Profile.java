package com.suvidha.bazaaratyourdwaar;

public class HelperClass_Profile {
    String identificationKey,name,address,district,state,pincode,email,phonenumber;

    public HelperClass_Profile( String identificationKey, String name, String address, String district, String state, String pincode, String email, String phonenumber ) {
        this.identificationKey = identificationKey;
        this.name = name;
        this.address = address;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public HelperClass_Profile() {

    }

    public String getIdentificationKey() {
        return identificationKey;
    }

    public void setIdentificationKey( String identificationKey ) {
        this.identificationKey = identificationKey;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict( String district ) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode( String pincode ) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber( String phonenumber ) {
        this.phonenumber = phonenumber;
    }
}
