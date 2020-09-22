package com.techgentsia.ecomexample.customerms.controllers;

import com.techgentsia.ecomexample.customerms.entity.Customer;
import com.techgentsia.ecomexample.customerms.exceptions.ResourceNotFoundException;
import com.techgentsia.ecomexample.customerms.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<Customer> getCurrentCustomers(@AuthenticationPrincipal User user) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(customerService.getCustomerById(UUID.fromString(user.getUsername())));
    }

    @GetMapping("/")
    public Iterable<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "id") UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
