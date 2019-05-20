import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 Executes Simple Chat Access Protocol commands
 from a socket
 */

public class ChatService implements Runnable{
    /**
     Constructs a service object that processes commands 
     from a socket for a chat room.
     @param aSocket the socket
     @param aChatRoom the chatroom
     */

     public ChatService(Socket aSocket, int aId, ArrayList<ChatUser> masList){
         s = aSocket;
         id = aId;
         mast_list = masList;
         userChangeLock = new ReentrantLock();
         messageSentCondition = userChangeLock.newCondition();
     }

     public void run(){
         try{
             userChangeLock.lock();
             try{
                 in = new Scanner(s.getInputStream());
                 out = new PrintWriter(s.getOutputStream());
                 ChatUser user = new ChatUser(s,id);
                 mast_list.add(user);
                 //user.getP().println("Welcome to the chatroom, please type 'LOGIN' to enter your username or 'LOGOUT' to leave");
                 doService(user);
                 messageSentCondition.signalAll();
             }
             finally{
                 s.close();
                 userChangeLock.unlock();
             }
         }
         catch (IOException exception){
             exception.printStackTrace();
         }
     }
     /**
      Executes all commands until the LOGOUT command or the end of input
      */

      public void doService(ChatUser user) throws IOException{

          while(true){
              user.getP().println("Message");
              if(!in.hasNext()) return;
              String command = in.next();
              try{
                if(command.equals("LOGOUT"))
                {
                //remove client records from mast_list
                mast_list.remove(user);
                user.getP().close();
                for (ChatUser u : mast_list){
                    u.getP().println(user.getUsername()+" left the chatroom");
                    u.getP().flush();
                }
                    return;
                } 
                else executeCommand(command,user);
            }
            catch (java.util.ConcurrentModificationException e){ // catch exception when everyone left the chatroom
                return;
            }
            catch (java.util.NoSuchElementException e){ //catch exception for when the user exits the terminal
                mast_list.remove(user);
                user.getP().close();
                for (ChatUser u : mast_list){
                    u.getP().println(user.getUsername()+" left the chatroom");
                    u.getP().flush();
                }
                    return;
            }
          }
      }

      /**
       Executes a single command.
       @param command the command to execute
       */
      public void executeCommand(String command,ChatUser user){
          if (command.equals("LOGIN")){
              String username = in.next();
            user.setUsername(username); 
            for (ChatUser u : mast_list)
                u.getP().println(user.getUsername()+" joined the chatroom");
            while(true){
                String message = in.nextLine();
                for (ChatUser u : mast_list){
                    if (message.equals("LOGOUT")){
                        //remove client records from mast_list
                        mast_list.remove(user);
                        u.getP().println(user.getUsername()+" left the chatroom");
                        u.getP().flush();
                        user.getP().close();
                        
                    }

                    else{
                            //relay messages to all the occupants
                            u.getP().println(user.getUsername()+": " + message);
                            u.getP().flush();
                        }
                }
            }
          }
          else{
              user.getP().println("Please type 'LOGIN' to login or 'LOGOUT' to leave");
          }
        
      }

      private Socket s;
      private Scanner in;
      private PrintWriter out;
      private int id;
      private ArrayList<ChatUser> mast_list;
      private Lock userChangeLock;
      private Condition messageSentCondition;
}