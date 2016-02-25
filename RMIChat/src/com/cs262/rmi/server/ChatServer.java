package com.cs262.rmi.server;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.cs262.rmi.client.ChatClientInt;
public class ChatServer implements ChatServerInt {

   @SuppressWarnings("unused")
   private static final long serialVersionUID = 1L;
   // Fields to store accounts and groups
   private HashMap<String, ChatClientInt> accounts;
   private HashMap<String, List<String>> groups;
   private HashMap<String, List<String>> undeliveredMessages;
   private HashMap<String, String> accountStatus;
   
   private static final String name = "server";
   
   // Initialize server
   public ChatServer() throws RemoteException {
       accounts = new HashMap<String, ChatClientInt>();
       groups = new HashMap<String, List<String>>();
       undeliveredMessages = new HashMap<String, List<String>>();
       accountStatus = new HashMap<String, String>();
   }
   
   // List account
   public synchronized void listAccount(ChatClientInt client) throws RemoteException {	   
	   client.update(name, "List of accounts: " + accounts.keySet().toString());
   }
   
   
   // List group
   public synchronized void listGroup(ChatClientInt client) throws RemoteException {   
	   client.update(name, "List of groups: " + groups.keySet().toString());
   }
   
   // Send message to account
   public synchronized void sendMessageToAccount(ChatClientInt sender, String receiver, String message) throws RemoteException{
	   if (accounts.containsKey(receiver)){
		   if (accountStatus.get(receiver).equals("ONLINE")){
			   accounts.get(receiver).update(sender.getAccountName(), message);
		   }else{
			   // TODO: Queue the message for offline users  
			   if (undeliveredMessages.containsKey(receiver)){
				   List<String> messages = undeliveredMessages.get(receiver);
				   messages.add(sender.getAccountName()+":"+message);
				   undeliveredMessages.put(receiver, messages);
			   }else {
				   List<String> messages = new ArrayList<String>();
				   messages.add(sender.getAccountName()+":"+message);
				   undeliveredMessages.put(receiver, messages);
			   }
			   
		   }		   
	   }else {
		   sender.update(name, "Account doesn't exist "+receiver);
	   }
   }
   
   // Send message to group
   public synchronized void sendMessageToGroup(ChatClientInt sender, String receiver, String message) throws RemoteException{
	   if (groups.containsKey(receiver)){
		   List<String> group = groups.get(receiver);
		   for (String member : group){
			   sendMessageToAccount(sender, member, message);
		   }
	   }else {
		   sender.update(name, "Group doesn't exist "+receiver);
	   }
   }
   
   // Make a group
   public synchronized boolean createGroup(ChatClientInt sender, String groupName, String members) throws RemoteException{ 
	   if (groups.containsKey(groupName)){
		   return false;
	   }else {
		   String[] groupMembers = members.split(",");
		   groups.put(groupName, Arrays.asList(groupMembers));
		   return true;
	   }	   
   }
   
   // Delete account
   public synchronized void deleteAccount(ChatClientInt client) throws RemoteException {
	   String accountName = client.getAccountName(); 
       accounts.remove(accountName);
       accountStatus.remove(accountName);
       undeliveredMessages.remove(accountName);
       Set<String> groupKeys = groups.keySet();
       for (String groupname : groupKeys){
    	   groups.get(groupname).remove(accountName);
       }       
   }

   // Sign out 
   public synchronized void logOff(ChatClientInt client) throws RemoteException {
	   accountStatus.put(client.getAccountName(), "OFFLINE");
       client.update(name, "Successfully logoff with status:"+accountStatus.get(client.getAccountName()));
   }
   
   public synchronized boolean createAccount(String name, ChatClientInt client) throws RemoteException {
	   
	   if (accounts.containsKey(name) && accountStatus.get(name).equals("ONLINE")){
		   return false;
	   }else{
		   accounts.put(name, client);
		   accountStatus.put(name, "ONLINE");
		   return true;
	   }
   }
   
   public synchronized List<String> fetchUndeliveredMessages(String accountName) throws RemoteException {
	   List<String> messages = undeliveredMessages.get(accountName);
	   undeliveredMessages.remove(accountName);
	   return messages;
   }

   public static void main (String[] args) {
       if (1 != args.length) {
           System.out.println("Usage: java ChatServer <server_port>");
           System.out.println("Example: java ChatServer 2001");
           return;
       }
       int port = Integer.parseInt(args[0]);

       try {
           System.setOut(new PrintStream("server.log"));
           ChatServer server = new ChatServer();
           LocateRegistry.getRegistry(port).bind("ChatServer",
               UnicastRemoteObject.exportObject(server, 0));
           writeLog("Server started at {0}, waiting for connections...");
       } catch(Exception e) {
           e.printStackTrace();
       }
   }

   private static void writeLog(String log) {
       System.out.println(MessageFormat.format(log, new Date().toString()));
   }
}