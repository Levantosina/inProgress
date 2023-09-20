package com.app.customer;

import com.app.exception.DuplicateResourceException;
import com.app.exception.RequestValidationException;
import com.app.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Levantosina
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final  CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO>getAllCustomers(){
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(
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
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer id){
        if (!customerDao.existPersonWithId(id)) {
            throw new ResourceNotFoundException(
                    "Customer with id [%s] not found".
                            formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }
    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest){
        Customer customer=customerDao.selectCustomerById(customerId)
                .orElseThrow(
                        ()->new ResourceNotFoundException(
                                "Customer with id [%s] not found".
                                        formatted(customerId)));
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
