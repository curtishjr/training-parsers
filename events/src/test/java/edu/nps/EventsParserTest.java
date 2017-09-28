package edu.nps;

import c3.ingest.test.EventAssert;
import c3.ingest.test.TestUtils;
import kronos.core.Event;
import kronos.core.EventBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class EventsParserTest {

    private static final String VIS = "U";
    private static final String DATA_TYPE = "vm-login-events";

    private static final String FEED_CONFIG_DIR = "src/main/configs";
    private static final String EVENTS_CSV = "src/test/resources/global_table_data_export.csv";

    @Test
    public void testBasicParse() throws Exception {

        List<Event> events  = newArrayList(TestUtils.parseKronosFile(FEED_CONFIG_DIR, DATA_TYPE, EVENTS_CSV));

        Event expectedEvent = EventBuilder.create("VM-LOGIN-EVENTS")
                .withId("dbaedb27")
                .withTimestamp(1501152060000L)
                .withField("//username", "GSOIS\\dminter", VIS)
                .withField("//severity//event", "Warn", VIS)
                .withField("//datetime", Instant.ofEpochMilli(1501152060000L), VIS)
                .withField("//module//event", "Agent", VIS)
                .withField("//message//event", "The pending session on machine S2017A-13 for user GSOIS\\dminter has expired", VIS)
                .withField("//virtualsystem", "DESKTOP POOL:S-2017A MACHINE:S2017A-13 ", VIS)
                .build();

        Assert.assertEquals(1821, events.size());
        EventAssert.assertEquals(expectedEvent, events.get(0));
    }
}
