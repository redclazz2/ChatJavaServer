package org.zarate.chatserver.Domain.Port;

import java.util.LinkedHashMap;

public interface ChatRouterPort {
    public void Init();
    public void Route(int sessionId, byte[] data);
    public void RouteChatConnect(int sessionId, String username);
    public void RouteChatDisconnect(int sessionId);
    public void RouteChatMessage(int sessionId, LinkedHashMap data);
}
