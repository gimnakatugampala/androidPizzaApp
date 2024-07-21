package com.example.pizzaorderingapp.Model;

public class PromoCode {

    private int id;
    private String promoCode;
    private double discountPercentage;
    private String expiryDate;

    public PromoCode() {}

    public PromoCode(int id, String promoCode, double discountPercentage, String expiryDate) {
        this.id = id;
        this.promoCode = promoCode;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                "id=" + id +
                ", promoCode='" + promoCode + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }
}
