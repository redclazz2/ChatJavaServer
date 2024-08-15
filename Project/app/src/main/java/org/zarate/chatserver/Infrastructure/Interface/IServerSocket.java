package org.zarate.chatserver.Infrastructure.Interface;
import java.net.Socket;

public interface IServerSocket {
    public boolean Init();
    public boolean Close(); 
    public Socket Listen();
}
