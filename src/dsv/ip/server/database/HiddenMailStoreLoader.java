package dsv.ip.server.database;

import dsv.ip.server.model.HiddenMailStore;
import dsv.ip.server.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fredrik Sander
 *
 * Responsible for loading HiddenMailStores from the database.
 */
public class HiddenMailStoreLoader {

  private User toLoadFor;

  /**
   * Constructs a new HiddenMailStoreLoader instance for a specific user.
   * @param user The user that the HiddenMailStore will in the future be loaded for.
   */
  public HiddenMailStoreLoader(User user) { this.toLoadFor = user; }

  /**
   * Loads a HiddenMailStore instance from the database for the previously set user.
   * @return The loaded HiddenMailStore.
   * @throws SQLException Thrown if a SQLException occurs.
   */
  public HiddenMailStore load() throws SQLException {
    String query = "SELECT mail_uid FROM hidden_mails WHERE user_id=" + toLoadFor.getId();
    ResultSet resultSet = DatabaseConnector.getInstance().query(query);

    HiddenMailStore store = new HiddenMailStore(toLoadFor);
    while (resultSet.next()) {
      store.addHiddenMail(resultSet.getInt(1));
    };

    return store;
  }
}
