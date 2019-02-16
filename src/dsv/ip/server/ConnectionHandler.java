package dsv.ip.server;

import dsv.ip.server.database.UserLoader;
import dsv.ip.server.model.User;
import dsv.ip.shared.status.ConnectionStatus;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventSystem;

/**
 * @author Fredrik Sander
 *
 * ConnectionHandler is responsible handling a requested connection from a new client.
 */
public class ConnectionHandler {

  /**
   * Responsible for handling a requested connection.
   * @param username The username that is requested by the connecting entity.
   * @return The status of the connection.
   */
  public int connect(String username) {
    if (username.isBlank())
      return ConnectionStatus.INVALID_NAME.ordinal();

    username = username.toLowerCase();
    UserLoader loader = new UserLoader(username);
    User connectedUser = loader.load();

    if (connectedUser == null)
      connectedUser = new User(username);

    EventSystem.getInstance().notifyEvent(Event.ON_USER_CONNECT, connectedUser);
    return ConnectionStatus.SUCCESS.ordinal();
  }
}
