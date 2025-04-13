package com.example.todoappdeel3.dao;


import com.example.todoappdeel3.dto.*;
import com.example.todoappdeel3.models.*;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OptionsRepository optionsRepository;

    private final OrderProductVariantRepository orderProductRepository;



    public ProductDAO(ProductRepository repository, CategoryRepository category, ProductVariantRepository productVariantRepository, OptionsRepository optionsRepository, OrderProductVariantRepository orderProductRepository) {
        this.productRepository = repository;
        this.categoryRepository = category;
        this.productVariantRepository = productVariantRepository;
        this.optionsRepository = optionsRepository;
        this.orderProductRepository = orderProductRepository;

    }

    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public List<Product> getProducts(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }


    public Product getProduct(long id){
        Optional<Product> product = this.productRepository.findById(id);

        if(product.isPresent()) {
            return product.get();
        }  else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "De gevraagde product is niet gevonden"
            );
        }

    }


    public boolean changeQuantityProduct(long id ){
        Product product = getProduct(id);

        int productQuantity = product.getQuantity();

        productQuantity = productQuantity - 1;

        product.setQuantity(productQuantity);

        return false;

    }


    public List<Product> getProductVariants(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }

    public List<Product> getOptions(List<Long> ids){
        return this.productRepository.findAllById(ids);
    }


    @Transactional
    public void createProduct(@NotNull Product product){
        this.categoryRepository.save(product.getCategory());
        this.productRepository.save(product);
    }

    public void updateProduct(Long id, ProductDTO productDTO){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setName(productDTO.name);
            product.get().setDescription(productDTO.description);
            product.get().setQuantity(productDTO.quantity);

            this.productRepository.save(product.get());
        }
    }

    public void checkProduct(Long id) {
        this.toggleProduct(id, true);
    }

    public void uncheckProduct(Long id) {
        this.toggleProduct(id, false);
    }

    private void toggleProduct(Long id, boolean value){
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isPresent()){
            product.get().setFinished(value);
            this.productRepository.save(product.get());
        }
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

    public void deleteVariantOptions( DeleteVariantOptionsDTO deleteVariantOptionsDTO){

        for(long options_id : deleteVariantOptionsDTO.option_ids) {
            this.optionsRepository.deleteById(options_id);
        }

        for(long variant_id : deleteVariantOptionsDTO.variant_ids) {
            ProductVariant productVariant = this.productVariantRepository.getById(variant_id);
            for(Options options : productVariant.getOptions()) {
                optionsRepository.deleteById(options.getId());
            }
            this.productVariantRepository.deleteById(variant_id);
        }

    }

    public void AddVariantOptions(Long productId, ProductDTO productDTO) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("No Product Found"));

        Set<ProductVariant> productVariants = prepareProductVariants(product, productDTO);

        product.setVariants(productVariants);

        ProductVariant newVariant = productVariants.stream().max(Comparator.comparingLong(ProductVariant::getId)).orElseThrow(() -> new NoSuchElementException("No id found"));
        newVariant.setId(0);
        this.productVariantRepository.save(newVariant);

        for (Options options : newVariant.getOptions()) {
            this.optionsRepository.save(options);
        }

        this.productRepository.save(product);
    }

    public void updateProductVariant(Long productId, ProductDTO productDTO) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("No Product Found"));

        Set<ProductVariant> productVariants = prepareProductVariants(product, productDTO);

        for (ProductVariant variant : productVariants) {
            this.productVariantRepository.save(variant);

            for (Options options : variant.getOptions()) {
                this.optionsRepository.save(options);
            }
        }
    }

    private Set<ProductVariant> prepareProductVariants(Product product, ProductDTO productDTO) {
        Set<ProductVariant> productVariants = new HashSet<>();

        for (ProductVariantDTO variantDTO : productDTO.getVariants()) {
            ProductVariant variant = new ProductVariant(variantDTO.getId(), variantDTO.getName(), variantDTO.getDescription(), product);
            Set<Options> optionsList = new HashSet<>();

            for (OptionsDTO optionsDTO : variantDTO.getOptions()) {
                Options options = new Options(optionsDTO.getName(), optionsDTO.getAdded_price(), variant);
                optionsList.add(options);
            }

            variant.setOptions(optionsList);
            productVariants.add(variant);
        }

        return productVariants;
    }


    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
