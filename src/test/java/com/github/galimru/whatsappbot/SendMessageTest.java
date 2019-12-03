package com.github.galimru.whatsappbot;

import com.github.galimru.whatsappbot.api.SendMessageResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SendMessageTest {

    private WhatsappBot whatsappBot;

    @Before
    public void setup() {
        whatsappBot = new WhatsappBot(Constants.BASE_URL, Constants.TOKEN);
    }

    @Test
    public void shouldSendMessageByChatId() {
        SendMessageResponse response = whatsappBot.sendMessage(Constants.CHAT_ID, "shouldSendMessageByChatId");
        Assert.assertEquals(true, response.getSent());
    }

    @Test
    public void shouldSendMessageByPhone() {
        SendMessageResponse response = whatsappBot.sendMessage(Long.parseLong(Constants.PHONE), "shouldSendMessageByPhone");
        Assert.assertEquals(true, response.getSent());
    }
}
