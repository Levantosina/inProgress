package com.app.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Lev
 * antosina
 */
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static List<Customer> customers;

    static {
        customers=new ArrayList<>();
        Customer lev=  new Customer(1,"Lev","lev@mail.com",33);
        customers.add(lev);
        Customer Kek=  new Customer(2,"Kek","Kek@mail.com",3);
        customers.add(Kek);

    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().filter(c->c.getId().equals(id)).findFirst()
                .stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream().anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream().filter(c->c.getId().equals(customerId)).findFirst()
                .stream().findFirst().ifPresent(customers::remove);

         }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream().anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        customers.add(updatedCustomer);
    }


}
