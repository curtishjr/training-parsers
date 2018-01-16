package com.eitccorp.demo.sofwerx;

import bdp.ingest.test.EventAssert;
import bdp.ingest.test.TestUtils;
import kronos.core.Event;
import kronos.core.EventBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class EmailParserTest {

    private static final String VIS = "U";
    private static final String DATA_TYPE = "email";

    private static final String FEED_CONFIG_DIR = "src/main/configs";
    private static final String EVENTS_CSV = "src/test/resources/warren_wwd.txt";

    @Test
    public void testBasicParse() throws Exception {

        List<Event> events  = newArrayList(TestUtils.parseKronosFile(FEED_CONFIG_DIR, DATA_TYPE, EVENTS_CSV));

        Event expectedEvent = EventBuilder.create("EMAIL")
                .withId("dbaedb27")
                .withTimestamp(1501152060000L)
                .withField("//from//smtpheader", "Patricia Cromartie <animejunkiekasumi123@msn.com>", VIS)
                .withField("//datetime//smtpheader", "Sunday, December 31, 2006 10:14 PM", VIS)
                .withField("//message//smtp", "as a correction officer I believe that at times people forget how imporant \n" +
                        "one person privacey is what we may saw to some one in passing we tend to \n" +
                        "forget how fast the inmate grape vine is and what inmates can do with bits \n" +
                        "and pieces of information they here we forget we told officer john about my \n" +
                        "new car. So please stress this to officers being in a small town everybody \n" +
                        "nose everybody buisness. So where do the heppa law go infect with this one.\n" +
                        "\n" +
                        "_________________________________________________________________\n" +
                        "Experience the magic of the holidays. Talk to Santa on Messenger. \n" +
                        "http://clk.atdmt.com/MSN/go/msnnkwme0080000001msn/direct/01/?href=http://imagine-windowslive.com/minisites/santabot/default.aspx?locale=en-us", VIS)
                .build();

        Assert.assertEquals(1821, events.size());
        EventAssert.assertEquals(expectedEvent, events.get(0));
    }
}
