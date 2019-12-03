package com.github.galimru.whatsappbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.galimru.whatsappbot.api.*;
import com.github.galimru.whatsappbot.events.*;
import com.github.galimru.whatsappbot.listeners.AcknowledgeListener;
import com.github.galimru.whatsappbot.listeners.EventListener;
import com.github.galimru.whatsappbot.listeners.MessageListener;
import com.github.galimru.whatsappbot.listeners.UpdateListener;
import com.github.galimru.whatsappbot.server.CallbackServer;
import com.github.galimru.whatsappbot.server.DefaultHandler;
import com.github.galimru.whatsappbot.server.ServerResponse;
import com.google.common.net.HttpHeaders;
import okhttp3.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhatsappBot {

    private final static String TOKEN_PARAM = "token";
    private final static String USER_AGENT = "WhatsappBot/1.0.0";
    private final static String CONTENT_TYPE = "application/json; charset=utf-8";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String baseUrl;
    private String token;

    private OkHttpClient httpClient;
    private CallbackServer callbackServer;

    private Set<EventListener> eventListeners = new HashSet<>();

    public WhatsappBot(String baseUrl, String token) {
        this.baseUrl = normalizeUrl(baseUrl);
        this.token = token;
        this.httpClient = new OkHttpClient();
        this.callbackServer = new CallbackServer();
    }

    public void addMessageListener(MessageListener listener) {
        addEventListener(listener);
    }

    public void addAcknowledgeListener(AcknowledgeListener listener) {
        addEventListener(listener);
    }

    public void addUpdateListener(UpdateListener listener) {
        addEventListener(listener);
    }

    protected void addEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void handle(AbstractEvent event) {
        handle(event, null);
    }

    public void handle(AbstractEvent event, ServerResponse response) {
        eventListeners.forEach(listener -> {
            if (event instanceof MessageEvent && listener instanceof MessageListener) {
                List<Message> messages = ((MessageEvent) event).getMessages();
                messages.forEach(message -> {
                    if (message.getFromMe() != Boolean.TRUE) {
                        ((MessageListener) listener).handle(message, response);
                    }
                });
            }
            if (event instanceof AcknowledgeEvent && listener instanceof AcknowledgeListener) {
                List<Acknowledge> acknowledges = ((AcknowledgeEvent) event).getAcknowledges();
                acknowledges.forEach(acknowledge -> ((AcknowledgeListener) listener).handle(acknowledge, response));
            }
            if (event instanceof UpdateEvent && listener instanceof UpdateListener) {
                List<Update> updates = ((UpdateEvent) event).getUpdates();
                updates.forEach(update -> ((UpdateListener) listener).handle(update, response));
            }
        });
    }

    public void listen(String host, Integer port, String path) throws IOException {
        callbackServer.addCallbackHandler(path, new DefaultHandler(this));
        callbackServer.listen(host, port);
    }

    public void listen(Integer port, String path) throws IOException {
        listen(null, port, path);
    }

    public void listen(String path) throws IOException {
        listen(null, null, path);
    }

    public SetWebhookResponse setWebhook(String url) {
        SetWebhook webhook = new SetWebhook();
        webhook.setUrl(url);
        return executeRequest(Endpoint.WEBHOOK,
                webhook, SetWebhookResponse.class);
    }

    public SendMessageResponse sendMessage(Long phone, String text) {
        SendMessage message = new SendMessage();
        message.setPhone(phone);
        message.setBody(text);
        return executeRequest(Endpoint.SEND_MESSAGE,
                message, SendMessageResponse.class);
    }

    public SendMessageResponse sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setBody(text);
        return executeRequest(Endpoint.SEND_MESSAGE,
                message, SendMessageResponse.class);
    }

    private String normalizeUrl(String url) {
        if (url.endsWith("/")) {
            url = url.replaceFirst("/$", "");
        }
        return url;
    }

    private <T> T executeRequest(Endpoint endpoint, Object payload, Class<T> responseClass)
            throws ApiException {
        String content;
        try {
            content = OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new ApiException(String.format("Cannot deserialize request payload: %s", payload), e);
        }
        String response;
        try {
            HttpUrl url = HttpUrl.get(baseUrl + endpoint.getPath()).newBuilder()
                    .addQueryParameter(TOKEN_PARAM, token)
                    .build();
            Response httpResponse = httpClient.newCall(new Request.Builder()
                    .url(url)
                    .addHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                    .post(RequestBody.create(MediaType.parse(CONTENT_TYPE), content))
                    .build()).execute();
            ResponseBody body = httpResponse.body();
            if (body == null) {
                throw new ApiException("Cannot get body content from cached request");
            }
            response = body.string();
        } catch (IOException e) {
            throw new ApiException(String.format("Cannot execute request %s with payload %s", endpoint, payload), e);
        }
        try {
            return OBJECT_MAPPER.readValue(response, responseClass);
        } catch (IOException e) {
            throw new ApiException(String.format("Cannot serialize response payload: %s", response));
        }
    }

}
