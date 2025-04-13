package com.example.todoappdeel3.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDate;

public class PromoCodeDTO {
    public String code;
    public int discount;
    public String type;
    public double minimumAmount;
    @JsonAlias("expiry_date")
    public LocalDate expiryDate;

}
