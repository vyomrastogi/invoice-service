package com.invocify.invoice.repository;

import com.invocify.invoice.entity.Company;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
  List<Company> findByActiveTrue();
}
