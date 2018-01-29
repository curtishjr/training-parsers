package com.eitccorp.training.parser.http;

import bdp.ingest.test.EventAssert;
import bdp.ingest.test.TestUtils;
import kronos.core.Event;
import kronos.core.EventBuilder;
import org.calrissian.mango.net.IPv4;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class DevforceHttpAccessTest {

    private static final String VIS = "U";
    private static final String DATA_TYPE = "devforce-http";

    private static final String FEED_CONFIG_DIR = "src/main/configs";
    private static final String HTTP_LOG = "src/test/resources/access.log";

    @Test
    public void testBasicParse() throws Exception {

        List<Event> events  = newArrayList(TestUtils.parseKronosFile(FEED_CONFIG_DIR, DATA_TYPE, HTTP_LOG));

        Event expectedEvent = EventBuilder.create("DEVFORCE-HTTP")
                .withTimestamp(1505622705000L)
                .withField("//time//receipt", Instant.ofEpochMilli(1505622705000L), VIS)
                .withField("//ip//src", IPv4.fromString("63.143.42.248"), VIS)
                .withField("//url//request", "HEAD / HTTP/1.1", VIS)
                .withField("//httpmethodname", "HEAD", VIS)
                .withField("//url//resource", "/", VIS)
                .withField("//protocol//transport", "HTTP/1.1", VIS)
                .withField("//httpresponsestatuscode", "302", VIS)
                .withField("//bytes//output", "-", VIS)
                .withField("//url//referrer", "http://devforce.io", VIS)
                .withField("//httpuseragent", "Mozilla/5.0+(compatible; UptimeRobot/2.0; http://www.uptimerobot.com/)", VIS)
                .build();

        Event expectedEvent2 = EventBuilder.create("DEVFORCE-HTTP")
                .withTimestamp(1505627670000L)
                .withField("//time//receipt", Instant.ofEpochMilli(1505627670000L), VIS)
                .withField("//ip//src", IPv4.fromString("46.118.154.197"), VIS)
                .withField("//url//request", "GET / HTTP/1.1", VIS)
                .withField("//httpmethodname", "GET", VIS)
                .withField("//url//resource", "/", VIS)
                .withField("//protocol//transport", "HTTP/1.1", VIS)
                .withField("//httpresponsestatuscode", "302", VIS)
                .withField("//bytes//output", "289", VIS)
                .withField("//url//referrer", "https://pl.id-forex.com/", VIS)
                .withField("//httpuseragent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; MyIE2; Deepnet Explorer)", VIS)
                .build();

  Event expectedEvent5 = EventBuilder.create("DEVFORCE-HTTP")
                .withTimestamp(1503421121000L)
                .withField("//time//receipt", Instant.ofEpochMilli(1503421121000L), VIS)
                .withField("//ip//src", IPv4.fromString("164.52.7.132"), VIS)
                .withField("//url//request", "\\x16\\x03\\x01\\x01\"\\x01", VIS)
                .withField("//httpresponsestatuscode", "302", VIS)
                .withField("//bytes//output", "274", VIS)
                .withField("//url//referrer", "-", VIS)
                .withField("//httpuseragent", "-", VIS)
                .build();

        assertEquals(5, events.size());
        EventAssert.assertEquals(expectedEvent, events.get(0));
        EventAssert.assertEquals(expectedEvent2, events.get(1));
        EventAssert.assertEquals(expectedEvent5, events.get(4));
    }
}
