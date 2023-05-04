package com.invocify.invoice.repository;

import com.invocify.invoice.entity.LineItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository extends JpaRepository<LineItem, UUID> {}
