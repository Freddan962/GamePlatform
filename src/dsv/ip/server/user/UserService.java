package dsv.ip.server.user;

import dsv.ip.server.Server;
import dsv.ip.server.database.DatabaseConnector;
import dsv.ip.server.model.User;
import dsv.ip.shared.status.AddFriendStatus;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * UserService is responsible for providing user related services such as
 * related to friends and users.
 */
public class UserService extends UnicastRemoteObject implements UserServiceRemote {

  /**
   * Constructs a new UserService entity by preparing for RMI connections.
   * @throws RemoteException
   */
  public UserService() throws RemoteException {
    try {
      Naming.rebind("rmi://0.0.0.0:" + Server.RMI_PORT + "/userservice", this);
    } catch (MalformedURLException e) { e.printStackTrace(); }

    System.out.println("UserService started.");
  }

  /**
   * Adds a one user as another users friend.
   * @param username The username to add the friend for.
   * @param friendToAdd The username of the friend to add.
   * @return The status of the add friend operation.
   * @throws RemoteException Thrown if server side exceptions have occurred.
   */
  @Override
  public AddFriendStatus addFriend(String username, String friendToAdd) throws RemoteException {
    if (username.equalsIgnoreCase(friendToAdd))
      return AddFriendStatus.FAILED_ADDING_SELF;

    User user = UserManager.getInstance().getUser(username);
    if (user == null)
      return AddFriendStatus.FAILED_UNKNOWN;

    if (user.getFriendList().isFriendsWith(friendToAdd))
    return AddFriendStatus.FAILED_ALREADY_FRIENDS;

    user.getFriendList().addFriend(friendToAdd);
    return AddFriendStatus.SUCCESS_ADDED_FRIEND;
  }

  /**
   * Removes a user from another users friendlist.
   * @param username The username of the friendlist owner.
   * @param friendToRemove The username of the friend to remove from the friendlist.
   * @throws RemoteException Thrown if anything has gone wrong on the server.
   */
  @Override
  public void removeFriend(String username, String friendToRemove) throws RemoteException {
    if (friendToRemove.isEmpty())
      return;

    User user = UserManager.getInstance().getUser(username);
    if (user == null)
      return;

    user.getFriendList().removeFriend(friendToRemove);
  }

  /**
   * Fetches a list with the usernames of all the users online friends.
   * @param username The username of the user to fetch all the friends for.
   * @return A lis containg the usernames of all the friends.
   * @throws RemoteException Thrown if anything has gone wrong on the server.
   */
  @Override
  public List<String> getOnlineFriends(String username) throws RemoteException {
    User user = UserManager.getInstance().getUser(username);
    if (user == null)
      return null;

    return user.getFriendList().getOnlineFriendNames();
  }

  /**
   * Fetches all the existing usernames provided a filter with a maximum of 15 users.
   * @param filter The filter that is used to filter the global list of usernames.
   * @return A list of the usernames found matching the provided filter.
   * @throws RemoteException Thrown if anything has gone wrong on the server.
   */
  @Override
  public List<String> getUsernames(String filter) throws RemoteException {
    ResultSet result = DatabaseConnector.getInstance().query("SELECT username FROM user WHERE username LIKE '%" + filter + "%' ORDER BY username  LIMIT 15;");
    try {
      List<String> names = new ArrayList<>();

      while (result.next())
        names.add(result.getString(1));

      return names;
    } catch (SQLException e) { e.printStackTrace(); }

    return null;
  }
}
