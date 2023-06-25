package com.app.customer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Levantosina
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer>getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id).
                orElseThrow(
                        ()->new IllegalStateException(
                                "Customer with id [%s]not found".
                                        formatted(id)));
    }
}
