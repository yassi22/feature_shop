package com.example.todoappdeel3.models;

import com.example.todoappdeel3.enums.RestituteDamage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orderProducts")
public class OrderProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private Order order;

    @JsonBackReference
    @ManyToOne
    private Restitute restitute;

    @ManyToOne
    @JsonBackReference
    private Product product;

    @OneToMany
    private List<OrderProductVariant> orderProductVariants;

    private String imageUrl;

    private String name;

    private double price;

    private boolean isRestituted = false;

    private RestituteDamage damage;
    @Column(nullable = false)
    private double top_up_amount;
    @ElementCollection
    private Set<String> cardsToppedUp = new HashSet<>();

    @ElementCollection
    private Set<String> appliedGiftCards = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Order> products = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "giftcard_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "giftcard_id"))
    private Set<GiftCard> giftCards = new HashSet<>();


    public OrderProduct() { }

    public OrderProduct(
            Product product,
            List<OrderProductVariant> orderProductVariants
    ) {
        this.product = product;
        this.orderProductVariants = orderProductVariants;
        this.imageUrl = product.getImageURL();
    }

    public OrderProduct(Long id, Order order, Restitute restitute, Product product, List<OrderProductVariant> orderProductVariants, String imageUrl, String name, double price, boolean isRestituted, RestituteDamage damage, double top_up_amount, Set<String> cardsToppedUp, Set<String> appliedGiftCards, Set<Order> products, Set<GiftCard> giftCards) {
        this.id = id;
        this.order = order;
        this.restitute = restitute;
        this.product = product;
        this.orderProductVariants = orderProductVariants;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.isRestituted = isRestituted;
        this.damage = damage;
        this.top_up_amount = top_up_amount;
        this.cardsToppedUp = cardsToppedUp;
        this.appliedGiftCards = appliedGiftCards;
        this.products = products;
        this.giftCards = giftCards;

    }
    public OrderProduct(Product product) {
        this.product = product;
        this.imageUrl = product.getImageURL();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public double getTotalVariantAddedPrice() {
        return orderProductVariants.stream()
                .mapToDouble(OrderProductVariant::getAddedPrice)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Restitute getRestitute() {
        return restitute;
    }

    public void setRestitute(Restitute restitute) {
        this.restitute = restitute;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<OrderProductVariant> getOrderProductVariants() {
        return orderProductVariants;
    }

    public void setOrderProductVariants(List<OrderProductVariant> orderProductVariants) {
        this.orderProductVariants = orderProductVariants;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRestituted() {
        return isRestituted;
    }

    public void setRestituted(boolean restituted) {
        isRestituted = restituted;
    }

    public RestituteDamage getDamage() {
        return damage;
    }

    public void setDamage(RestituteDamage damage) {
        this.damage = damage;
    }

    public double getTop_up_amount() {
        return top_up_amount;
    }

    public void setTop_up_amount(double top_up_amount) {
        this.top_up_amount = top_up_amount;
    }

    public Set<String> getCardsToppedUp() {
        return cardsToppedUp;
    }

    public void setCardsToppedUp(Set<String> cardsToppedUp) {
        this.cardsToppedUp = cardsToppedUp;
    }

    public Set<String> getAppliedGiftCards() {
        return appliedGiftCards;
    }

    public void setAppliedGiftCards(Set<String> appliedGiftCards) {
        this.appliedGiftCards = appliedGiftCards;
    }

    public Set<Order> getProducts() {
        return products;
    }

    public void setProducts(Set<Order> products) {
        this.products = products;
    }

    public Set<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(Set<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

}
