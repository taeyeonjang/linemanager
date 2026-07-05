package com.tenten.linemanager.service;

import com.tenten.linemanager.domain.Product;
import com.tenten.linemanager.domain.ResultState;
import com.tenten.linemanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct() {
        return createProduct(getSerialNumber());
    }

    public Product createProduct(String serialNumber) {

        if(productRepository.findBySerialNumber(serialNumber).isPresent()) {
            throw new IllegalStateException("이미 존재하는 시리얼번호입니다.");
        }

        Product product = Product.create(serialNumber);

        productRepository.save(product);

        return product;
    }

    private String getSerialNumber() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    public void updateCurrentProcessNo(Long productId, int processNo) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.updateCurrentProcessNo(processNo);
    }

    public void updateLineStatus(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("없는 Product 입니다, productId =" + productId));
        product.updateLineStatus();
    }

    public void updateFinalResult(Long productId, ResultState result) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.updateFinal(result);
    }



    @Transactional(readOnly = true)
    public Optional<Product> findOne(String serialNumber) {
        return productRepository.findBySerialNumber(serialNumber);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

//    public void init() {
//        productRepository.init();
//    }

}
