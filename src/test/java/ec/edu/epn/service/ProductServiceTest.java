package ec.edu.epn.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ec.edu.epn.model.Category;
import ec.edu.epn.model.Product;
import ec.edu.epn.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    private Category defaultCategory;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        defaultCategory = new Category("Electronics", "Electronic items");
    }

    @Test
    void findBySku_Encuentra_Producto() {
        Product p = new Product("SKU_111", "Telefono", new BigDecimal("199.99"), 15, defaultCategory);
        when(productRepository.findBySku("SKU_111")).thenReturn(Optional.of(p));

        Product found = productService.findBySku("SKU_111");

        assertEquals("SKU_111", found.getSku());
    }

    @Test
    void findBySku_NoEncuentra_Producto() {
        when(productRepository.findBySku("NOPE")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.findBySku("NOPE"));
    }

    @Test
    void findActiveProducts_returnsOnlyActive() {
        Product p1 = new Product("A1", "Prod A", new BigDecimal("10.00"), 5, defaultCategory);
        Product p2 = new Product("B2", "Prod B", new BigDecimal("20.00"), 3, defaultCategory);
        p1.setActive(true);
        p2.setActive(true);

        when(productRepository.findByActiveTrue()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.findActiveProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> "A1".equals(p.getSku())));
        assertTrue(result.stream().anyMatch(p -> "B2".equals(p.getSku())));
    }
}
