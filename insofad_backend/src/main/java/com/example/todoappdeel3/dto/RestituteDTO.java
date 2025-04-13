package com.example.todoappdeel3.dto;

import com.example.todoappdeel3.enums.RestituteDamage;

public class RestituteDTO {

    private Long orderId;
    private Long productId;
    private RestituteDamage damage;

    public RestituteDTO() { }

    public RestituteDTO(
            Long orderId,
            Long productId,
            RestituteDamage damage
    ) {
        this.orderId = orderId;
        this.productId = productId;
        this.damage = damage;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public RestituteDamage getDamage() {
        return damage;
    }

    public void setDamage(RestituteDamage damage) {
        this.damage = damage;
    }
}
