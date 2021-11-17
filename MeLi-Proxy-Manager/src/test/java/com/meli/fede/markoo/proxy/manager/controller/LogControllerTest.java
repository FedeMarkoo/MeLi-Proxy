package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(LogController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {LogController.class})
class LogControllerTest extends BaseControllerTest {

    @MockBean
    private LogService logService;

    @Override
    protected String getBaseURI() {
        return "/log";
    }

    @Test
    void getIpsDenied() throws Exception {
        this.get("/ip/denied").andExpect(status().isOk());
    }

    @Test
    void getPathsDenied() throws Exception {
        this.get("/path/denied").andExpect(status().isOk());
    }
}