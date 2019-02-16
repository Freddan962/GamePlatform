package dsv.ip.client.handlers;

import dsv.ip.client.rmi.UserManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * @author Fredrik Sander
 *
 * FriendHandler is responsible for providing extra functionality for management of friends.
 */
public class FriendHandler {

  /**
   * Prompts the client to confirm the removal of a friend.
   * Removes the friend if confirmed.
   * @param friendList The friend list UI ListView instance.
   * @param friend The username of the friend to remove.
   */
  public void promptRemoveFriend(ListView friendList, String friend) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.CANCEL);
    alert.setHeaderText("Removing " + friend + " as a friend.");
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES)
     UserManager.getInstance().removeFriend(friendList, friend);
  }
}
