package com.eitccorp.training.parser;

import bdp.ingest.test.EventAssert;
import bdp.ingest.test.TestUtils;
import kronos.core.Event;
import kronos.core.EventBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ADSBParserTest {

    private static final String VIS = "U";
    private static final String DATA_TYPE = "ads-b";

    private static final String FEED_CONFIG_DIR = "src/main/configs";
    private static final String ADSB_JSON = "src/test/resources/ads-b.json";

    @Test
    public void testBasicParse() throws Exception {

        List<Event> events  = newArrayList(TestUtils.parseKronosFile(FEED_CONFIG_DIR, DATA_TYPE, ADSB_JSON));

        Event expectedEvent = EventBuilder.create("ADS-B")
                .withId("dbaedb27")
                .withTimestamp(1465430397644L)
                .withField("//time//receipt", Instant.ofEpochMilli(1465430397644L), VIS)
                .withField("//adsb//callsign", "SER621", VIS)
                .withField("//adsb//icao", "C1E4DB", VIS)
                .withField("//country", "Canada", VIS)
                .build();

        Event expectedEvent2 = EventBuilder.create("ADS-B")
                .withId("dbaedb27")
                .withTimestamp(1465430380128L)
                .withField("//time//receipt", Instant.ofEpochMilli(1465430380128L), VIS)
                .withField("//adsb//callsign", "RXA2356", VIS)
                .withField("//adsb//icao", "7C80F8", VIS)
                .withField("//country", "Australia", VIS)
                .withField("//latitude", -32.272475999999997, VIS)
                .withField("//longitude", 116.570874, VIS)
                .withField("//adsb//alt", 12000, VIS)
                .withField("//adsb//speed", 256.19999999999999, VIS)
                .build();

        Assert.assertEquals(3, events.size());
        EventAssert.assertEquals(expectedEvent, events.get(0));
        EventAssert.assertEquals(expectedEvent2, events.get(2));
    }
}