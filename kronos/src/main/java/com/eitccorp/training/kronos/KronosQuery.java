package com.eitccorp.training.kronos;

import kronos.core.EventIdentifier;
import kronos.core.query.IdentifierQuery;
import kronos.core.query.Query;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.collect.Range.open;
import static java.time.Instant.ofEpochMilli;

public class KronosQuery {

    public Query queryByDateRange(){
        return new Query().withDateRange(open(ofEpochMilli(1444103000000L), ofEpochMilli(1444276800000L)));
    }

    public Query queryByAttributeValue(){
        return new Query().withExpression("s1=='val6' || s1=='val8'");
    }

    public Query queryWithLimitedReturnFields(){
        return new Query().withFields("IP_SRC");
    }

    public Query queryWithLimitedTypes() {
        return new Query().withTypes("FAKE_TYPE");
    }

    public IdentifierQuery queryByID() {
        return new IdentifierQuery().withIdentifiers(new EventIdentifier("SILK", "SILK2", 1443924000000L ));
    }

    public Collection getAuths(){
        return Collections.singletonList("U");
    }
}
