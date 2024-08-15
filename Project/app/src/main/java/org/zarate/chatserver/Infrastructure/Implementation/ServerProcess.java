package org.zarate.chatserver.Infrastructure.Implementation;

import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.zarate.chatserver.Domain.Port.ChatRouterPort;
import org.zarate.chatserver.Helper.Logger.Logger;
import org.zarate.chatserver.Helper.Logger.LoggerLevel;
import org.zarate.chatserver.Infrastructure.Interface.IServerProcess;
import org.zarate.chatserver.Infrastructure.Interface.IServerSocket;
import org.zarate.chatserver.Infrastructure.Interface.ISessionSocket;

public class ServerProcess implements IServerProcess {

    private final String name = "ServerProcess";
    private final IServerSocket socket;
    private final ChatRouterPort router;

    private final Map<Integer, SessionSocket> clients = new ConcurrentHashMap<>();

    public ServerProcess(IServerSocket socket, ChatRouterPort router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void Init() {
        Logger.Log(LoggerLevel.Info, name, "Initializing Communication Module");

        if (socket.Init()) {
            Logger.Log(LoggerLevel.Info, name, "Socket Initialized");
            Listen();
        } else {
            Logger.Log(LoggerLevel.Error, name, "Socket Failure");
        }
    }

    @Override
    public void Close() {
        if (socket.Close()) {
            //TODO: Clean client sockets
            Logger.Log(LoggerLevel.Info, name, "Bye");
        } else {
            Logger.Log(LoggerLevel.Error, name, "Error on communication module clean up");
        }
    }

    @Override
    public void Listen() {
        while (true) {
            Logger.Log(LoggerLevel.Info, name, "Listening");

            var sessionSocket = socket.Listen();

            if (sessionSocket != null) {
                Logger.Log(LoggerLevel.Info, name, "Client accepted. New thread started");

                Thread thread = new Thread(() -> HandleConnection(sessionSocket));
                thread.start();
            } else {
                Logger.Log(LoggerLevel.Info, name, "Error accepting client");
            }

        }
    }

    @Override
    public void HandleConnection(Socket sessionSocket) {
        try {
            Logger.Log(LoggerLevel.Info, name, "Setting up client session");
            SessionSocket session = new SessionSocket(sessionSocket, this.getUserId(), this.router);
            Logger.Log(LoggerLevel.Info, name, "ClieusernameMapnt registered with id: " + session.sessionId);
            clients.put(session.sessionId, session);

            session.Init();
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, name, "Error while handling connection: " + e);
        }
    }

    @Override
    public void Broadcast(byte[] data) {
        int count = 0;
        Logger.Log(LoggerLevel.Info, name, "Broadcasting ... ");
        for (ISessionSocket session : clients.values()) {
            session.Write(data);
            count++;
        }
        Logger.Log(LoggerLevel.Info, name, "Broadcasted to " + count + " clients");
    }

    @Override
    public void Broadcast(int sessionId, byte[] data) {
        int count = 0;
        Logger.Log(LoggerLevel.Info, name, "Broadcasting ... ");
        for (SessionSocket session : clients.values()) {
            if (session.sessionId != sessionId) {
                session.Write(data);
            }
            count++;
        }
        Logger.Log(LoggerLevel.Info, name, "Broadcasted to " + count + " clients");
    }

    @Override
    public void Write(int sessionId, byte[] data) {
        ISessionSocket session = clients.get(sessionId);
        session.Write(data);
    }

    @Override
    public void Disconnect(int sessionId) {
        if (this.clients.containsKey(sessionId)) {
            var client = this.clients.get(sessionId);
            client.running = false;
            Logger.Log(LoggerLevel.Info, name, "Removing client#" + sessionId + " from socket list");
            this.clients.remove(sessionId);
        } else {
            Logger.Log(
                    LoggerLevel.Warning, name,
                    "Client#" + sessionId + " not found on socket list. You're never supposed to see this message!!!");
        }
    }

    private Integer getUserId() {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = secureRandom.nextInt(10000);

        while (clients.containsKey(randomNumber)) {
            randomNumber = secureRandom.nextInt(10000);
        }

        return randomNumber;
    }
}
