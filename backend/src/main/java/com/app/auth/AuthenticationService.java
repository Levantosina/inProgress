package com.app.auth;

import com.app.customer.Customer;
import com.app.customer.CustomerDTO;
import com.app.customer.CustomerDTOMapper;
import com.app.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Levantosina
 */
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
     Authentication authentication =   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.username(),
                authenticationRequest.password()
        ));
    Customer principal=(Customer) authentication.getPrincipal();
    CustomerDTO customerDTO=customerDTOMapper.apply(principal);
    String token= jwtUtil.issueToken(customerDTO.email(),customerDTO.roles());//go to issieToken and change settings  public String issueToken(String subject, List<String> scopes){return issueToken(subject, Map.of("scopes",scopes));}
   return  new AuthenticationResponse(token,customerDTO);
    }
}
