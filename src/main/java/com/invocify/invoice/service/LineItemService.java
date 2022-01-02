package com.invocify.invoice.service;

import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.repository.LineItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LineItemService {

    private LineItemRepository lineItemRepository;

    public LineItem createLineItem(LineItem lineItem) {
        return lineItemRepository.save(lineItem);
    }
}
