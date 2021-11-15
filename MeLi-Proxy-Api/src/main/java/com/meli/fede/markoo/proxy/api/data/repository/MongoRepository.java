package com.meli.fede.markoo.proxy.api.data.repository;

import com.meli.fede.markoo.proxy.api.data.model.RequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoRepository {

    private final MongoTemplate mongo;

    public void incCounter(final RequestData data, final boolean denieded) {
        final Query query = Query.query(Criteria.byExample(Example.of(data)));

        final Update update = new Update();
        update.inc("requestedCant");

        if (denieded) {
            update.inc("deniedCant");
        }

        final FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(true);

        this.mongo.findAndModify(query, update, findAndModifyOptions, RequestData.class);
    }
}
