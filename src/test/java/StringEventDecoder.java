import com.google.common.collect.Maps;
import com.rocana.event.Event;
import com.rocana.kafka.DecoderException;
import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

import static java.lang.System.out;

/**
 * Created by dave on 7/12/16.
 */
public class StringEventDecoder implements Decoder<Event> {

    /* Spark Streaming Kafka connector expects Decoder classes to have
       a constructor that takes a Properties object. */
    public StringEventDecoder(Properties properties) {
    }

    public StringEventDecoder(VerifiableProperties properties) {

    }

    @Override
    public Event fromBytes(byte[] bytes) {
        Event result = new Event();
        Map<String, String> attributeMap = Maps.newHashMap();
        result.setAttributes(attributeMap);

        try {
            String hexString = new String(bytes, "UTF-8");

            result.setTs(new Date().getTime());
            result.setId(UUID.randomUUID().toString());
            result.setVersion(1L);
            result.setEventTypeId(1234);
            result.setSource("hexString");
            result.setLocation("json_decode");
            result.setHost(InetAddress.getLocalHost().getHostAddress());
            result.setService("hexStringDecode");
            result.setBody(ByteBuffer.wrap(hexString.getBytes()));

        } catch (IOException e) {
            throw new DecoderException("Problem decompressing bytes ", e, bytes);
        } catch(Exception e){
            throw new RuntimeException(e);
        }

        return result;
    }
}
