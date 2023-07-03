package com.app.customer;

import com.app.exception.DuplicateResourceException;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


/**
 * @author Levantosina
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {

        underTest=new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);

        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        Customer actual=underTest.getCustomer((int)id);
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowGetCustomerReturnEmptyOptional() {
        long id=1;

        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.empty());

      assertThatThrownBy(()->underTest.getCustomer((int)id))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessageContaining(
                      "Customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {

        String email = "lev@gmail.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);

       CustomerRegistrationRequest request=new CustomerRegistrationRequest(
               "Puk",
               email,
               24);
        underTest.addCustomer(request);

        ArgumentCaptor <Customer>customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {

        String email = "lev@gmail.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                "Puk",
                email,
                24);

        assertThatThrownBy(()->underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken".formatted(email));



        verify(customerDao,never()).insertCustomer(any());




    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void updateCustomer() {
    }
}