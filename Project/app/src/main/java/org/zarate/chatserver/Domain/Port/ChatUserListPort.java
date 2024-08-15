package org.zarate.chatserver.Domain.Port;

public interface ChatUserListPort {
    public void AddUser(int sessionId, String username);
    public String GetUser(int sessionId);
    public void RemoveUser(int sessionId);
    public String[] ToArray();
}
