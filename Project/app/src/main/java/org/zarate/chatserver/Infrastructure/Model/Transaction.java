package org.zarate.chatserver.Infrastructure.Model;

public class Transaction {
    public String route;
    public Object data;

    public Transaction(){}

    public Transaction(String route, Object data){
        this.route = route;
        this.data = data;
    }
}
