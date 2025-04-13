package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.OrderProduct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class OrderProductDAO {
    private final OrderProductRepository orderProductRepository;

    private final ProductRepository productRepository;

    public OrderProductDAO(OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    public OrderProduct getOrderdedProduct(long id){
        Optional<OrderProduct> orderProduct  = this.orderProductRepository.findById(id);

        if(orderProduct.isPresent()) {
            return orderProduct.get();
        }  else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "De gevraagde bestelde product is niet gevonden"
            );
        }

    }



}
