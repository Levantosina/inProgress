package com.app.customer;

/**
 * @author Levantosina
 */
public record CustomerRegistrationRequest (
    String name,
    String email,
    Integer age,
    Gender gender
) { }
