package com.edublitz.orderservice;

import com.edublitz.orderservice.model.Order;
import com.edublitz.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Test
    void repositoryShouldFindOrder() {

        Order order = new Order();

        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(order));

        Optional<Order> result =
                orderRepository.findById("test-id");

        assertTrue(result.isPresent());

        verify(orderRepository, times(1))
                .findById("test-id");
    }

    @Test
    void repositoryShouldReturnEmptyWhenOrderDoesNotExist() {

        when(orderRepository.findById(any()))
                .thenReturn(Optional.empty());

        Optional<Order> result =
                orderRepository.findById("invalid-id");

        assertFalse(result.isPresent());

        verify(orderRepository, times(1))
                .findById("invalid-id");
    }
}
