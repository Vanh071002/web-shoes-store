package com.example.demo.control.payment;

public class OrderDetail {
    private String productName;
    private float subtotal;
    private float shipping;
    private float tax;
    private float total;
    private String address;
    private String username;
    private String phone;

    public OrderDetail(String productName, String subtotal,
                       String shipping, String tax, String total, String address, String username, String phone) {
        this.productName = productName;
        this.subtotal = Float.parseFloat(subtotal);
        this.shipping = Float.parseFloat(shipping);
        this.tax = Float.parseFloat(tax);
        this.total = Float.parseFloat(total);
        this.address = address;
        this.username = username;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getProductName() {
        return productName;
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal);
    }

    public String getShipping() {
        return String.format("%.2f", shipping);
    }

    public String getTax() {
        return String.format("%.2f", tax);
    }

    public String getTotal() {
        return String.format("%.2f", total);
    }
}