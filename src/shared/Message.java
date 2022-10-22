package shared;

public class Message
{
  private String user;
  private String messageBody;

  public Message(String user, String messageBody)
  {
    this.user = user;
    this.messageBody = messageBody;
  }

  public String getUser()
  {
    return user;
  }

  public String getMessageBody()
  {
    return messageBody;
  }
}
