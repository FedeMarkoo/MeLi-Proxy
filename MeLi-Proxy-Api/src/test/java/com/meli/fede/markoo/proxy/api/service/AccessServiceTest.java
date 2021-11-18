package com.meli.fede.markoo.proxy.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.meli.fede.markoo.proxy.api.data.repository.RedisRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccessServiceTest {

    @Autowired
    @InjectMocks
    private AccessService service;

    @MockBean
    private RedisRepository redisRepository;
    @Autowired
    @MockBean
    private AccessManagerValues values;

    @BeforeEach
    private void setUp() {
        when(this.values.getMaxRequestPerIp()).thenReturn(10);
        when(this.values.getMaxRequestPerPath()).thenReturn(10);
        when(this.values.getMaxRequestPerCombo()).thenReturn(10);
        when(this.values.getMaxRequestPerUserAgent()).thenReturn(10);
    }

    @Test
    void validAccess() {
        when(this.redisRepository.getAndIncrement(anyString())).thenReturn(10L);
        when(this.redisRepository.isBlackIp(any())).thenReturn(false);
        when(this.redisRepository.isBlackUserAgent(any())).thenReturn(false);
        assertTrue(this.service.validateAccess("/path", "localhost", "chrome"));
    }

    @Test
    void invalidAccess() {
        when(this.redisRepository.getAndIncrement(anyString())).thenReturn(11L);
        when(this.redisRepository.isBlackIp(any())).thenReturn(false);
        when(this.redisRepository.isBlackUserAgent(any())).thenReturn(false);
        assertFalse(this.service.validateAccess("/path", "localhost", "chrome"));
    }

    @Test
    void autoBlacklistIP() {
        when(this.redisRepository.getAndIncrement(anyString())).thenReturn(21L);
        when(this.redisRepository.isBlackIp(any())).thenReturn(false);
        when(this.redisRepository.isBlackUserAgent(any())).thenReturn(false);

        assertFalse(this.service.validateAccess("/path", "localhost", "chrome"));

        verify(this.redisRepository, times(1)).blackIp(anyString());
    }

    @Test
    @Disabled
    void processProxy() throws Exception {
        final Object actual = this.service.processProxy(
                new Response()
                , HttpMethod.GET
                , Optional.empty()
                , "/categories/MLA1088");


        final String actualString = new ObjectMapper().writeValueAsString(actual);
        JSONAssert.assertEquals(this.loadTestJson("../../../../../../../expected.json"), actualString, true);
    }

    @Test
    @Disabled
    void processProxyFail() {
        final HttpServletResponse response = new MockHttpServletResponse();
        this.service.processProxy(
                response
                , HttpMethod.GET
                , Optional.empty()
                , "/fakePath");

        assertTrue(Objects.requireNonNull(HttpStatus.resolve(response.getStatus())).isError());
    }

    public String loadTestJson(final String fileName) {
        final URL url = Resources.getResource(this.getClass(), fileName);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException("Couldn't load a JSON file.", e);
        }
    }
}