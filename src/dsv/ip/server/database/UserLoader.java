package dsv.ip.server.database;

import dsv.ip.server.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fredrik Sander
 *
 * Responsible for loading of a user provided a username.
 */
public class UserLoader {
  private String username;

  /**
   * Constructs a new UserLoader instance and prepare the username of the user to be loaded from the database.
   * @param username The username of the user to be loaded from the database.
   */
  public UserLoader(String username) {
    this.username = username;
  }

  /**
   * Loads a user by username from the database.
   * @return The loaded user.
   */
  public User load() {
    try {
      return attemptToLoadExistingUser();
    } catch (SQLException e) {
      return prepareUserWithID();
    }
  }

  /**
   * Attempts to load a existing user.
   * @return The user that was loaded.
   * @throws SQLException Thrown if a SQL exception occurs (e.g. if the user does not exist!)
   */
  private User attemptToLoadExistingUser() throws SQLException {
    ResultSet resultSet = DatabaseConnector.getInstance().query("SELECT id, email FROM user WHERE username = '" + username + "'" );

    resultSet.next();
    int id = resultSet.getInt(1);
    String email = resultSet.getString(2);
    User user = new User(username);
    user.setId(id);
    user.setEmail(email);
    user.attemptedToLoadFromDB();

    FriendListLoader friendListLoader = new FriendListLoader(user);
    user.setFriendList(friendListLoader.load());

    HiddenMailStoreLoader hiddenMailStoreLoader = new HiddenMailStoreLoader(user);
    user.setHiddenMailStore(hiddenMailStoreLoader.load());

    return user;
  }

  /**
   * Prepares a new user with a ID picked by the database service.
   * @return The new user instance.
   */
  private User prepareUserWithID() {
    User user = new User(username);
    UserSaver saver = new UserSaver();
    saver.save(user);
    User loaded = new UserLoader(username).load();
    return loaded;
  }
}
