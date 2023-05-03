package com.invocify.invoice.repository;

import com.invocify.invoice.entity.LineItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, UUID> {}
