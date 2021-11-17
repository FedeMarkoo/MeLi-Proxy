package com.meli.fede.markoo.proxy.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public abstract class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    protected ResultActions put(final String uri) throws Exception {
        final var request = MockMvcRequestBuilders.put(this.getBaseURI() + uri)
                .contentType(MediaType.APPLICATION_JSON);

        request.accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(request);
    }

    protected ResultActions del(final String uri) throws Exception {
        final var request = MockMvcRequestBuilders.delete(this.getBaseURI() + uri)
                .contentType(MediaType.APPLICATION_JSON);

        request.accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(request);
    }

    protected ResultActions post(final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.post(this.getBaseURI() + "")
                .contentType(MediaType.APPLICATION_JSON);

        if (body != null) {
            request.content(new ObjectMapper().writeValueAsString(body));
        }
        request.accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(request);
    }

    protected ResultActions get(final String uri) throws Exception {
        final var request = MockMvcRequestBuilders.get(this.getBaseURI() + uri)
                .contentType(MediaType.APPLICATION_JSON);

        request.accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(request);
    }

    protected abstract String getBaseURI();
}
