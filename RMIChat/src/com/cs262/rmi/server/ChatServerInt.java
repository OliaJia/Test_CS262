package com.cs262.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.cs262.rmi.client.ChatClientInt;

public interface ChatServerInt extends Remote {
	boolean createAccount(String name, ChatClientInt c) throws RemoteException;
    void deleteAccount(ChatClientInt client) throws RemoteException;
    void logOff(ChatClientInt c) throws RemoteException;
    void listAccount(ChatClientInt client) throws RemoteException;
    void listGroup(ChatClientInt client) throws RemoteException;
    void sendMessageToAccount(ChatClientInt sender, String receiver, String message) throws RemoteException;
    void sendMessageToGroup(ChatClientInt sender, String receiver, String message) throws RemoteException;
    boolean createGroup(ChatClientInt sender, String groupName, String members) throws RemoteException;
    List<String> fetchUndeliveredMessages(String accountName) throws RemoteException;
}
