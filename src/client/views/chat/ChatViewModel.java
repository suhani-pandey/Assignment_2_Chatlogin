package client.views.chat;

import client.core.ModelFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Message;
import shared.util.Request;

import java.beans.PropertyChangeEvent;

public class ChatViewModel
{
  private ModelFactory modelFactory;
  private ObservableList<String> users;
  private ObservableList<Message> chatViewsIndivisualChat;
  private ObservableList<Message> chatViewsGlobalChat;
  private StringProperty messageTextField;

  public ChatViewModel(ModelFactory modelFactory)
  {
    this.modelFactory = modelFactory;
    messageTextField = new SimpleStringProperty();
    users = FXCollections.observableArrayList(modelFactory.getLoginModel().getAllUsers());
     //chatViewsGlobalChat=FXCollections.observableArrayList(modelFactory.getChatModel().getMessages());

    modelFactory.getLoginModel().addListener(Request.TYPE.ONLOGGEDINADDUSER.toString(),this::userAdded);
    modelFactory.getChatModel().addListener("addMessage",this::addMessage);
  }

  private void addMessage(PropertyChangeEvent event)
  {
    Platform.runLater(()-> chatViewsGlobalChat.add(
        (Message) event.getNewValue()));
  }

  private void userAdded(PropertyChangeEvent event)
  {
    Platform.runLater(()->users.add((String) event.getNewValue()));
  }

  public ObservableList<String> getUsers()
  {
    return users;
  }

  public void sendMessage()
  {
    Message message= new Message(modelFactory.getLoginModel().getUser().getUserName(),messageTextField.get());
  }

  public ObservableList<Message> getMessages(){
    return chatViewsGlobalChat;
  }
}
