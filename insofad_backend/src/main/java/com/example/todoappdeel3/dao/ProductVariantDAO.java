package com.example.todoappdeel3.dao;

import com.example.todoappdeel3.models.Product;
import com.example.todoappdeel3.models.ProductVariant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class ProductVariantDAO {

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantDAO(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    public List<ProductVariant> getAllProductVariant(){
        return this.productVariantRepository.findAll();
    }

    @GetMapping
    public ProductVariant getProductVariant(long id){
        Optional<ProductVariant> productVariant = this.productVariantRepository.findById(id);

        if(productVariant.isPresent()) {
            return productVariant.get();
        }  else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "De gevraagde product variant is niet gevonden"
            );
        }

    }




}
