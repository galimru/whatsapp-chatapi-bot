package com.github.galimru.whatsappbot;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

public class EventListenerTest {

    private WhatsappBot whatsappBot;

    @Before
    public void setup() throws IOException {
        whatsappBot = new WhatsappBot(Constants.BASE_URL, Constants.TOKEN);
        whatsappBot.listen(Constants.WEBHOOK_PATH);
        whatsappBot.setWebhook(Constants.WEBHOOK_URL);
    }

    @Test
    public void shouldReceiveMessageEvent() {
        AtomicBoolean eventReceived = new AtomicBoolean();
        whatsappBot.addMessageListener((message, response) -> {
            eventReceived.set(true);
        });
        whatsappBot.sendMessage(Long.parseLong(Constants.PHONE), "тест");
        await().atMost(60, TimeUnit.SECONDS).untilTrue(eventReceived);
    }

}
