package com.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Levantosina
 */


public class TestContainersTest extends AbstractTestContainers {

    @Test
    void canStartPostgresDB() {

        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();

    }


}
