package com.suvidha.bazaaratyourdwaar;

public class HelperClass_User {
    String username,email_phone,password,identificationKey,logIn_time,logOut_time;

    public HelperClass_User( String username, String email_phone, String password,String identificationKey,String logIn_time,String logOut_time ) {
        this.identificationKey=identificationKey;
        this.username = username;
        this.email_phone = email_phone;
        this.password = password;
        this.logIn_time = logIn_time;
        this.logOut_time = logOut_time;
    }

    public HelperClass_User() {
    }

    public String getIdentificationKey() {
        return identificationKey;
    }

    public void setIdentificationKey( String identificationKey ) {
        this.identificationKey = identificationKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getEmail_phone() {
        return email_phone;
    }

    public void setEmail_phone( String email_phone ) {
        this.email_phone = email_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getLogIn_time() {
        return logIn_time;
    }

    public void setLogIn_time( String logIn_time ) {
        this.logIn_time = logIn_time;
    }

    public String getLogOut_time() {
        return logOut_time;
    }

    public void setLogOut_time( String logOut_time ) {
        this.logOut_time = logOut_time;
    }
}
