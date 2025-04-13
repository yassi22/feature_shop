package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.Restitute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestituteRepository extends JpaRepository<Restitute, Long> {

}
