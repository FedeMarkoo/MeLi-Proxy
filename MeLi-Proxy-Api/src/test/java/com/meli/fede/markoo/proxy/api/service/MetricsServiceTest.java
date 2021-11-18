package com.meli.fede.markoo.proxy.api.service;

import com.meli.fede.markoo.proxy.api.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@EnableAsync
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MetricsServiceTest {

    @Autowired
    @InjectMocks
    private MetricsService service;

    @MockBean
    private MongoRepository mongoRepository;

    @BeforeAll
    private void setUp() {
        final AccessManagerValues values = new AccessManagerValues();
        values.setMaxRequestPerIp(1);
        values.setMaxRequestPerPath(1);
        values.setMaxRequestPerCombo(1);
        values.setMaxRequestPerUserAgent(1);

        when(this.mongoRepository.getMaxCantRequest()).thenReturn(values);
    }

    @Test
    void incrementMetricsDenied() throws InterruptedException {
        this.service.incrementMetrics("localhost", "/fakepath", "chrome", true);
        Thread.sleep(1000);
        verify(this.mongoRepository, times(1)).incCounter(any(), anyBoolean());
    }
}