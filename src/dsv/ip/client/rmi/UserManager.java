package dsv.ip.client.rmi;

import dsv.ip.client.Main;
import dsv.ip.client.Session;
import dsv.ip.client.helpers.AlertHelper;
import dsv.ip.server.user.UserServiceRemote;
import dsv.ip.shared.status.AddFriendStatus;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventSystem;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Fredrik Sander
 *
 * Responsible for providing functionality that works together with the
 * server's user related functionality.
 */
public class UserManager {

  private UserServiceRemote service;
  private Executor executor = Executors.newCachedThreadPool();

  private static UserManager manager = new UserManager();

  /**
   * Prepares a new UserManager by initiating connection.
   */
  private UserManager() {
    initiateConnection();
  }

  /**
   * Fetches the static UserManager instance.
   * @return Returns the UserManager singleton.
   */
  public static UserManager getInstance() {
    return manager;
  }

  /**
   * Initiates the RMI connection.
   */
  private void initiateConnection() {
    try {
      service = (UserServiceRemote) Naming.lookup("rmi://" +  Main.SERVER_HOST + ":1100/userservice");
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Searches for all existing users based on a filter that the user enters.
   * @param listView The ListView to fill the results with.
   * @param filter The filter to be used in the searching process.
   */
  public void searchUsers(ListView listView, String filter) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        List<String> userNames = service.getUsernames(filter);
        if (userNames == null) return;

        Platform.runLater(() -> {
          listView.getItems().clear();
          for (String name : userNames) {
            listView.getItems().add(name);
          }

          if (userNames.isEmpty())
            listView.getItems().add("No users found...");
        });
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }, executor);
  }

  /**
   * Adds a new friend to the client's friend list.
   * @param friendToAdd The username of the friend to add.
   */
  public void addFriend(String friendToAdd) {
    CompletableFuture<AddFriendStatus> future = CompletableFuture.supplyAsync(() -> {
      try {
        return service.addFriend(Session.getInstance().getUsername(), friendToAdd);
      } catch (RemoteException e) {
        return AddFriendStatus.FAILED_UNKNOWN;
      }
    }, executor);

    future.thenAcceptAsync((result) -> {

      Platform.runLater(() -> {
        switch (result) {
          case SUCCESS_ADDED_FRIEND:
            AlertHelper.createAlert(Alert.AlertType.INFORMATION, "Adding friend", "Successfully added friend.", true);
            break;
          case FAILED_ALREADY_FRIENDS:
            AlertHelper.createAlert(Alert.AlertType.INFORMATION, "Adding friend", "You are already friends with your target.", true);
            break;
          case FAILED_ADDING_SELF:
            AlertHelper.createAlert(Alert.AlertType.ERROR, "Adding friend", "Unable to add yourself as friend.", true);
            break;
          case FAILED_UNKNOWN:
            AlertHelper.createAlert(Alert.AlertType.ERROR, "Adding friend", "Unable to add friend, reason unknown.", true);
            break;
        }
      });

    });
  }

  /**
   * Updates the client's online friendlist.
   * @param friendList The ListView to fill the results with.
   */
  public void updateOnlineFriends(ListView friendList) {

    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      do {

        try {
          List<String> onlineFriends = service.getOnlineFriends(Session.getInstance().getUsername());

          Platform.runLater(() -> {
            friendList.getItems().clear();

            if (onlineFriends == null || onlineFriends.size() == 0) {
              friendList.getItems().add("No online friends...");
              return;
            }

            for (String friend : onlineFriends)
            {
              String name = friend.substring(0, 1).toUpperCase() + friend.substring(1);
              friendList.getItems().add(name);
            }

            EventSystem.getInstance().notifyEvent(Event.ON_ONLINE_FRIENDLIST_UPDATED, onlineFriends);
          });

          Thread.sleep(5000);
        } catch (Exception e) { e.printStackTrace(); }

      } while (true);

    }, executor);
  }

  /**
   * Removes a friend of the client from the ListView and from his/her friendlist.
   * @param friendList The ListView to remove the friend entry from.
   * @param friendToRemove The username of the friend to remove.
   */
  public void removeFriend(ListView friendList, String friendToRemove) {

    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        service.removeFriend(Session.getInstance().getUsername(), friendToRemove);

        Platform.runLater(() -> {
          if (friendList.getItems().contains(friendToRemove))
             friendList.getItems().remove(friendToRemove);

          EventSystem.getInstance().notifyEvent(Event.ON_REMOVED_FRIEND, friendToRemove);
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, executor);

  }
}
