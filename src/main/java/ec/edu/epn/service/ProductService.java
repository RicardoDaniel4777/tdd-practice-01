package ec.edu.epn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.epn.model.Product;
import ec.edu.epn.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product with sku '" + sku + "' not found"));
    }

    public List<Product> findActiveProducts() {
        return productRepository.findByActiveTrue();
    }
}