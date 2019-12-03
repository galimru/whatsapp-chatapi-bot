package com.github.galimru.whatsappbot.listeners;

import com.github.galimru.whatsappbot.events.Acknowledge;
import com.github.galimru.whatsappbot.server.ServerResponse;

public interface AcknowledgeListener extends EventListener {
    void handle(Acknowledge acknowledge, ServerResponse response);
}
