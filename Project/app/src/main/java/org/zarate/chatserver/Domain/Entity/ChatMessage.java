package org.zarate.chatserver.Domain.Entity;

public class ChatMessage {
    String username;
    String body;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
