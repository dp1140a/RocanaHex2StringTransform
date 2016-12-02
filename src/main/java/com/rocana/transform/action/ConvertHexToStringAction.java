package com.rocana.transform.action;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.rocana.transform.Action;
import com.rocana.transform.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map;

/**
 * Created by dave on 7/12/16.
 */
public class ConvertHexToStringAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ConvertDateAction.class);
    private String inputHexString;
    private String destination;

    public ConvertHexToStringAction(String inputHexString, String destination) {
        this.inputHexString = inputHexString;
        this.destination = destination;
    }

    public ConvertHexToStringAction(ByteBuffer hexBuffer, String destination) {
        try {
            this.inputHexString = new String(hexBuffer.array(), "utf-8");
            System.out.println(this.inputHexString);
            this.destination = destination;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Status execute(ActionContext context) {
        Map<String, String> destinationMap = context.get(destination);

        if (destinationMap == null) {
            destinationMap = Maps.newHashMap();
            context.put(destination, destinationMap);

        }

        String formattedInput = context.evaluate(inputHexString);
        Preconditions.checkArgument(
                !Strings.nullToEmpty(formattedInput).trim().isEmpty(), "convert-hex action was provided an expression (%s) for input hexString," + " but that expression evaluated to empty", inputHexString);

        StringBuilder output = new StringBuilder();
        try {
            for (int i = 0; i < formattedInput.length(); i += 2) {
                String str = formattedInput.substring(i, i + 2);
                output.append((char) Integer.parseInt(str, 16));
            }
        } catch (Exception e) {
            System.out.println("Error parsing hexString value {}.  Stack trace follows. " + formattedInput + " " + e);
            return Status.FAILURE;
        }

        destinationMap.put("decodedString", output.toString());
        return Status.SUCCESS;
    }
}