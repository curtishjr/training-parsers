package com.eitccorp.training.kronos;

import kronos.core.Event;
import kronos.core.config.IngestConfig;
import kronos.core.config.QueryConfig;
import kronos.ingest.EventWriter;
import kronos.ingest.accumulo.AccumuloEventWriter;
import kronos.query.QueryService;
import kronos.query.accumulo.AccumuloQueryService;
import kronos.test.util.TestData;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.calrissian.mango.collect.FluentCloseableIterable.from;


public class KronosQueryTest {
    static Connector connector;

    /*
    This method generates a set of 10 SILK events in Kronos with the following attribute values.
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | IP_SRC   | IP_DST    | PORT_SRC | PORT_DST | s1   | s2    | timestamp                    |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.0 | 10.1.1.1  | 80       | 81       | val0 | val1  | Thu Oct 01 20:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.1 | 10.1.1.2  | 81       | 82       | val1 | val2  | Fri Oct 02 21:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.2 | 10.1.1.3  | 82       | 83       | val2 | val3  | Sat Oct 03 22:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.3 | 10.1.1.4  | 83       | 84       | val3 | val4  | Sun Oct 04 23:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.4 | 10.1.1.5  | 84       | 85       | val4 | val5  | Tue Oct 06 00:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.5 | 10.1.1.6  | 85       | 86       | val5 | val6  | Wed Oct 07 01:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.6 | 10.1.1.7  | 86       | 87       | val6 | val7  | Thu Oct 08 02:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.7 | 10.1.1.8  | 87       | 88       | val7 | val8  | Fri Oct 09 03:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.8 | 10.1.1.9  | 88       | 89       | val8 | val9  | Sat Oct 10 04:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    | 10.1.1.9 | 10.1.1.10 | 89       | 90       | val9 | val10 | Sun Oct 11 05:00:00 EDT 2015 |
    +----------+-----------+----------+----------+------+-------+------------------------------+
    */
    @BeforeClass
    public static void writeTestDataIntoKronosAccumulo() throws Exception{
        List<Event> testEvents = TestData.targetEvents(10, "SILK", "U");

        connector = mockConnector();
        getEventWriter(connector).write(testEvents);
    }

    @Test
    public void testQueryByDateRange() throws Exception{
        QueryService queryService = getQueryService(connector);

        KronosQuery query = new KronosQuery();

        List<Event> events = from(queryService.query(query.queryByDateRange(), query.getAuths()))
                .autoClose()
                .toList();

        events.get(0).getFields("IP_SRC").forEach((field) -> Assert.assertEquals("10.1.1.4", field.getValue()));
        events.get(1).getFields("IP_SRC").forEach((field) -> Assert.assertEquals("10.1.1.5", field.getValue()));
        Assert.assertEquals(2, events.size());
    }

    @Test
    public void testQueryByAttributeValue() throws Exception{
        QueryService queryService = getQueryService(connector);

        KronosQuery query = new KronosQuery();

        List<Event> events = from(queryService.query(query.queryByAttributeValue(), query.getAuths()))
                .autoClose()
                .toList();

        events.get(0).getFields("s1").forEach((field) -> Assert.assertEquals("val6", field.getValue()));
        events.get(1).getFields("s1").forEach((field) -> Assert.assertEquals("val8", field.getValue()));
        Assert.assertEquals(2, events.size());
    }

    @Test
    public void testQueryWithLimitedFieldReturn() throws Exception{
        QueryService queryService = getQueryService(connector);

        KronosQuery query = new KronosQuery();

        List<Event> events = from(queryService.query(query.queryWithLimitedReturnFields(), query.getAuths()))
                .autoClose()
                .toList();

        Assert.assertEquals(10, events.size());
        events.get(0).getFields("IP_SRC").forEach((field) -> Assert.assertEquals("10.1.1.0", field.getValue()));
        Assert.assertEquals(0, events.get(0).getFields("IP_DST").size());
    }

    @Test
    public void testQueryWithLimitedTypes() throws Exception{
        QueryService queryService = getQueryService(connector);

        KronosQuery queryClient = new KronosQuery();

        List<Event> events = from(queryService.query(queryClient.queryWithLimitedTypes(), queryClient.getAuths()))
                .autoClose()
                .toList();

        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testQueryByID() throws Exception{
        QueryService queryService = getQueryService(connector);

        KronosQuery query = new KronosQuery();

        List<Event> events = from(queryService.query(query.queryByID(), query.getAuths()))
                .autoClose()
                .toList();

        Assert.assertEquals(1, events.size());
        events.get(0).getFields("IP_SRC").forEach((field) -> Assert.assertEquals("10.1.1.2", field.getValue()));
    }



    private static Connector mockConnector() throws Exception{
        return new MockInstance().getConnector("root", new PasswordToken(""));
    }

    private static QueryService getQueryService(Connector connector) throws Exception {
        return new AccumuloQueryService(
                connector,
                new QueryConfig());
    }

    private static EventWriter getEventWriter(Connector connector) throws Exception {
        return new AccumuloEventWriter(
                connector,
                new IngestConfig()
        );
    }


}
