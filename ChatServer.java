import java.io.IOException;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 A Server that executes chat access protocol
 */

 public class ChatServer{

     public static void main(String[] args) throws IOException {
         final int SBAP_PORT = 8888;
         ServerSocket server = new ServerSocket(SBAP_PORT);
         System.out.println("waiting for clients to connect...");
         ArrayList<ChatUser> mast_list = new ArrayList<ChatUser>();
         int clientId = 0;
         while(true){
             Socket s = server.accept();
             System.out.println("New Client gets connected.");
             //pw.println("Welcome to the chatroom, please type 'LOGIN' to enter your username or 'LOGOUT' to leave");
             ChatService service = new ChatService(s, clientId++, mast_list); 
             Thread t = new Thread(service);
             t.start();
         }

     }
 }