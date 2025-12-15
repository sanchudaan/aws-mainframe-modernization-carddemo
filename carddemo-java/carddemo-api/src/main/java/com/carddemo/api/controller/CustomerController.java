package com.carddemo.api.controller;

import com.carddemo.api.dto.CustomerDto;
import com.carddemo.core.customer.entity.Customer;
import com.carddemo.core.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Customer operations - replaces CICS transactions:
 * - CMVW (Customer View) -> GET /api/customers/{id}
 * - CMUP (Customer Update) -> PUT /api/customers/{id}
 * - CMLI (Customer List) -> GET /api/customers
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "List all customers", description = "Replaces CICS transaction CMLI")
    public ResponseEntity<Page<CustomerDto>> listCustomers(Pageable pageable) {
        Page<CustomerDto> customers = customerService.findAll(pageable).map(CustomerDto::from);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer by ID", description = "Replaces CICS transaction CMVW")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long customerId) {
        return customerService.findById(customerId)
                .map(CustomerDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new customer")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerDto.toEntity();
        Customer created = customerService.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDto.from(created));
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer", description = "Replaces CICS transaction CMUP")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerDto.toEntity();
        customer.setCustomerId(customerId);
        Customer updated = customerService.update(customer);
        return ResponseEntity.ok(CustomerDto.from(updated));
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete customer")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by name")
    public ResponseEntity<Page<CustomerDto>> searchByName(@RequestParam String name, Pageable pageable) {
        Page<CustomerDto> customers = customerService.searchByName(name, pageable).map(CustomerDto::from);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search/lastname")
    @Operation(summary = "Search customers by last name")
    public ResponseEntity<Page<CustomerDto>> searchByLastName(@RequestParam String lastName, Pageable pageable) {
        Page<CustomerDto> customers = customerService.searchByLastName(lastName, pageable).map(CustomerDto::from);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/state/{stateCode}")
    @Operation(summary = "List customers by state")
    public ResponseEntity<Page<CustomerDto>> listByStateCode(@PathVariable String stateCode, Pageable pageable) {
        Page<CustomerDto> customers = customerService.findByStateCode(stateCode, pageable).map(CustomerDto::from);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/zip/{zipCode}")
    @Operation(summary = "List customers by zip code")
    public ResponseEntity<List<CustomerDto>> listByZipCode(@PathVariable String zipCode) {
        List<CustomerDto> customers = customerService.findByZipCode(zipCode).stream()
                .map(CustomerDto::from)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/fico/min/{minScore}")
    @Operation(summary = "List customers with minimum FICO score")
    public ResponseEntity<List<CustomerDto>> listByMinFicoScore(@PathVariable Integer minScore) {
        List<CustomerDto> customers = customerService.findByMinFicoScore(minScore).stream()
                .map(CustomerDto::from)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/fico/range")
    @Operation(summary = "List customers by FICO score range")
    public ResponseEntity<List<CustomerDto>> listByFicoScoreRange(@RequestParam Integer minScore, @RequestParam Integer maxScore) {
        List<CustomerDto> customers = customerService.findByFicoScoreRange(minScore, maxScore).stream()
                .map(CustomerDto::from)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/primary-cardholders")
    @Operation(summary = "List primary card holders")
    public ResponseEntity<List<CustomerDto>> listPrimaryCardHolders() {
        List<CustomerDto> customers = customerService.findPrimaryCardHolders().stream()
                .map(CustomerDto::from)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/count/state/{stateCode}")
    @Operation(summary = "Count customers by state")
    public ResponseEntity<Long> countByStateCode(@PathVariable String stateCode) {
        return ResponseEntity.ok(customerService.countByStateCode(stateCode));
    }

    @PatchMapping("/{customerId}/fico")
    @Operation(summary = "Update customer FICO score")
    public ResponseEntity<CustomerDto> updateFicoScore(@PathVariable Long customerId, @RequestParam Integer ficoScore) {
        Customer updated = customerService.updateFicoScore(customerId, ficoScore);
        return ResponseEntity.ok(CustomerDto.from(updated));
    }

    @PatchMapping("/{customerId}/address")
    @Operation(summary = "Update customer address")
    public ResponseEntity<CustomerDto> updateAddress(
            @PathVariable Long customerId,
            @RequestParam String addressLine1,
            @RequestParam(required = false) String addressLine2,
            @RequestParam String stateCode,
            @RequestParam String zipCode,
            @RequestParam(defaultValue = "USA") String countryCode) {
        Customer updated = customerService.updateAddress(customerId, addressLine1, addressLine2, stateCode, zipCode, countryCode);
        return ResponseEntity.ok(CustomerDto.from(updated));
    }
}
