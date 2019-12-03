package com.github.galimru.whatsappbot.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.galimru.whatsappbot.deserializers.EventDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = EventDeserializer.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageEvent.class, name = "messages"),
        @JsonSubTypes.Type(value = AcknowledgeEvent.class, name = "ack"),
        @JsonSubTypes.Type(value = UpdateEvent.class, name = "chatUpdate"),
})
public interface AbstractEvent {
}
