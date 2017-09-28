package com.eitccorp.training.parser;

import c3.ingest.test.EventAssert;
import c3.ingest.test.TestUtils;
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
                .build();

        Event expectedEvent2 = EventBuilder.create("ADS-B")
                .withId("dbaedb27")
                .withTimestamp(1465430397628L)
                .withField("//time//receipt", Instant.ofEpochMilli(1465430397628L), VIS)
                .withField("//adsb//icao", "ADDF5C", VIS)
                .build();

        Event expectedEvent3 = EventBuilder.create("ADS-B")
                .withId("dbaedb27")
                .withTimestamp(1465430380128L)
                .withField("//time//receipt", Instant.ofEpochMilli(1465430380128L), VIS)
                .withField("//adsb//callsign", "RXA2356", VIS)
                .withField("//adsb//icao", "7C80F8", VIS)
//                .withField("//adsb//cos", "a,b,c,d", VIS)
                .build();

        Assert.assertEquals(3, events.size());
        EventAssert.assertEquals(expectedEvent, events.get(0));
        EventAssert.assertEquals(expectedEvent2, events.get(1));
        EventAssert.assertEquals(expectedEvent3, events.get(2));
    }
}

//"AltT": 0,
//        "Bad": false,
//        "CMsgs": 2,
//        "Call": "SER621",
//        "CallSus": false,
//        "Cou": "Canada",
//        "EngMount": 0,
//        "EngType": 0,
//        "FSeen": "/Date(1465430397644)/",
//        "FlightsCount": 0,
//        "HasPic": false,
//        "HasSig": false,
//        "Icao": "C1E4DB",
//        "Id": 12707035,
//        "Interested": false,
//        "Mil": false,
//        "Rcvr": 1,
//        "SpdTyp": 0,
//        "Species": 0,
//        "Sqk": "",
//        "TSecs": 3,
//        "TT": "a",
//        "Tisb": false,
//        "TrkH": false,
//        "Trt": 1,
//        "VsiT": 0,
//        "WTC": 0