package dsv.ip.server.database;

import dsv.ip.server.model.User;

/**
 * @author Fredrik Sander
 *
 * Responsible for saving user instances to the database.
 */
public class UserSaver {

  /**
   * Saves a provided user to the database.
   * @param user The user to be saved.
   */
  public void save(User user) {
    String query = String.format("INSERT INTO user (username, email) VALUES ('%s', '%s') " +
                    "ON DUPLICATE KEY UPDATE username='%s', email='%s'",
            user.getUsername(), user.getEmail(), user.getUsername(), user.getEmail());

    DatabaseConnector.getInstance().execute(query);

    if (user.getFriendList() != null) {
      FriendListSaver friendListSaver = new FriendListSaver();
      friendListSaver.save(user.getFriendList());
    }

    if (user.getHiddenMailStore() != null) {
      HiddenMailStoreSaver hiddenMailStoreSaver = new HiddenMailStoreSaver();
      hiddenMailStoreSaver.save(user.getHiddenMailStore());
    }
  }
}
