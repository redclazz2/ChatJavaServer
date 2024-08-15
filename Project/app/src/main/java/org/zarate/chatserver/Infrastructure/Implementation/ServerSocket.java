/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.zarate.chatserver.Infrastructure.Implementation;

import java.io.IOException;
import java.net.Socket;
import org.zarate.chatserver.Infrastructure.Interface.IServerSocket;

public class ServerSocket implements IServerSocket {

    private final int port;
    private final int amountClients;
    private java.net.ServerSocket socket;

    public ServerSocket(int port, int amountClients) {
        this.port = port;
        this.amountClients = amountClients;
    }

    //Erease this method ...
    @Override
    public boolean Init() {
        try {
            this.socket = new java.net.ServerSocket(this.port, this.amountClients);
            return true;
        } catch (IOException e) {
            return false;
        }
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
    public Socket Listen() {
        try {
            var socketSession = socket.accept();
            return socketSession;
        } catch (IOException e) {
            return null;
        }
    }
}
