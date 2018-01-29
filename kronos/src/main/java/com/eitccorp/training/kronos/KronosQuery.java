package com.eitccorp.training.kronos;

import kronos.core.query.Query;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.collect.Range.open;
import static java.time.Instant.ofEpochMilli;

public class KronosQuery {

    public Query dateRangeQuery(){
        return new Query().withDateRange(open(ofEpochMilli(1444103000000L), ofEpochMilli(1444276800000L)));
    }

    public Collection getAuths(){
        return Collections.singletonList("U");
    }
}
