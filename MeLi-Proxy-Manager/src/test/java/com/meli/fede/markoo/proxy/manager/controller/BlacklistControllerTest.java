package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.service.BlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BlacklistController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {BlacklistController.class})
class BlacklistControllerTest extends BaseControllerTest {

    @MockBean
    private BlacklistService service;

    @Override
    protected String getBaseURI() {
        return "/blacklist";
    }

    @Test
    void addBlackIp() throws Exception {
        this.put("/ip/localhost").andExpect(status().isCreated());
        verify(this.service, times(1)).setBlackHost(anyString());
    }

    @Test
    void removeBlackIp() throws Exception {
        this.del("/ip/localhost").andExpect(status().isNoContent());
        verify(this.service, times(1)).setWhiteHost(anyString());
    }

    @Test
    void getAllBlackIp() throws Exception {
        this.get("/ip").andExpect(status().isOk());
        verify(this.service, times(1)).getAllBlackIp();
    }

    @Test
    void isBlackIp() throws Exception {
        this.get("/ip/localhost").andExpect(status().isOk());
        verify(this.service, times(1)).isBlackHost(anyString());
    }

    @Test
    void addBlackUserAgent() throws Exception {
        this.put("/userAgent/test").andExpect(status().isCreated());
        verify(this.service, times(1)).setBlackUserAgent(anyString());
    }

    @Test
    void removeBlackUserAgent() throws Exception {
        this.del("/userAgent/test").andExpect(status().isNoContent());
        verify(this.service, times(1)).setWhiteUserAgent(anyString());
    }

    @Test
    void getAllBlackUserAgent() throws Exception {
        this.get("/userAgent").andExpect(status().isOk());
        verify(this.service, times(1)).getAllBlackUserAgent();
    }

    @Test
    void isBlackUserAgent() throws Exception {
        this.get("/userAgent/test").andExpect(status().isOk());
        verify(this.service, times(1)).isBlackUserAgent(anyString());
    }


}