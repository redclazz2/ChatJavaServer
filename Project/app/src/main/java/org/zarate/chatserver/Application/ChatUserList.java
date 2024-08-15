package org.zarate.chatserver.Application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.zarate.chatserver.Domain.Port.ChatUserListPort;
import org.zarate.chatserver.Helper.Logger.Logger;
import org.zarate.chatserver.Helper.Logger.LoggerLevel;

public class ChatUserList implements ChatUserListPort{
    private final String name = "ChatUserList";
    private final Map<Integer, String> userMap = new ConcurrentHashMap<>();

    @Override
    public synchronized void AddUser(int sessionId, String username) {
        Logger.Log(LoggerLevel.Info, name, "Adding client#" + sessionId + " to username list as: " + username);
        userMap.put(sessionId, username);
    }

    @Override
    public synchronized void RemoveUser(int sessionId) {
        if (userMap.containsKey(sessionId)) {
            Logger.Log(LoggerLevel.Info, name, "Removed client#" + sessionId + " from username map");
            userMap.remove(sessionId);
        }else{
             Logger.Log(LoggerLevel.Info, name, "Client#" + sessionId + " not found on username map");
        }
    }

    @Override
    public synchronized String[] ToArray() {
        String[] usernames = new String[userMap.values().size()];
        userMap.values().toArray(usernames);

        return usernames;
    }

    @Override
    public synchronized String GetUser(int sessionId) {
        return userMap.get(sessionId);
    }
}
