package com.example.todoappdeel3.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity(name = "Users")
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "customUser")
    @JsonManagedReference
    public List<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    public List<Restitute> restitutes;

    public CustomUser() {
    }

    public CustomUser(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = "ROLE_USER";  // Default role  //ROLE_ IS A MUST FOR SPRING SECURITY
    }

    public CustomUser(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Restitute> getRestitutes() {
        return restitutes;
    }

    public void setRestitutes(List<Restitute> restitutes) {
        this.restitutes = restitutes;
    }
}
