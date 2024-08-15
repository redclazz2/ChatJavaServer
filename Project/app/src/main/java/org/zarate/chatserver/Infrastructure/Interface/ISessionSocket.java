package org.zarate.chatserver.Infrastructure.Interface;

import java.io.IOException;

public interface ISessionSocket {
    public void Init();
    public boolean Close();
    public void Read() throws IOException;
    public void Write(byte[] data);
}
