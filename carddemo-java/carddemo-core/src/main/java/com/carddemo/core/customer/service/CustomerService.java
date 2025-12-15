package com.carddemo.core.customer.service;

import com.carddemo.core.customer.entity.Customer;
import com.carddemo.core.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for Customer operations - replaces COBOL programs:
 * - COCUSTIC.cbl (Customer Inquiry)
 * - COCUSTUC.cbl (Customer Update)
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findBySsn(String ssn) {
        return customerRepository.findBySsn(ssn);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    @Transactional(readOnly = true)
    public Page<Customer> searchByLastName(String lastName, Pageable pageable) {
        return customerRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Customer> searchByName(String name, Pageable pageable) {
        return customerRepository.searchByName(name, pageable);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByStateCode(String stateCode) {
        return customerRepository.findByStateCode(stateCode);
    }

    @Transactional(readOnly = true)
    public Page<Customer> findByStateCode(String stateCode, Pageable pageable) {
        return customerRepository.findByStateCode(stateCode, pageable);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByZipCode(String zipCode) {
        return customerRepository.findByZipCode(zipCode);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer create(Customer customer) {
        if (customer.getCustomerId() != null && customerRepository.existsById(customer.getCustomerId())) {
            throw new IllegalArgumentException("Customer already exists with ID: " + customer.getCustomerId());
        }
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        if (customer.getCustomerId() == null || !customerRepository.existsById(customer.getCustomerId())) {
            throw new IllegalArgumentException("Customer not found with ID: " + customer.getCustomerId());
        }
        return customerRepository.save(customer);
    }

    public void delete(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByMinFicoScore(Integer minScore) {
        return customerRepository.findByMinFicoScore(minScore);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByFicoScoreRange(Integer minScore, Integer maxScore) {
        return customerRepository.findByFicoScoreRange(minScore, maxScore);
    }

    @Transactional(readOnly = true)
    public List<Customer> findPrimaryCardHolders() {
        return customerRepository.findPrimaryCardHolders();
    }

    @Transactional(readOnly = true)
    public long countByStateCode(String stateCode) {
        return customerRepository.countByStateCode(stateCode);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersInRange(Long startId, Long endId) {
        return customerRepository.findCustomersInRange(startId, endId);
    }

    public Customer updateFicoScore(Long customerId, Integer ficoScore) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        customer.setFicoCreditScore(ficoScore);
        return customerRepository.save(customer);
    }

    public Customer updateAddress(Long customerId, String addressLine1, String addressLine2, 
                                   String stateCode, String zipCode, String countryCode) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        
        customer.setAddressLine1(addressLine1);
        customer.setAddressLine2(addressLine2);
        customer.setStateCode(stateCode);
        customer.setZipCode(zipCode);
        customer.setCountryCode(countryCode);
        
        return customerRepository.save(customer);
    }
}
