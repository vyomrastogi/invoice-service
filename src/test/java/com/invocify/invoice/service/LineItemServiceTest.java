package com.invocify.invoice.service;

import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.repository.LineItemRepository;
import com.invocify.invoice.service.LineItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LineItemServiceTest {

    LineItemRepository lineItemRepository;
    LineItemService lineItemService;

    @BeforeEach
    public void initialize() {
        lineItemRepository = mock(LineItemRepository.class);
        lineItemService = new LineItemService(lineItemRepository);
    }

    @Test
    public void createLineItem_flatRate() {

        LineItem expectedLineItem = LineItem.builder()
                .description("tdd")
                .rate(new BigDecimal(10.00).setScale(2, RoundingMode.HALF_EVEN))
                .rateType("flat")
                .id(UUID.randomUUID())
                .build();

        when(lineItemRepository.save(any(LineItem.class))).thenReturn(expectedLineItem);

        LineItem actualLineItem = lineItemService.createLineItem(expectedLineItem);
        assertEquals(expectedLineItem, actualLineItem);
        assertEquals(expectedLineItem.getRate(), actualLineItem.getTotalFees());
        verify(lineItemRepository, times(1)).save(any(LineItem.class));

    }

    @Test
    public void createLineItem_RateBased() {

        LineItem expectedLineItem = LineItem.builder()
                .description("tdd")
                .rate(new BigDecimal(10.00).setScale(2, RoundingMode.HALF_EVEN))
                .rateType("rate")
                .quantity(2)
                .id(UUID.randomUUID())
                .build();

        when(lineItemRepository.save(any(LineItem.class))).thenReturn(expectedLineItem);

        LineItem actualLineItem = lineItemService.createLineItem(expectedLineItem);
        assertEquals(expectedLineItem, actualLineItem);
        assertEquals(expectedLineItem.getRate().multiply(new BigDecimal(expectedLineItem.getQuantity())), actualLineItem.getTotalFees());
        verify(lineItemRepository, times(1)).save(any(LineItem.class));

    }
}
