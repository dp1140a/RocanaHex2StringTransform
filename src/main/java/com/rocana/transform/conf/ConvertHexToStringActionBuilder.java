/*
 * Copyright (c) 2016 Rocana. All rights reserved.
 */

package com.rocana.transform.conf;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.rocana.configuration.annotations.ConfigurationProperty;
import com.rocana.transform.action.ConvertHexToStringAction;


@ConfigurationProperty(name = "convert-hex-string")
public class ConvertHexToStringActionBuilder implements ActionBuilder<ConvertHexToStringAction> {

    private String source;
    // New map attribute stuff
    private String destination;

    private String inputHexString;

    @Override
    public ConvertHexToStringAction build() {
        Preconditions.checkState(!Strings.nullToEmpty(inputHexString).isEmpty(), "The convert-hex-string action requires a hexString");

        // Return a new instance of ORSParseAction.
        return new ConvertHexToStringAction(inputHexString, destination);
    }

    // New map attribute stuff
    public String getDestination() {
        return destination;
    }

    @ConfigurationProperty(name = "destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getInputHexString() {
        return inputHexString;
    }

    @ConfigurationProperty(name = "hexString")
    public void setInputHexString(String inputHexString) {
        this.inputHexString = inputHexString;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("map", destination)
                .add("inputHexString", inputHexString)
                .toString();
    }

}

