package com.app.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Lev
 * antosina
 */
@Repository
public class CustomerDataAccessService implements CustomerDao{

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
}
