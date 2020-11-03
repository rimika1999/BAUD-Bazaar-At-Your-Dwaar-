package com.suvidha.bazaaratyourdwaar;

import java.util.List;

public class UserInformation {
    private String userName,userEmail,userPhoneNumber,userJoinDate,userScore,userPassword;
    private List<UserAddress> userDeliveryAddresses;
    private List<OrderedProduct> orderedProducts;
    private List<ProductInformation> favouriteProducts;
    private List<CreditCards> creditCards;
    private List<DebitCards> debitCards;




    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserInformation()
    {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDate(String userJoinDate) {
        this.userJoinDate = userJoinDate;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public List<UserAddress> getUserDeliveryAddresses() {
        return userDeliveryAddresses;
    }

    public void setUserDeliveryAddresses(List<UserAddress> userDeliveryAddresses) {
        this.userDeliveryAddresses = userDeliveryAddresses;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public List<ProductInformation> getFavouriteProducts() {
        return favouriteProducts;
    }

    public void setFavouriteProducts(List<ProductInformation> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }

    public List<CreditCards> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCards> creditCards) {
        this.creditCards = creditCards;
    }

    public List<DebitCards> getDebitCards() {
        return debitCards;
    }

    public void setDebitCards(List<DebitCards> debitCards) {
        this.debitCards = debitCards;
    }
}
