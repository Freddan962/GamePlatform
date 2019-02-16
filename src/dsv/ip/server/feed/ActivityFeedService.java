package dsv.ip.server.feed;

import dsv.ip.server.Server;
import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

/**
 * @autor Fredrik Sander
 *
 * ActivityFeedService is responsible for providing ActivityFeed related services such as
 * sending and retrieving messages from the feed.
 */
public class ActivityFeedService extends UnicastRemoteObject implements ActivityFeedServiceRemote {

  private MessageStore messageStore = new MessageStore();

  /**
   * Constructs a new ActivityFeedService entity by preparing for RMI connections.
   * @throws RemoteException
   */
  public ActivityFeedService() throws RemoteException {
    try {
      Naming.rebind("rmi://0.0.0.0:" + Server.RMI_PORT + "/activityfeedservice", this);
    } catch (MalformedURLException e) { e.printStackTrace(); }

    System.out.println("ActivityFeed service started.");
  }

  /**
   * Shares a message from a user identified by username.
   * @param fromUsername The username of the user to share the message from.
   * @param message The message to be shared.
   * @throws RemoteException Thrown if server side exception is generated.
   */
  @Override
  public void shareMessage(String fromUsername, String message) throws RemoteException {
    User from = UserManager.getInstance().getUser(fromUsername);
    if (from == null) return;

    if (message.length() > 100)
      message = message.substring(0, 100);

    String formattedMessage = from.getUsername() + ": " + message;

    messageStore.addMessage(new Message(
            new Date(),
            formattedMessage
    ));
  }

  /**
   * Fetches all the latest messages for a specific user
   * @param since
   * @return
   * @throws RemoteException
   */
  @Override
  public List<Message> getLatestMessages(Date since) throws RemoteException {
    return messageStore.getMessages(since);
  }
}
