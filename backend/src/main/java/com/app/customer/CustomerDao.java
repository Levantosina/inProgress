package com.app.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Levantosina
 */

public interface  CustomerDao {
    List<Customer>selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);

    void deleteCustomerById(Integer customerId);
    boolean existPersonWithId(Integer id);

    void updateCustomer(Customer updatedCustomer);

}
