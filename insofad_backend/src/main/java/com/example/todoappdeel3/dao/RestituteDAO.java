package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.dto.RestituteDTO;
import com.example.todoappdeel3.enums.RestituteDamage;
import com.example.todoappdeel3.enums.RestituteStatus;
import com.example.todoappdeel3.models.CustomUser;
import com.example.todoappdeel3.models.Order;
import com.example.todoappdeel3.models.OrderProduct;
import com.example.todoappdeel3.models.Restitute;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestituteDAO {

    private static final double LIGHT_DAMAGE_PERCENTAGE = 0.90;
    private static final double HEAVY_DAMAGE_PERCENTAGE = 0.85;

    private final RestituteRepository restituteRepository;
    private final UserRepository userRepository;
    private final OrderDAO orderDao;
    private final OrderProductRepository orderProductRepository;

    public RestituteDAO(
            RestituteRepository restituteRepository,
            UserRepository userRepository,
            OrderDAO orderDao,
            OrderProductRepository orderProductRepository
    ) {
        this.restituteRepository = restituteRepository;
        this.userRepository = userRepository;
        this.orderDao = orderDao;
        this.orderProductRepository = orderProductRepository;
    }

    public List<Restitute> getRestitutes() {
        return restituteRepository.findAll();
    }

    public List<Restitute> getUserRestitutes() {
        CustomUser user = getAuthenticatedUser();

        return user.getRestitutes();
    }

    public List<Order> getUserUnrestitutedOrders() {
        CustomUser user = getAuthenticatedUser();
        List<OrderProduct> unrestitutedOrders = orderProductRepository.findUnrestitutedOrderProductsByUser(user);

        Map<Order, List<OrderProduct>> groupedProducts = unrestitutedOrders
                .stream()
                .collect(Collectors.groupingBy(OrderProduct::getOrder));

        return groupedProducts
                .entrySet()
                .stream()
                .peek((entry) -> entry.getKey().setOrderProducts(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public Restitute createRestitute(List<RestituteDTO> request) {
        CustomUser user = getAuthenticatedUser();

        Restitute restitute = new Restitute();
        List<OrderProduct> products = request
                .stream()
                .map((restituteDto) -> {
                    Order order = orderDao.findOrderById(restituteDto.getOrderId());
                    OrderProduct orderProduct = order
                            .getOrderProducts()
                            .stream()
                            .filter((op) -> op.getId().equals(restituteDto.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("OrderProduct with id: '%s' was not found".formatted(restituteDto.getProductId())));

                    orderProduct.setRestituted(true);
                    orderProduct.setRestitute(restitute);
                    orderProduct.setDamage(restituteDto.getDamage());

                    return orderProduct;
                })
                .toList();

        double totalPrice = calculateTotalPrice(products);

        restitute.setUser(user);
        restitute.setProducts(products);
        restitute.setTotalPrice(totalPrice);
        restitute.setStatus(RestituteStatus.PROCESSING);

        return restituteRepository.save(restitute);
    }

    public Restitute updateRestituteStatus(Long restituteId, RestituteStatus status) {
        Restitute restitute = restituteRepository
                .findById(restituteId)
                .orElseThrow(() -> new EntityNotFoundException("Restitute with id: '%s' was not found".formatted(restituteId)));

        restitute.setStatus(status);

        return restituteRepository.save(restitute);
    }

    public CustomUser getAuthenticatedUser() {
        try {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            return userRepository.findByEmail(token.getName());
        } catch (ClassCastException e) {
            return null;
        }
    }

    private double calculateTotalPrice(List<OrderProduct> restituteProducts) {
        double totalPrice = 0.0;

        for (OrderProduct product : restituteProducts) {
            double productBasePrice = product.getProduct().getPrice();
            double totalVariantAddedPrice = product.getTotalVariantAddedPrice();
            double productPriceWithVariants = productBasePrice + totalVariantAddedPrice;

            switch (product.getDamage()) {
                case RestituteDamage.UNDAMAGED:
                    totalPrice += productPriceWithVariants;
                    break;
                case RestituteDamage.LIGHT_DAMAGE:
                    totalPrice += productPriceWithVariants * LIGHT_DAMAGE_PERCENTAGE;
                    break;
                case RestituteDamage.HEAVY_DAMAGE:
                    totalPrice += productPriceWithVariants * HEAVY_DAMAGE_PERCENTAGE;
                    break;
                default:
                    throw new IllegalArgumentException("RestituteDamage with type: '%s' was not found".formatted(product.getDamage()));
            }
        }

        return totalPrice;
    }
}
