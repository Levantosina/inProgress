package com.app.customer;

import com.app.exception.DuplicateResourceException;
import com.app.exception.RequestValidationException;
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
    void deleteCustomerById() {
        int id=1;


        when(customerDao.existPersonWithId((int)id)).thenReturn(true);

        underTest.deleteCustomerById((int)id);
        verify(customerDao).deleteCustomerById((int)id);

    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {

        long id=1;

        when(customerDao.existPersonWithId((int)id)).thenReturn(false);


        assertThatThrownBy(()->underTest.deleteCustomerById((int)id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Customer with id [%s] not found".formatted(id));
        verify(customerDao, never()).deleteCustomerById((int)id);

    }

    @Test
    void canUpdateAllCustomersProperties() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

       String newEmail ="puk@gmail.com";
        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest("Puk",newEmail,25);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer((int)id,customerUpdateRequest);

        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

       Customer capturedCustomer= customerArgumentCaptor.getValue();

       assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());

    }
    @Test
    void canUpdateOnlyCustomerName() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest("Puk",null,null);


        underTest.updateCustomer((int)id,customerUpdateRequest);

        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer= customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomerEmail() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        String newEmail ="puk@gmail.com";
        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest(null,newEmail,null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer((int)id,customerUpdateRequest);

        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer= customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateOnlyCustomerAge() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        String newEmail ="puk@gmail.com";
        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest(null,null,44);



        underTest.updateCustomer((int)id,customerUpdateRequest);

        ArgumentCaptor<Customer>customerArgumentCaptor=
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer= customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());

    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        String newEmail ="puk@gmail.com";
        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest(null,newEmail,null);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);


        assertThatThrownBy(()->underTest.updateCustomer((int)id,customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken".formatted(newEmail));


        verify(customerDao,never()).updateCustomer(any());


    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {

        long id=1;
        Customer customer=new Customer(id,"Lev","lev@gmail.com",24);


        when(customerDao.selectCustomerById((int)id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest=new CustomerUpdateRequest(customer.getName(),customer.getEmail(),customer.getAge());

       assertThatThrownBy(()->underTest.updateCustomer((int)id,customerUpdateRequest))
               .isInstanceOf(RequestValidationException.class)
               .hasMessageContaining("No changes detected");

       verify(customerDao,never()).updateCustomer(any());

    }
}