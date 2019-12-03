package com.github.galimru.whatsappbot.listeners;

import com.github.galimru.whatsappbot.events.Update;
import com.github.galimru.whatsappbot.server.ServerResponse;

public interface UpdateListener extends EventListener {
    void handle(Update update, ServerResponse response);
}
