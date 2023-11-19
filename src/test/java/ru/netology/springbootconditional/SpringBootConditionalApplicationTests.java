package ru.netology.springbootconditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootConditionalApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    private static final GenericContainer<?> devapp = new GenericContainer<>("devapp")
            .withExposedPorts(8080);
    private static final GenericContainer<?> prodapp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoadsDev() {
        String dev = "Current profile is dev";
        ResponseEntity<String> forDev = restTemplate.getForEntity("http://localhost:" +
                devapp.getMappedPort(8080) + "/profile", String.class);
        Assertions.assertEquals(dev, forDev.getBody());
    }

    @Test
    void contextLoadsProd() {
        String prod = "Current profile is production";
        ResponseEntity<String> forProd = restTemplate.getForEntity("http://localhost:" +
                prodapp.getMappedPort(8081) + "/profile", String.class);
        Assertions.assertEquals(prod, forProd.getBody());
    }
}
