package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.CompanyDetailResponse;
import com.invocify.invoice.model.CompanyRequest;
import com.invocify.invoice.model.CompanyBaseResponse;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.service.helper.InvocifyServiceHelper;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService extends InvocifyServiceHelper {

    private CompanyRepository companyRepository;


    public Company createCompany(CompanyRequest companyRequest) {
        Company companyEntity = new Company();
        BeanUtils.copyProperties(companyRequest,companyEntity);
        return companyRepository.save(companyEntity);
    }


    public List<? extends CompanyBaseResponse> getAllCompanies(boolean includeDetail, boolean includeInactive) {
        List<Company> companies;
        if (includeInactive) {
            companies = companyRepository.findAll();
        } else {
            companies = companyRepository.findByActiveTrue();
        }


        if (includeDetail) {
            return companies.stream().map(company -> {
                CompanyDetailResponse companyDetail = new CompanyDetailResponse();
                BeanUtils.copyProperties(company, companyDetail);
                return companyDetail;
            }).collect(Collectors.toList());

        } else {
            return companies.stream().map(company -> {
                CompanyBaseResponse companySV = new CompanyBaseResponse();
                BeanUtils.copyProperties(company, companySV);
                return companySV;
            }).collect(Collectors.toList());
        }
    }

    public Company archiveCompany(UUID companyId) throws InvalidCompanyException {
        Company company = getCompanyOrThrowException(companyRepository, companyId);
        company.setActive(false);
        return companyRepository.save(company);
    }
    
    public Company modifyCompany(UUID companyId, CompanyRequest companyRequest) throws InvalidCompanyException {    
    	Company company = getCompanyOrThrowException(companyRepository, companyId);
    	BeanUtils.copyProperties(companyRequest, company);
    	return companyRepository.save(company);
    }
}
