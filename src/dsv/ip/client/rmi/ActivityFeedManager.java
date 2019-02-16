package dsv.ip.client.rmi;

import dsv.ip.client.Main;
import dsv.ip.client.Session;
import dsv.ip.server.feed.ActivityFeedServiceRemote;
import dsv.ip.server.feed.Message;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Fredrik Sander
 *
 * Responsible for providing functionality that works together with the
 * server's activity feed system functionality.
 */
public class ActivityFeedManager {

  private ActivityFeedServiceRemote service;
  private boolean updatingMessages = false;
  private Date lastFetchDate = new Date();
  private int updateInterval = 2500;

  private Executor executor = Executors.newCachedThreadPool();

  private static ActivityFeedManager instance = new ActivityFeedManager();

  /**
   * Prepares a new ActivityFeedManager by initiating connection.
   */
  private ActivityFeedManager() {
    initiateConnection();
  }

  /**
   * Fetches the static ActivityFeedManager instance.
   * @return Returns the ActivityFeedManager singleton.
   */
  public static ActivityFeedManager getInstance() {
    return instance;
  }

  /**
   * Initiates the RMI connection.
   */
  private void initiateConnection() {
    try {
      service = (ActivityFeedServiceRemote) Naming.lookup("rmi://" + Main.SERVER_HOST + ":1100/activityfeedservice");
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Begins updating messages for the ActivityFeed.
   * Updates on a set interval, fills the provided TextArea with the actual content of the feed.
   * @param messages The TextArea to be filled with the activity feed messages.
   */
  public void updateMessages(TextArea messages) {
    if (updatingMessages)
      return;

    updatingMessages = true;

    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      do {
        try {
          List<Message> latest = service.getLatestMessages(lastFetchDate);
          lastFetchDate = new Date();

          Platform.runLater(() -> {
            StringBuilder text = new StringBuilder();
            text.append(messages.getText());
            for (Message msg : latest)
              text.append(msg.getMessage() + "\n");

            String finalText = text.toString();
            messages.setText(finalText);
          });

          Thread.sleep(updateInterval);
        } catch (Exception e) { e.printStackTrace(); }
      } while (true);
    }, executor);
  }

  /**
   * Sends a message to the ActivityFeed.
   * @param message The message to be sent.
   */
  public void sendMessage(String message) {
    try {
      service.shareMessage(Session.getInstance().getUsername(), message);
    } catch (RemoteException e) { e.printStackTrace(); }
  }
}
