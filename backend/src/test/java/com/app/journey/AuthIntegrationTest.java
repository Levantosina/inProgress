package com.app.journey;

import com.app.auth.AuthenticationRequest;
import com.app.auth.AuthenticationResponse;
import com.app.customer.CustomerDTO;
import com.app.customer.CustomerRegistrationRequest;
import com.app.customer.Gender;
import com.app.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Levantosina
 */

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthIntegrationTest {


    @Autowired
    private WebTestClient webTestClient;//like a postman

    @Autowired
    private JWTUtil jwtUtil;

    private static final Random RANDOM = new Random();
    private static String AUTHENTICATION_PATH = "/api/v1/auth";
    private static String CUSTOMER_PATH = "/api/v1/customers";

    @Test
    void logIn() {
        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        String password = "password";

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                password,
                age,
                gender
        );
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );

        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();



        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(customerRegistrationRequest),
                        CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

       String token= result.getResponseHeaders().get(AUTHORIZATION).get(0);

       AuthenticationResponse authenticationResponse=result.getResponseBody();
       assertThat(jwtUtil.isTokeValid(
               token,
               authenticationResponse.customerDTO().username()));
    CustomerDTO customerDTO=authenticationResponse.customerDTO();

       assertThat(customerDTO.email()).isEqualTo(email);
       assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}





