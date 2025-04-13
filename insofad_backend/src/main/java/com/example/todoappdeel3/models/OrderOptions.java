package com.example.todoappdeel3.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.example.todoappdeel3.models.OrderProductVariant;

import java.util.List;

@Entity
public class OrderOptions {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private double added_price;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "OrderProductVariant_id")
    public OrderProductVariant orderProductVariant;

    @ManyToMany
    @JsonBackReference
    private List<Order> order;

    public OrderOptions(String name, double added_price, OrderProductVariant orderProductVariant) {
        this.name = name;
        this.added_price = added_price;
        this.orderProductVariant = orderProductVariant;
    }

    public OrderOptions(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAdded_price() {
        return added_price;
    }

    public void setAdded_price(double added_price) {
        this.added_price = added_price;
    }


    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }


    public OrderProductVariant getOrderProductVariant() {
        return orderProductVariant;
    }

    public void setOrderProductVariant(OrderProductVariant orderProductVariant) {
        this.orderProductVariant = orderProductVariant;
    }
}
