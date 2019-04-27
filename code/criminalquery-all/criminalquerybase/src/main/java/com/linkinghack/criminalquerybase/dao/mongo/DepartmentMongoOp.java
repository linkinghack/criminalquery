package com.linkinghack.criminalquerybase.dao.mongo;

import org.bson.BSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentMongoOp {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DepartmentMongoOp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String aggregateDistricts() {
        GraphLookupOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("districts")
                .startWith("id")
                .connectFrom("id")
                .connectTo("supervisorid")
                .as("children");
        Aggregation agg = Aggregation.newAggregation(graphLookupOperation);
        AggregationResults results = mongoTemplate.aggregate(agg, "districts", BSON.class);
        return results.getRawResults().toString();
    }
}
