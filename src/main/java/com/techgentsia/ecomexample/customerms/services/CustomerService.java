package com.techgentsia.ecomexample.customerms.services;

import com.techgentsia.ecomexample.customerms.entity.Customer;
import com.techgentsia.ecomexample.customerms.exceptions.CustomerNotFoundException;
import com.techgentsia.ecomexample.customerms.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;


    public List<Customer> getAllCustomers() {
        List<Customer> result = new ArrayList<>();
        customerRepository.findAll().forEach(result::add);
        return result;
    }

    public Customer getCustomerById(UUID id)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return customer;
    }
    public Customer getCustomerByEmail(String email) {
        Customer customer = customerRepository.findOneByEmail(email);
        return customer;
    }
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, Customer customerDetails) throws CustomerNotFoundException {

        customerDetails.setId(getCustomerById(id).getId());
        final Customer updatedCustomer = customerRepository.save(customerDetails);
        return updatedCustomer;
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return new User("foo","foo", new ArrayList<>());
    }
}