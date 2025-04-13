package com.example.todoappdeel3.models;

import com.example.todoappdeel3.enums.RestituteStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Restitute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne
    private CustomUser user;

    @JsonManagedReference
    @OneToMany(mappedBy = "restitute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

    private double totalPrice;

    private RestituteStatus status;

    public Restitute() { }

    public Restitute(
            Long id,
            LocalDateTime createdAt,
            CustomUser user,
            List<OrderProduct> products,
            double totalPrice,
            RestituteStatus status
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RestituteStatus getStatus() {
        return status;
    }

    public void setStatus(RestituteStatus status) {
        this.status = status;
    }
}
