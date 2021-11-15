package com.meli.fede.markoo.proxy.manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainControllerApplicationTests {
    /**
     * La carga media del proxy (como solución) debe poder superar los 50000
     * request/segundo. Por lo cual como escala la solución es muy importante
     */
    @Timeout(1)
    @Test
    public void tooManyRequest() {

    }
}
