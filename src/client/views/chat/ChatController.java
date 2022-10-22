package client.views.chat;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.views.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import shared.Message;

public class ChatController implements ViewController
{
  private ViewHandler viewHandler;
  private ViewModelFactory viewModelFactory;

  @FXML private ListView<Message> chatViewsIndivisualChat;
  @FXML private ListView<String> onlineUserViews;
  @FXML private ListView<Message> chatViewsGlobalChat;

  @FXML private TextField messageTextField;


  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler=viewHandler;
    this.viewModelFactory=viewModelFactory;

    onlineUserViews.setItems(viewModelFactory.getChatViewModel().getUsers());

  }


  public void onSendButton(ActionEvent actionEvent)
  {
    viewModelFactory.getChatViewModel().sendMessage();
    messageTextField.clear();
  }

  //Global chat
  public void onBackToGlobalChat(ActionEvent actionEvent)
  {
    viewHandler.openGlobalChatPage();
  }

  public void onSelectUserButtonGlobalChat(ActionEvent actionEvent)
  {

  }

  //Indivisual chat

  public void onSelectUserButtonIndivisualChat(ActionEvent actionEvent)
  {

  }


}
