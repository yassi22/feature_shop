package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.OrderOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderOptionsRepository extends JpaRepository<OrderOptions, Long> {
}
