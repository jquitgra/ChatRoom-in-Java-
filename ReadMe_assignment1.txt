Document: ReadMe file for Multi-threading & Internet networking Chatroom project
Author: John Quitto Graham

Server class: I started this project by creating a server class that has the 
server always waits for clients (users) to connect on port 8888. I used the ServerSocket
object from the java.net library to listen for connections. I then used an array-list of 
clients to easily add and remove them from a 'master list' of users that are connected 
to the server. There is also an indexing variable that keeps track of the index of the 
users in the master list of users. Finally, in an infinite loop accept method is waiting
for client connection and giving the client a socket.

Service class: Within the server class a service class called 'ChatService' is initalized
for every user who connects to the server. In this class the Runnable interface is 
implemented so that instances of the user function to send messages to the chatroom are 
executed by a thread. The run method in the service class recieves a scanner and printWriter
from the socket and executes the command that reads user input and directs to a method
that allows for a messages to be sent by adding the user to the array-list defined in the server.
After each command from after the user is types "LOGIN" and a username, anything that is typed
is broadcasted to the server and relayed to all ocuppants of the chatroom within a loop
that iterates through the array-list of occupants and uses a writer to send the message.
Each thread is synchorined to avoid race condition using Locks.

User class: In this class, a constuctor with a Socket and ID as parameters initalizes 
a User object that uses PrintWriter to recieve output streams. Getters are used for 
the initalized PrintWriter object and the ID, so the output and index of each user 
is recognized by the server and master list of users.

Directions to use: 1) Type 'LOGIN' to enter a username. (press enter)
2) Type in a username (press enter)
3) Send any message.
4) Type 'LOGOUT' to leave the chatroom or exit the terminal.