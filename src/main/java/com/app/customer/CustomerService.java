package com.app.customer;

import com.app.exception.DuplicateResourceException;
import com.app.exception.RequestValidationException;
import com.app.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Levantosina
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer>getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id).
                orElseThrow(
                        ()->new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exist
       String email= customerRegistrationRequest.email();
        if(customerDao.existPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }

        Customer customer=new Customer(
        customerRegistrationRequest.name(),
        customerRegistrationRequest.email(),
        customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer id){
        if (!customerDao.existPersonWithId(id)) {
            throw new ResourceNotFoundException(
                    "Customer with id [%s] not found".
                            formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }
    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){
        Customer customer=getCustomer(customerId);
        boolean changes=false;

        if(customerUpdateRequest.name()!=null && !customerUpdateRequest.name().equals(customer.getName()))
        {
            customer.setName(customerUpdateRequest.name());
            changes=true;
        }
        if(customerUpdateRequest.age()!=null && !customerUpdateRequest.age().equals(customer.getAge()))
        {
            customer.setAge(customerUpdateRequest.age());
            changes=true;
        }
        if(customerUpdateRequest.email()!=null && !customerUpdateRequest.email().equals(customer.getEmail()))
        {
            if(customerDao.existPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes=true;
        }
        if(!changes){
            throw new RequestValidationException("No changes detected");
        }
        customerDao.updateCustomer(customer);
    }
}
