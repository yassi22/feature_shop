package com.example.todoappdeel3.controller;

import com.example.todoappdeel3.dao.OrderDAO;
import com.example.todoappdeel3.dto.OrderDTO;
import com.example.todoappdeel3.models.Order;
import com.example.todoappdeel3.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderDAO orderDao;

    private final OrderService orderService;


    public OrderController(
            OrderDAO orderDao,
            OrderService orderService
    ) {
        this.orderService = orderService;
        this.orderDao = orderDao;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderDao.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            this.orderDao.createOrder(orderDTO);

            System.out.println("Order created with orderPrice " + orderDTO.orderPrice);
            System.out.println("Order created with email " + orderDTO.email);
            return ResponseEntity.ok("Order created");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping ("/user/{userId}")
    public ResponseEntity <List<Order>> getOrderUser(@PathVariable Long userId) {
        try {
            List<Order> order = this.orderService.findOrderUser(userId);

            return ResponseEntity.ok(order);
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
