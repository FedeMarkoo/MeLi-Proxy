package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.service.CounterService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CounterController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {CounterController.class})
class CounterControllerTest extends BaseControllerTest {

    @MockBean
    private CounterService service;

    @Override
    protected String getBaseURI() {
        return "/counter";
    }

    @Test
    void counterIp() throws Exception {
        this.get("/ip").andExpect(status().isOk());
    }

    @Test
    void counterPath() throws Exception {
        this.get("/path").andExpect(status().isOk());
    }

    @Test
    void counterCombo() throws Exception {
        this.get("/combo").andExpect(status().isOk());
    }

    @Test
    void counterUserAgent() throws Exception {
        this.get("/userAgent").andExpect(status().isOk());
    }
}