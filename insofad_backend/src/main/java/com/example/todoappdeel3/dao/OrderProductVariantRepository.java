package com.example.todoappdeel3.dao;


import com.example.todoappdeel3.models.OrderProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductVariantRepository extends JpaRepository<OrderProductVariant, Long> {


}
