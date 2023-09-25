package com.app.auth;

/**
 * @author Levantosina
 */
public record AuthenticationRequest (
    String username,
    String password)
{ }
