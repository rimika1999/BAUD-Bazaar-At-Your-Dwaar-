package com.suvidha.bazaaratyourdwaar;

public class ProductInformation {
    private String productName,productId,productPrice,productDiscount,productRating,productPhotos;
    private ProductSeller productSeller;

    public ProductInformation( String productName, String productId, String productPrice, String productDiscount, String productRating, String productPhotos ) {
        this.productName = productName;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productRating = productRating;
        this.productPhotos = productPhotos;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName( String productName ) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId( String productId ) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice( String productPrice ) {
        this.productPrice = productPrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount( String productDiscount ) {
        this.productDiscount = productDiscount;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating( String productRating ) {
        this.productRating = productRating;
    }

    public String getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos( String productPhotos ) {
        this.productPhotos = productPhotos;
    }
}
