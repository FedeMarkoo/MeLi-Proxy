package com.meli.fede.markoo.proxy.manager.service;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import com.meli.fede.markoo.proxy.manager.data.repository.MongoRepository;
import com.meli.fede.markoo.proxy.manager.generator.ObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CounterServiceTest {

    @Autowired
    @InjectMocks
    private CounterService service;

    @MockBean
    private MongoRepository repository;

    @BeforeEach
    private void setUp() {
        final List<RequestData> list = ObjectGenerator.createFullyList(RequestData.class, 20);
        list.addAll(list);
        when(this.repository.getData()).thenReturn(list);
    }

    @Test
    void getCounterByIp() {
        this.service.getCounterByIp();
    }

    @Test
    void getCounterByCombo() {
        this.service.getCounterByCombo();
    }

    @Test
    void getCounterByPath() {
        this.service.getCounterByPath();
    }

    @Test
    void getCounterByUserAgent() {
        this.service.getCounterByUserAgent();
    }
}