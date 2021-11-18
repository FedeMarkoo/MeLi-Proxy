package com.meli.fede.markoo.proxy.manager.service;

import com.google.common.collect.Ordering;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LogServiceTest {

    @Autowired
    @InjectMocks
    private LogService service;

    @MockBean
    private MongoRepository repository;

    @BeforeEach
    private void setUp() {
        final List<RequestData> list = ObjectGenerator.createFullyList(RequestData.class, 20);
        list.addAll(list);
        when(this.repository.getData()).thenReturn(list);
    }

    private static Comparator<Map.Entry<String, Long>> getComparator() {
        return (rd1, rd2) -> Long.compare(rd2.getValue(), rd1.getValue());
    }

    @Test
    void getIpsDenied() {
        final Map<String, Long> ipsDenied = this.service.getIpsDenied();
        assertTrue(Ordering.from(getComparator()).isOrdered(ipsDenied.entrySet()));
    }

    @Test
    void getPathsDenied() {
        final Map<String, Long> pathsDenied = this.service.getPathsDenied();
        assertTrue(Ordering.from(getComparator()).isOrdered(pathsDenied.entrySet()));
    }
}