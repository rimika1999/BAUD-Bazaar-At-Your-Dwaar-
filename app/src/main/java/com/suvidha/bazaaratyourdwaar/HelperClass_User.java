package com.suvidha.bazaaratyourdwaar;

public class HelperClass_User {
    String username,email,phone,password,identificationKey,logIn_time,logOut_time;

    public HelperClass_User( String username, String email,String phone, String password,String identificationKey,String logIn_time,String logOut_time ) {
        this.identificationKey=identificationKey;
        this.username = username;
        this.email = email;
        this.phone = phone;
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

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
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
