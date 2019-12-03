package com.github.galimru.whatsappbot.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcknowledgeEvent implements AbstractEvent {

    @JsonProperty(value = "ack")
    protected List<Acknowledge> acknowledges;

    public List<Acknowledge> getAcknowledges() {
        return acknowledges;
    }

    public void setAcknowledges(List<Acknowledge> acknowledges) {
        this.acknowledges = acknowledges;
    }
}
