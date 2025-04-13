package com.example.todoappdeel3.dao;


import com.example.todoappdeel3.models.OrderProduct;
import com.example.todoappdeel3.models.OrderProductVariant;
import com.example.todoappdeel3.models.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class OrderProductVariantDAO {

    public OrderProductVariant convertProductVariantToOrderProductVariant(ProductVariant productVariant, OrderProduct orderProduct){

        OrderProductVariant orderProductVariant = new OrderProductVariant(productVariant.getName(), productVariant.getDescription(),
                orderProduct
        );

        return orderProductVariant;

    }

}
