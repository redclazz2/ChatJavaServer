package org.zarate.chatserver.Infrastructure.Implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.zarate.chatserver.Domain.Port.ChatRouterPort;
import org.zarate.chatserver.Helper.Logger.Logger;
import org.zarate.chatserver.Helper.Logger.LoggerLevel;
import org.zarate.chatserver.Infrastructure.Interface.ISessionSocket;

public class SessionSocket implements ISessionSocket {
    public Integer sessionId;
    public Socket socket;
    public volatile boolean running = true;

    private String name = "SessionSocket";
    private final InputStream inputStream;
    private final OutputStream outputStream;  
    private final ChatRouterPort router;

    public SessionSocket(Socket socket, Integer sessionId, ChatRouterPort controller) throws IOException {
        this.socket = socket;
        this.sessionId = sessionId;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.name = "SessionSocket#" + sessionId.toString();
        this.router = controller;
    }

    @Override
    public void Init() {
        new Thread(this::ReadThread).start();
    }

    @Override
    public boolean Close() {
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void Read() throws IOException {
        while (this.running) {
            byte[] lengthBuffer = new byte[4];
            inputStream.read(lengthBuffer);
            //order(ByteOrder.LITTLE_ENDIAN).
            int messageLength = ByteBuffer.wrap(lengthBuffer).getInt();

            if (messageLength > 0) {
                byte[] buffer = new byte[messageLength];
                inputStream.read(buffer, 0, messageLength);

                router.Route(this.sessionId, buffer);
            } else {
                Logger.Log(LoggerLevel.Warning, name, "Forcefully disconnected");
                this.router.Route(sessionId, "{\"route\":\"Disconnect\", \"data\": \"\"}".getBytes(StandardCharsets.UTF_8));
                this.running = false;
            }
        }

        this.Close();
    }

    private void ReadThread() {
        try {
            Logger.Log(LoggerLevel.Info, name, "Read thread started");
            Read();
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, name, "Error while reading data: " + e.getMessage());
            this.router.Route(sessionId, "{\"route\":\"Disconnect\", \"data\": \"\"}".getBytes(StandardCharsets.UTF_8));
            Close();
        }
    }

    @Override
    public void Write(byte[] data) {
        try {
            byte[] lengthBytes = ByteBuffer.allocate(4).putInt(data.length).array();

            outputStream.write(lengthBytes);
            outputStream.flush();

            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, name, "Error while writing data: " + e.getMessage());
        }
    }
}
