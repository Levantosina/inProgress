package com.app.customer;

/**
 * @author Levantosina
 */
public record CustomerRegistrationRequest (
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {

}
