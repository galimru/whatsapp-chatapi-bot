package com.github.galimru.whatsappbot.listeners;

import com.github.galimru.whatsappbot.events.Message;
import com.github.galimru.whatsappbot.server.ServerResponse;

public interface MessageListener extends EventListener {
    void handle(Message message, ServerResponse response);
}
