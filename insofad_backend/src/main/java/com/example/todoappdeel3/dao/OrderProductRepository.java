package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.CustomUser;
import com.example.todoappdeel3.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op FROM OrderProduct op WHERE op.order.customUser = :user AND op.isRestituted = false")
    List<OrderProduct> findUnrestitutedOrderProductsByUser(@Param("user") CustomUser user);
}
