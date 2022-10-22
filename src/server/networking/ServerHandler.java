package server.networking;

import server.model.ChatHandler;
import server.model.Login;
import shared.Message;
import shared.User;
import shared.util.Request;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerHandler implements Runnable
{
  private Socket  socket;
  private ChatHandler chatHandler;
  private ConnectionPool pool;
  private Login login;
  private User user;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;

  public ServerHandler(Socket socket,Login login,ConnectionPool pool,ChatHandler chatHandler){
    this.socket=socket;
    this.login=login;
    this.chatHandler=chatHandler;
    this.pool=pool;

    try
    {
      inputStream= new ObjectInputStream(socket.getInputStream());
      outputStream= new ObjectOutputStream(socket.getOutputStream());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override public void run()
  {
    while (true){
      try
      {
        //System.out.println("Server handler");
        Request response= (Request) inputStream.readObject();


        if ("Listener".equals(response.getType())) {
          this.user= (User) response.getArg();
          pool.addConnection(this);
          login.addListener(Request.TYPE.ONLOGGEDINADDUSER.toString(),this::onUserLoggedIn);
          chatHandler.addListener("addMessage",this::messageAdded);

        }
        if (Request.TYPE.ADDUSER.toString().equals(response.getType())){
          boolean temp= login.addUser((User) response.getArg());
          outputStream.writeObject(new Request(Request.TYPE.ADDUSER.toString(),temp));

          break;
        }else if (Request.TYPE.LOGINPOSSIBLE.toString().equals(response.getType()))
        {
          boolean temp = login.login((User) response.getArg());
          if (temp)
          {
            this.user = user;
            //System.out.println(temp + " server handler");
            outputStream.writeObject(new Request(Request.TYPE.LOGINPOSSIBLE.toString(), temp));

            pool.broadcastUserName((User) response.getArg());
          }
          break;
        }else if (Request.TYPE.USERLIST.toString().equals(response.getType())){
            List<String> loginAllUsers = login.getAllUsers();
            outputStream.writeObject(new Request(Request.TYPE.USERLIST.toString(),loginAllUsers));
            break;
          }
          else if("addMessage".equals(response.getType())){
            chatHandler.addMessages((Message) response.getArg());
            pool.broadcastMessage((Message) response.getArg());
            break;
          }
          else if ("getMessage".equals(response.getType())){
            List<Message> messages= chatHandler.getMessages();
            outputStream.writeObject(new Request("getMessage",messages));
        }
        }
      catch (IOException | ClassNotFoundException e)
      {
        e.printStackTrace();
      }
    }
    }

  private void messageAdded(PropertyChangeEvent event)
  {
    try
    {
      outputStream.writeObject(new Request(event.getPropertyName(),event.getNewValue()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  private void onUserLoggedIn(PropertyChangeEvent event)
  {
    try
    {
      outputStream.writeObject(new Request(event.getPropertyName(),event.getNewValue()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void sendMessageToClient(Message msg)
  {
    try
    {
      outputStream.writeObject(new Request("addMessage",msg));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public User getUser()
  {
    return user;
  }

  public void sendUsers(User users)
  {
    try
    {
      outputStream.writeObject(new Request(Request.TYPE.ONLOGGEDINADDUSER.toString(),users));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }


}
