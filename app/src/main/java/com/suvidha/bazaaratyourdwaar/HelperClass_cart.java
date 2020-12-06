package com.suvidha.bazaaratyourdwaar;

import java.util.List;

public class HelperClass_cart {

    public List<String> products;
    public int number_of_items;

    public HelperClass_cart() {
        this.number_of_items=0;
    }

    public HelperClass_cart( List<String> products, int number_of_items ) {
        this.products = products;
        this.number_of_items = number_of_items;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts( List<String> products ) {
        this.products = products;
    }

    public int getNumber_of_items() {
        return number_of_items;
    }

    public void setNumber_of_items( int number_of_items ) {
        this.number_of_items = number_of_items;
    }
}
