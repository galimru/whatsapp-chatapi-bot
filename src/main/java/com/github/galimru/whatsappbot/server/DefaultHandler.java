package com.github.galimru.whatsappbot.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.galimru.whatsappbot.WhatsappBot;
import com.github.galimru.whatsappbot.events.AbstractEvent;
import com.google.common.io.CharStreams;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import java.io.IOException;

public class DefaultHandler implements CallbackHandler {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private WhatsappBot whatsappBot;

    public DefaultHandler(WhatsappBot whatsappBot) {
        this.whatsappBot = whatsappBot;
    }

    @Override
    public void handle(Request request, Response response) throws IOException {
        if (request.getMethod() == Method.POST) {
            String content = CharStreams.toString(request.getReader());
            AbstractEvent event = OBJECT_MAPPER.readValue(content, AbstractEvent.class);
            whatsappBot.handle(event, new GrizzlyServerResponse(response));
        } else {
            response.sendError(HttpStatus.METHOD_NOT_ALLOWED_405.getStatusCode());
        }
    }
}
