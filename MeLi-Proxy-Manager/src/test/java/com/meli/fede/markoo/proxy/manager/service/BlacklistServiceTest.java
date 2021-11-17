package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.repository.RedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BlacklistServiceTest {

    @Autowired
    @InjectMocks
    private BlacklistService service;

    @MockBean
    private RedisRepository redis;

    @Test
    void isBlackHost() {
        when(this.redis.isBlackIp(anyString())).thenReturn(true);
        assertTrue(this.service.isBlackHost(anyString()));
    }

    @Test
    void setBlackHost() {
        this.service.setBlackHost("localhost");
        verify(this.redis, times(1)).blackIp(anyString(), anyBoolean());
    }

    @Test
    void setWhiteHost() {
        this.service.setWhiteHost("localhost");
        verify(this.redis, times(1)).blackIp(anyString(), anyBoolean());
    }

    @Test
    void getAllBlackUserAgent() {
        this.service.getAllBlackUserAgent();
        verify(this.redis, times(1)).getAllBlackUserAgent();
    }

    @Test
    void isBlackUserAgent() {
        when(this.redis.isBlackUserAgent(anyString())).thenReturn(true);
        assertTrue(this.service.isBlackUserAgent(anyString()));
    }

    @Test
    void setBlackUserAgent() {
        this.service.setBlackUserAgent("test");
        verify(this.redis, times(1)).blackUserAgent(anyString(), anyBoolean());
    }

    @Test
    void setWhiteUserAgent() {
        this.service.setWhiteUserAgent("test");
        verify(this.redis, times(1)).blackUserAgent(anyString(), anyBoolean());
    }

    @Test
    void getAllBlackIp() {
        this.service.getAllBlackIp();
        verify(this.redis, times(1)).getAllBlackIp();
    }
}