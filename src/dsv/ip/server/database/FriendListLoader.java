package dsv.ip.server.database;

import dsv.ip.server.model.FriendList;
import dsv.ip.server.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fredrik Sander
 *
 * Responsible for loading a FriendList instance from the database for a specific user.
 */
public class FriendListLoader {

  private User toLoadFor;

  /**
   * Constructs a new FriendListLoader and sets the user that the
   * FriendList will in the future be loaded from.
   * @param user The user to load the FriendList instance for.
   */
  public FriendListLoader(User user) {
    this.toLoadFor = user;
  }

  /**
   * Loads the previously sets users FriendList from the database.
   * @return The loaded FriendList.
   * @throws SQLException Thrown if a SQLException occurs on the SQL server.
   */
  public FriendList load() throws SQLException {
    String query = "SELECT friend_user_id FROM friends WHERE user_id = " + toLoadFor.getId();
    ResultSet resultSet = DatabaseConnector.getInstance().query(query);

    FriendList friendList = new FriendList(toLoadFor);
    while (resultSet.next()) {
      friendList.addFriend(resultSet.getInt(1));
    };

    return friendList;
  }
}
