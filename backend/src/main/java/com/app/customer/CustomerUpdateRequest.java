package com.app.customer;

/**
 * @author Levantosina
 */
public record CustomerUpdateRequest (
    String name,
    String email,
    Integer age
){

}
