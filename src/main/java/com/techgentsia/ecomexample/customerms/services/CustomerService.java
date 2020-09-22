package com.techgentsia.ecomexample.customerms.services;

import com.techgentsia.ecomexample.customerms.entity.Customer;
import com.techgentsia.ecomexample.customerms.exceptions.ResourceNotFoundException;
import com.techgentsia.ecomexample.customerms.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.techgentsia.ecomexample.customerms.constants.MessageConstants.CUSTOMER_NOT_FOUND_ERROR;

@Service
public class CustomerService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public List<Customer> getAllCustomers() {
        List<Customer> result = new ArrayList<>();
        customerRepository.findAll().forEach(result::add);
        return result;
    }

    public Customer getCustomerById(UUID id)
            throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND_ERROR, id.toString()));
        return customer;
    }
    public Customer getCustomerByEmail(String email) {
        Customer customer = customerRepository.findOneByEmail(email);
        return customer;
    }
    public Customer createCustomer(Customer customer) {
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(UUID id, Customer customerDetails) throws ResourceNotFoundException {

        customerDetails.setId(getCustomerById(id).getId());
        final Customer updatedCustomer = customerRepository.save(customerDetails);
        return updatedCustomer;
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findOneByEmail(email);
        return new User(customer.getEmail(),customer.getPassword(), new ArrayList<>());
    }

    public UserDetails loadUserByUserId(UUID id) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(CUSTOMER_NOT_FOUND_ERROR, id.toString()));
        return new User(customer.getId().toString(),customer.getPassword(), new ArrayList<>());
    }
}
