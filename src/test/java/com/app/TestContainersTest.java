package com.app;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Levantosina
 */
@Testcontainers
public class TestContainersTest {

    @Container
    private static PostgreSQLContainer<?>postgreSQLContainer=
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("levantos-dao-unit-test")
                    .withUsername("levantos")
                    .withPassword("password");
    @Test
    void canStartPostgresDB() {

        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();

    }
}
