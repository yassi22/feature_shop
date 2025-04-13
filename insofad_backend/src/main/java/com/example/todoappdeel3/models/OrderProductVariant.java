package com.example.todoappdeel3.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.example.todoappdeel3.models.OrderOptions;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "OrderProductVariant")
public class OrderProductVariant {

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    @JsonBackReference
    private List<Order> order;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private OrderProduct orderProduct;

    @OneToMany(mappedBy = "orderProductVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    public List<OrderOptions> orderOptions;

    private String name;

    private String description;

    public OrderProductVariant() { }

    public OrderProductVariant(
            String name,
            String description,
            OrderProduct orderProduct
    ) {
        this.name = name;
        this.description = description;
        this.orderProduct = orderProduct;
    }

    public double getAddedPrice() {
        return orderOptions.stream()
                .mapToDouble(OrderOptions::getAdded_price)
                .sum();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public List<OrderOptions> getOrderOptions() {
        return orderOptions;
    }

    public void setOrderOptions(List<OrderOptions> orderOptions) {
        this.orderOptions = orderOptions;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }


}

