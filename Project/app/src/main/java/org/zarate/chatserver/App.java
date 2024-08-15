package org.zarate.chatserver;

import org.zarate.chatserver.Application.ChatUserList;
import org.zarate.chatserver.Domain.Port.ChatRouterPort;
import org.zarate.chatserver.Infrastructure.Controller.ChatController;
import org.zarate.chatserver.Infrastructure.Router.ChatRouter;

public class App {
    public static void main(String[] args) {
        ChatRouterPort router = new ChatRouter(
            new ChatController(
                new ChatUserList()
            )
        );
        router.Init();
    }
}
