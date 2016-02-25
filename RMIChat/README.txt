README file for RMI Chat application

Check out the source code and build the project, then follow the steps below to register and launch the chat application.

1. RMI registration
Open a new command window and input the following commands
-cd to ~workspace/bin
-rmic com.cs262.rmi.server.ChatServer
-rmic com.cs262.rmi.client.ChatClient
-rmiregistry 2001

2. Start server
Open a new command window 
-cd to ~workspace/bin
-java com.cs262.rmi.server.ChatServer 2001

3. Start Client
Open a new command window
-cd to ~workspace/bin
-java com.cs262.rmi.client.ChatClient 127.0.0.1 2001 user1
(*if you are connecting to a remote server, then change 127.0.0.1 to the IP of remote server)

4. Follow step 3 to start more clients

5. Use the application with following commands:
(1):listaccount
(2):group <groupname> <username1>,<username2>...
(3):listgroup
(4):toaccount <accountname> <message>
(5):togroup <groupname> <message>
(6):signout
(7):delete