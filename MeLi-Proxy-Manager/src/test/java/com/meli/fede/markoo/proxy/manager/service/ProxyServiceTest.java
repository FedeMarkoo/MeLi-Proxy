package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.manager.generator.ObjectGenerator;
import com.meli.fede.markoo.proxy.manager.values.AccessManagerValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProxyServiceTest {

    @Autowired
    @InjectMocks
    private ProxyService service;

    @MockBean
    private MongoRepository repository;

    @BeforeEach
    private void setUp() {
        final AccessManagerValues values = new AccessManagerValues();
        values.setMaxRequestPerIp(10);
        values.setMaxRequestPerPath(10);
        values.setMaxRequestPerCombo(10);
        values.setMaxRequestPerUserAgent(10);
        when(this.repository.getMaxCantRequest()).thenReturn(values);
    }

    @Test
    void getAccessManagerValues() {
        final AccessManagerValues expected = new AccessManagerValues();
        expected.setMaxRequestPerIp(10);
        expected.setMaxRequestPerPath(10);
        expected.setMaxRequestPerCombo(10);
        expected.setMaxRequestPerUserAgent(10);

        final AccessManagerValues accessManagerValues = this.service.getAccessManagerValues();

        Assertions.assertEquals(expected, accessManagerValues);
    }

    @Test
    void setAccessManagerValues() {
        this.service.setAccessManagerValues(ObjectGenerator.createInstanceFully(AccessManagerValues.class));
        verify(this.repository, times(1)).saveMaxCantRequest(any());
    }
}