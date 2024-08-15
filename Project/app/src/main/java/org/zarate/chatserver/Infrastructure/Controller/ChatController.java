package org.zarate.chatserver.Infrastructure.Controller;

import java.util.LinkedHashMap;

import org.zarate.chatserver.Domain.Entity.ChatMessage;
import org.zarate.chatserver.Domain.Port.ChatControllerPort;
import org.zarate.chatserver.Domain.Port.ChatUserListPort;
import org.zarate.chatserver.Helper.Logger.Logger;
import org.zarate.chatserver.Helper.Logger.LoggerLevel;
import org.zarate.chatserver.Infrastructure.Model.Transaction;

public class ChatController implements ChatControllerPort {

    private final String name = "ChatController";
    private final ChatUserListPort userList;

    public ChatController(ChatUserListPort userList) {
        this.userList = userList;
    }

    @Override
    public Transaction HandleMessage(int sessionId, LinkedHashMap data) {
        ChatMessage message = new ChatMessage();
        message.setUsername(data.get("username").toString());
        message.setBody(data.get("body").toString());
        return new Transaction("Message", message);
    }

    @Override
    public Transaction[] HandleConnection(int sessionId, String username) {
        userList.AddUser(sessionId, username);
        String[] usernames = userList.ToArray();

        return new Transaction[]{new Transaction("Welcome", usernames), new Transaction("Connect", username)};
    }
    
    @Override
    public Transaction HandleDisconnection(int sessionId) {
        Logger.Log(LoggerLevel.Info, name, "Disconnecting client: " + sessionId);
        String username = userList.GetUser(sessionId);
        userList.RemoveUser(sessionId);
        
        return new Transaction("Disconnect", username);        
    }
}
