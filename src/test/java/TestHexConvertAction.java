import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.rocana.configuration.ConfigurationParser;
import com.rocana.event.Event;
import com.rocana.transform.ActionEngine;
import kafka.utils.VerifiableProperties;
import org.apache.commons.compress.utils.Charsets;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by dave on 7/12/16.
 */
public class TestHexConvertAction {
    private static final Logger logger = LoggerFactory.getLogger(TestHexConvertAction.class);

    /*
     * This test mimics how the transformation engine is invoked within Rocana.
     */
    @Test
    public void testKeyValueParseAction() throws IOException {
        ConfigurationParser parser = new ConfigurationParser();
        Date startTime = new Date();

        ActionEngine<Event> engine = null;

        try (InputStream inputStream = Resources.getResource("test-hexString-action.conf").openStream()) {
            engine = parser.parse(new InputStreamReader(inputStream), ActionEngine.class);
        }
        Map<String, String> attrs = new HashMap<String, String>();

        StringReader reader = new StringReader(Resources.toString(Resources.getResource("hex.log"), Charsets.UTF_8));
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<Event> allEvents = new ArrayList<Event>();

        StringEventDecoder decoder = new StringEventDecoder(new VerifiableProperties());

        while ((line = br.readLine()) != null) {
            Event event = decoder.fromBytes(line.getBytes());
            Iterable events = engine.transform(event);
            allEvents.addAll(Lists.newArrayList(events));
            //logger.info("Output event:{}", Joiner.on(",").join(events));
        }
        System.out.println("Num Events: " + allEvents.size());
        for(Iterator<Event> i = allEvents.iterator(); i.hasNext();){
            Event event;
            event = i.next();
            String decodedString = (String) event.getAttributes().get("decodedString");
            System.out.println(event.toString());
            System.out.println("Decoded String: " + decodedString);
        }

        Date endTime = new Date();
        Assert.assertNotNull("Event iterable is null after transformation", allEvents);
        Assert.assertEquals(3, allEvents.size());
    }
}
