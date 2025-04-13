package com.example.todoappdeel3.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private int discount;
    private LocalDate expiryDate;
    private String type;
    private double minimumAmount;
    private int usageCount = 0;

    public PromoCode() {
    }

    public PromoCode(String code, int discount, LocalDate expiryDate, String type, double minimumAmount) {
        this.code = code;
        this.discount = discount;
        this.expiryDate = expiryDate;
        this.type = type;
        this.minimumAmount = minimumAmount;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getUsageCount() {
        return usageCount;
    }
    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }
    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}

