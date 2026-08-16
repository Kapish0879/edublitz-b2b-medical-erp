package com.edublitz.productservice;

import com.edublitz.productservice.model.Product;
import com.edublitz.productservice.repository.ProductRepository;
import com.edublitz.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void repositoryShouldFindProduct() {

        Product product = new Product();

        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));

        Optional<Product> result =
                productRepository.findById("test-id");

        assertTrue(result.isPresent());

        verify(productRepository, times(1))
                .findById("test-id");
    }

    @Test
    void repositoryShouldReturnEmptyWhenProductDoesNotExist() {

        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        Optional<Product> result =
                productRepository.findById("invalid-id");

        assertFalse(result.isPresent());

        verify(productRepository, times(1))
                .findById("invalid-id");
    }
}
