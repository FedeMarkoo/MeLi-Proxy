package com.meli.fede.markoo.proxy.api.controller;

import com.meli.fede.markoo.proxy.api.service.AccessService;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ProxyController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {ProxyController.class})
class ProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccessService accessService;


    @Test
    void deniedRequest() throws Exception {
        when(this.accessService.validateAccess(anyString(), anyString(), anyString())).thenReturn(false);
        this.get("/fakePath").andExpect(status().isTooManyRequests());
    }

    @Test
    void okRequest() throws Exception {
        when(this.accessService.validateAccess(anyString(), anyString(), anyString())).thenReturn(true);
        this.get("/fakePath").andExpect(status().isOk());
    }

    protected ResultActions get(final String uri) throws Exception {
        final var request = MockMvcRequestBuilders.get(uri)
                .header("User-Agent", "test")
                .header("Host", "localhost")
                .contentType(MediaType.APPLICATION_JSON);

        request.accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(request);
    }
}