package com.app;

import com.app.customer.Customer;
import com.app.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;


/**
 * @author Levantosina
 */

@SpringBootApplication

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
         return args -> {
             Customer lev=  new Customer("Lev","lev@mail.com",33);
             Customer Kek=  new Customer("Kek","Kek@mail.com",3
             );

             List<Customer>customers=List.of(lev,Kek);
             customerRepository.saveAll(customers);

         };
    }
}
