package org.zarate.chatserver.Infrastructure.Interface;

import java.net.Socket;

public interface IServerProcess {
    public void Init();
    public void Close();
    public void Listen();
    public void HandleConnection(Socket sessionSocket);
    public void Broadcast(byte[] data);
    public void Broadcast(int sessionId, byte[] data);
    public void Write(int sessionId, byte[] data);
    public void Disconnect(int sessionId);
}
