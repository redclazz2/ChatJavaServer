package org.zarate.chatserver.Infrastructure.Router;

import java.util.LinkedHashMap;

import org.zarate.chatserver.Domain.Port.ChatControllerPort;
import org.zarate.chatserver.Domain.Port.ChatRouterPort;
import org.zarate.chatserver.Helper.Formatter.Formatter;
import org.zarate.chatserver.Helper.Logger.Logger;
import org.zarate.chatserver.Helper.Logger.LoggerLevel;
import org.zarate.chatserver.Infrastructure.Implementation.ServerProcess;
import org.zarate.chatserver.Infrastructure.Implementation.ServerSocket;
import org.zarate.chatserver.Infrastructure.Interface.IServerProcess;
import org.zarate.chatserver.Infrastructure.Model.Transaction;

public class ChatRouter implements ChatRouterPort {

    private final String name = "ChatRouter";
    private final ChatControllerPort controller;
    private final IServerProcess server;

    public ChatRouter(ChatControllerPort controller) {
        this.controller = controller;
        this.server = new ServerProcess(
                new ServerSocket(
                        8059,
                        10
                ),
                this
        );
    }

    @Override
    public void Init(){
        server.Init();
    }

    @Override
    public void Route(int sessionId, byte[] data) {
        Transaction obj = Formatter.Deserialize(data, Transaction.class);
        if (obj != null) {
            Logger.Log(LoggerLevel.Info, name, "Routing request from client#" + sessionId);

            switch (obj.route) {
                case "Connect" ->
                    this.RouteChatConnect(sessionId, (String) obj.data);
                case "Disconnect" ->
                    this.RouteChatDisconnect(sessionId);
                case "Message" ->
                    this.RouteChatMessage(sessionId, (LinkedHashMap) obj.data);
            }
        } else {
            Logger.Log(LoggerLevel.Info, name, "Unable to read request from client#" + sessionId);
        }
    }

    @Override
    public void RouteChatConnect(int sessionId, String username) {
        Transaction[] chatConnection = this.controller.HandleConnection(sessionId, username);
        //Sends username list
        Logger.Log(LoggerLevel.Info, name, "Sending username list to new client");
        server.Write(sessionId, Formatter.Serialize(chatConnection[0]));

        //send all other clients new username
        server.Broadcast(sessionId, Formatter.Serialize(chatConnection[1]));
    }

    @Override
    public void RouteChatDisconnect(int sessionId) {
        Transaction chatDisconnection = this.controller.HandleDisconnection(sessionId);
        server.Broadcast(sessionId, Formatter.Serialize(chatDisconnection));
        server.Disconnect(sessionId);
    }

    @Override
    public void RouteChatMessage(int sessionId, LinkedHashMap data) {
        Transaction messageTransaction = this.controller.HandleMessage(sessionId, data);
        Logger.Log(LoggerLevel.Info, name, "Broascasting message sent by: " + sessionId);
        this.server.Broadcast(sessionId,
                Formatter.Serialize(messageTransaction)
        );
    }
}
