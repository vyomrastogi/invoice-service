package com.invocify.invoice.repository;

import com.invocify.invoice.entity.Invoice;
import java.util.Date;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

  Page<Invoice> findByLastModifiedDateAfter(
      Pageable pageAble, @Param(value = "lastModifiedDate") Date filterDate);
}
