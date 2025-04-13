package com.example.todoappdeel3.dao;


import com.example.todoappdeel3.dto.OptionsDTO;
import com.example.todoappdeel3.dto.OrderDTO;
import com.example.todoappdeel3.dto.ProductDTO;
import com.example.todoappdeel3.dto.ProductVariantDTO;
import com.example.todoappdeel3.models.*;
import com.example.todoappdeel3.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDAO {

    private final OrderRepository orderRepository;

    private final ProductDAO productDAO;

    private final UserRepository userRepository;

    private final ProductService productService;

    private final CategoryDAO categoryDAO;

    private final OptionsDAO optionsDAO;

    private final OrderOptionsDAO orderOptionsDAO;

    private final OrderOptionsRepository orderOptionsRepository;

    private final ProductVariantDAO productVariantDAO;

    private final OrderProductVariantDAO orderProductVariantDAO;

    private final OrderProductVariantRepository orderProductVariantRepository;

    private final OrderProductRepository orderProductRepository;


    public OrderDAO(OrderRepository orderRepository, ProductDAO productDAO, UserRepository userRepository, ProductService productService, CategoryDAO categoryDAO, OptionsDAO optionsDAO,
                    ProductVariantDAO productVariantDAO, OrderProductVariantDAO orderProductVariantDAO, OrderProductVariantRepository orderProductVariantRepository, OrderProductRepository orderProductRepository,
                    OrderOptionsDAO orderOptionsDAO, OrderOptionsRepository orderOptionsRepository) {
        this.orderRepository = orderRepository;
        this.productDAO = productDAO;
        this.userRepository = userRepository;
        this.productService = productService;
        this.categoryDAO = categoryDAO;
        this.optionsDAO = optionsDAO;
        this.productVariantDAO = productVariantDAO;
        this.orderProductVariantDAO = orderProductVariantDAO;
        this.orderProductVariantRepository = orderProductVariantRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderOptionsDAO = orderOptionsDAO;
        this.orderOptionsRepository = orderOptionsRepository;
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }


    @Transactional
    public void createOrder(OrderDTO orderDTO) {

        List<Product> productList = new ArrayList<>();

        List<OrderProduct> orderProducts = new ArrayList<>();

        List<OrderProductVariant> totalOrderProductVariantList = new ArrayList<>();


        List<OrderOptions> totalOrderOptionsList = new ArrayList<>();


        System.out.println(orderDTO.products);
        for (ProductDTO productJson : orderDTO.products) {
            System.out.println(productJson);
            Product product = productDAO.getProduct(productJson.id);


            OrderProduct orderProduct = new OrderProduct(product);

            productDAO.changeQuantityProduct(productJson.id);

            productList.add(product);

            List<OrderProductVariant> orderProductVariantList = new ArrayList<>();
            for (ProductVariantDTO productVariantJson : productJson.variants) {
                List<OrderOptions> orderOptionsList = new ArrayList<>();
                // Ontvang de productVariant van productJson.
                ProductVariant productVariant = productVariantDAO.getProductVariant(productVariantJson.id);
                OrderProductVariant orderProductVariant = orderProductVariantDAO.convertProductVariantToOrderProductVariant(productVariant, orderProduct);

                // Converteer productVariant naar OrderProductVariant.
                for (OptionsDTO optionsJson : productVariantJson.options) {
                    Options option = optionsDAO.getOption(optionsJson.id);
                    OrderOptions orderOptions = orderOptionsDAO.convertOptionToOrderOption(option, orderProductVariant);

                    orderOptionsList.add(orderOptions);
                    totalOrderOptionsList.add(orderOptions);
                }
                orderProductVariant.setOrderOptions(orderOptionsList);
                orderProductVariantList.add(orderProductVariant);
                totalOrderProductVariantList.add(orderProductVariant);
            }

            orderProduct.setOrderProductVariants(orderProductVariantList);
            orderProducts.add(orderProduct);
        }


        System.out.println("test buiten de for loop");

        System.out.println("order price " + orderDTO.orderPrice);

        Order order = new Order(productService.makeName(productList), orderDTO.orderPrice, LocalDateTime.now());
        order.setCustomUser(userRepository.findByEmail(orderDTO.email));
        this.orderRepository.saveAndFlush(order);

        for(OrderOptions orderOptions : totalOrderOptionsList){
            orderOptionsRepository.save(orderOptions);
        }


        for(OrderProductVariant orderProductVariant : totalOrderProductVariantList) {
            orderProductVariantRepository.save(orderProductVariant);
        }

        for(OrderProduct orderProduct : orderProducts ) {
            orderProduct.setOrder(order);
            orderProductRepository.save(orderProduct);
        }

        order.setOrderProducts(orderProducts);
        this.orderRepository.save(order);

    }

    public Order findOrderById(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: '%s' was not found".formatted(orderId)));
    }

}
