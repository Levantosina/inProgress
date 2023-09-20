package com.app.customer;


import com.app.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;




/**
 * @author Levantosina
 */

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper=new CustomerRowMapper();
    @BeforeEach
    void setUp() {
        underTest=new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }


    @Test
    void selectAllCustomers() {

        Customer customer=new Customer(FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

        List<Customer> actual=underTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
       String email= FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer=new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

       long id= underTest.selectAllCustomers()
                        .stream()
                                .filter(c->c.getEmail().equals(email))
                                        .map(Customer::getId)
                                                .findFirst()
                                                        .orElseThrow();



       Optional<Customer> actual= underTest.selectCustomerById((int) id);

       assertThat(actual).isPresent().hasValueSatisfying(c->{
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());

       });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){

        int id=-1;
        var actual=underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }




    @Test
    void existPersonWithEmail() {
        String email= FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name=FAKER.name().fullName();
        Customer customer=new Customer(name,
                email,
                "password", 20, Gender.MALE);
        underTest.insertCustomer(customer);
        boolean actual=underTest.existPersonWithEmail(email);
        assertThat(actual).isTrue();

    }
    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExists(){
        String email= FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        boolean actual=underTest.existPersonWithEmail(email);

        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        underTest.deleteCustomerById(Math.toIntExact(id));

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));

        assertThat(actual).isNotPresent();
    }

    @Test
    void existPersonWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        var actual= underTest.existPersonWithId(Math.toIntExact(id));

        assertThat(actual).isTrue();

    }

    @Test
     void existPersonWithIdReturnsFalseWhenDoesNotExists(){
        int id=-1;
        var actual=underTest.existPersonWithId(id);
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().
                orElseThrow();


        var newName="foo";

        Customer update=new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
    @Test
    void updateCustomerEmail(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().
                orElseThrow();

        var newEmail=FAKER.internet().safeEmailAddress()+"-"+UUID.randomUUID();

        Customer update=new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }
    @Test
    void updateCustomerAge(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        int newAge=100;

        Customer update=new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());

        });


    }
    @Test
    void willUpdateAllPropertiesCustomer(){

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        Customer update=new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail=UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(100);

        underTest.updateCustomer(update);

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));

        assertThat(actual).isPresent().hasValueSatisfying(updated->{
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getGender()).isEqualTo(Gender.MALE );
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(100);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(FAKER.name().fullName(),
                email,
                "password", 20, Gender.MALE);

        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();


        Customer update=new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        Optional<Customer>actual=underTest.selectCustomerById(Math.toIntExact(id));

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}