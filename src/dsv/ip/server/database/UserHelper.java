package dsv.ip.server.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fredrik Sander
 *
 * Responsible for helping with user related tasks.
 */
public class UserHelper {

  /**
   * Fetches a user ID provided a username
   * @param username The username to fetch the user ID for from the database.
   * @return The user ID or -1 if not found.
   */
  public static int getUserIdByName(String username) {
    ResultSet result = DatabaseConnector.getInstance().query("SELECT id FROM user WHERE username = '" + username + "'");

    try {
      result.next();
      return result.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }

    return -1;
  }

}

