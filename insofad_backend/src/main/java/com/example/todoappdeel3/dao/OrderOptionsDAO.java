package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.Options;
import com.example.todoappdeel3.models.OrderOptions;
import com.example.todoappdeel3.models.OrderProductVariant;
import org.springframework.stereotype.Component;

@Component
public class OrderOptionsDAO {



     public OrderOptions convertOptionToOrderOption(Options options, OrderProductVariant orderProductVariant){

         OrderOptions orderOptions = new OrderOptions(options.getName(), options.getAdded_price(), orderProductVariant );

         return orderOptions;

     }


}
