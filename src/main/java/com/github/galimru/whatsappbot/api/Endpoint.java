package com.github.galimru.whatsappbot.api;

public enum Endpoint {
    WEBHOOK("/webhook"),
    SEND_MESSAGE("/sendMessage");

    private String path;

    Endpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
