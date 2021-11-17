package com.meli.fede.markoo.proxy.manager.controller;

import com.meli.fede.markoo.proxy.manager.service.ProxyService;
import com.meli.fede.markoo.proxy.manager.values.AccessManagerValues;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AccessController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {AccessController.class})
class AccessControllerTest extends BaseControllerTest {

    @MockBean
    private ProxyService proxyService;

    @Override
    protected String getBaseURI() {
        return "/accessControllerValues";
    }

    @Test
    public void get() throws Exception {
        final AccessManagerValues actual = new AccessManagerValues();
        actual.setMaxRequestPerIp(1);
        actual.setMaxRequestPerPath(1);
        actual.setMaxRequestPerCombo(1);
        actual.setMaxRequestPerUserAgent(1);

        when(this.proxyService.getAccessManagerValues()).thenReturn(actual);

        final ResultActions result = this.get("");

        result.andExpect(status().isOk())
                .andExpect(jsonPath("maxRequestPerIp").exists())
                .andExpect(jsonPath("maxRequestPerPath").exists())
                .andExpect(jsonPath("maxRequestPerCombo").exists())
                .andExpect(jsonPath("maxRequestPerUserAgent").exists());
    }

    @Test
    public void post() throws Exception {
        final AccessManagerValues actual = new AccessManagerValues();
        actual.setMaxRequestPerIp(1);
        actual.setMaxRequestPerPath(1);
        actual.setMaxRequestPerCombo(1);
        actual.setMaxRequestPerUserAgent(1);

        final ResultActions result = this.post("", actual);

        result.andExpect(status().is2xxSuccessful());

        verify(this.proxyService, times(1)).setAccessManagerValues(any());
    }

}
