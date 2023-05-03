package com.invocify.invoice.service.helper;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.repository.CompanyRepository;
import java.util.UUID;

public class InvocifyServiceHelper {

  public Company getCompanyOrThrowException(CompanyRepository repository, UUID companyId)
      throws InvalidCompanyException {
    return repository.findById(companyId).orElseThrow(() -> new InvalidCompanyException(companyId));
  }
}
