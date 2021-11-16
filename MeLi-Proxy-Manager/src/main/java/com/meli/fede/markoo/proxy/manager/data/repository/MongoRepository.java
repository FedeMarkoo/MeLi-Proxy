package com.meli.fede.markoo.proxy.manager.data.repository;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoRepository {

    private final MongoTemplate mongo;

    public List<RequestData> getData() {
        return this.mongo.findAll(RequestData.class);
    }
}
