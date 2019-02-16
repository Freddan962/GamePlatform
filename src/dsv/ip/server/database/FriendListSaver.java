package dsv.ip.server.database;

import dsv.ip.server.model.FriendList;

import java.util.Iterator;

/**
 * @autor Fredrik Sander
 *
 * Responsible for saving FriendList instances to the database.
 */
public class FriendListSaver {

  /**
   * Saves a FriendList instance to the database.
   * @param friendList The FriendList instance to be saved.
   */
  public void save(FriendList friendList) {
    if (friendList.getFriendsIds().isEmpty())
      return;

    DatabaseConnector.getInstance().execute("DELETE FROM friends WHERE user_id=" + friendList.getOwner().getId() + "; ");

    String query = "INSERT INTO friends (user_id, friend_user_id) VALUES ";

    Iterator iterator = friendList.getFriendsIds().iterator();
    while (iterator.hasNext()) {
      int id = (int)iterator.next();
      query += String.format("(%d, %d)", friendList.getOwner().getId(), id);

      if (iterator.hasNext())
        query += ", ";
    }

    query += ";";
    DatabaseConnector.getInstance().execute(query);
  }

}
