package com.app.customer;

import java.util.List;

/**
 * @author Levantosina
 */
public record CustomerDTO (
                           Long id,
                           String name,
                           String email,
                           Gender gender,

                           Integer age,
                            List<String>roles,
                            String username
                          ) {


}
