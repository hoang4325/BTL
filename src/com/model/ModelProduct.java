
package com.model;

public class ModelProduct {
    private String productId;
    private String productName;
    private byte[] image;
    private String status;

    public ModelProduct() {
    }

    public ModelProduct(String productId, String productName, byte[] image, String status) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.status = status;

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    
}
