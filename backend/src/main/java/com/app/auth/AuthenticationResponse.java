package com.app.auth;

import com.app.customer.CustomerDTO;

/**
 * @author Levantosina
 */
public record AuthenticationResponse (
       String token,
       CustomerDTO customerDTO
) {
}
