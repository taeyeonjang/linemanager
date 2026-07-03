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

    public void createProduct() {
        String serialNumber = getSerialNumber();

        Product product = Product.create(serialNumber);

        productRepository.save(product);
    }

    private String getSerialNumber() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    public void updateFinalResult(Product product, ResultState result) {
        product.update(result, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Optional<Product> findOne(String serialNumber) {
        return productRepository.findBySerialNumber(serialNumber);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
