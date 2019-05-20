import java.util.ArrayList;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class ChatRoom{
    /**
    Constructs a Chat Room with a given number of 
    // occupants in the chat.
    @param size the number of occupants
   */

   public ChatRoom(int size){
       occupants = new ChatUser[size];
       for (int i=0; i<occupants.length; i++){
           occupants[i] = new ChatUser();
        }
   }

   /**
    Users can broadcast a message in the chat room
    @param username the user sending the message
    @param message the message sent in the chat room
    */
    public void sendMessage(int id, String message, String username){
        ChatUser user = occupants[id];
        user.sendMessage(message);
    }

    /**
     Removes the user from the Chat room.
     @param username the user to be logged out
     */
    public void login(int id){
        ChatUser user = occupants[id];
        user.login();
    }
   private ChatUser[] occupants;
}