package com.github.galimru.whatsappbot.deserializers;

import com.github.galimru.whatsappbot.events.AbstractEvent;

public class EventDeserializer extends PresenceDeserializer<AbstractEvent> {

    public EventDeserializer() {
        super(AbstractEvent.class);
    }
}
