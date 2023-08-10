package com.app.customer;


import org.junit.jupiter.api.Test;


import java.sql.ResultSet;
import java.sql.SQLException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Levantosina
 */

class CustomerRowMapperTest {

      @Test
    void mapRow() throws SQLException {
          CustomerRowMapper customerRowMapper=new CustomerRowMapper();

          ResultSet resultSet=mock(ResultSet.class);
            when(resultSet.getLong("id")).thenReturn(2L);
          when(resultSet.getInt("age")).thenReturn(19);
          when(resultSet.getString("email")).thenReturn("pepea@gmail.com");
          when(resultSet.getString("name")).thenReturn("Pepe");
          when(resultSet.getString("gender")).thenReturn("FEMALE");

         Customer actual=  customerRowMapper.mapRow(resultSet,1);

         Customer expectedCustomer=new Customer(
                 2L,"Pepe","pepea@gmail.com",19,
                 Gender.FEMALE);

         assertThat(actual).isEqualTo(expectedCustomer);
      }
}