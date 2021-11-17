package com.meli.fede.markoo.proxy.api.data.repository;

import com.meli.fede.markoo.proxy.api.data.model.RequestData;
import com.meli.fede.markoo.proxy.api.values.AccessManagerValues;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MongoRepository {

    private final MongoTemplate mongo;

    public void incCounter(final RequestData data, final boolean denied) {
        final Query query = Query.query(Criteria.byExample(Example.of(data)));

        final Update update = new Update();
        update.inc("requestedCant");

        if (denied) {
            update.inc("deniedCant");
        }

        final FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true);

        this.mongo.findAndModify(query, update, findAndModifyOptions, RequestData.class);
    }

    public AccessManagerValues getMaxCantRequest() {
        final List<AccessManagerValues> all = this.mongo.findAll(AccessManagerValues.class);
        if (!all.isEmpty()) {
            return all.get(0);
        }
        return null;
    }
}
