package com.meli.fede.markoo.proxy.manager.data.repository;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import com.meli.fede.markoo.proxy.manager.values.AccessManagerValues;
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

    public AccessManagerValues getMaxCantRequest() {
        final List<AccessManagerValues> all = this.mongo.findAll(AccessManagerValues.class);
        if (!all.isEmpty()) {
            return all.get(0);
        }
        return null;
    }

    public void saveMaxCantRequest(final AccessManagerValues request) {
        this.mongo.remove(AccessManagerValues.class).all();
        this.mongo.save(request);
    }
}
