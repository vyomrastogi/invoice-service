package com.invocify.invoice.repository;

import com.invocify.invoice.entity.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, UUID> {

}
