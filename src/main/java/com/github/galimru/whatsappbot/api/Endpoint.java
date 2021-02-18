package com.github.galimru.whatsappbot.api;

public enum Endpoint {
    WEBHOOK("/webhook"),
    SEND_MESSAGE("/sendMessage"),
    SEND_FILE("/sendFile");

    private String path;

    Endpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
