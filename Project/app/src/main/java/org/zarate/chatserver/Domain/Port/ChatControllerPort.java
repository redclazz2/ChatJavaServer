package org.zarate.chatserver.Domain.Port;

import java.util.LinkedHashMap;

import org.zarate.chatserver.Infrastructure.Model.Transaction;

public interface ChatControllerPort {
    public Transaction HandleMessage(int sessionId, LinkedHashMap data);
    public Transaction[] HandleConnection(int sessionId, String username);
    public Transaction HandleDisconnection(int sessionId);
}
