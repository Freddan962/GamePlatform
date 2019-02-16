package dsv.ip.server.user;

import dsv.ip.shared.status.AddFriendStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Represents the interface used for User related RMI communication.
 *
 * @author Fredrik Sander
 */
public interface UserServiceRemote extends Remote {
  AddFriendStatus addFriend(String username, String friendToAdd) throws RemoteException;
  void removeFriend(String username, String friendToRemove) throws RemoteException;
  List<String> getOnlineFriends(String username) throws RemoteException;
  List<String> getUsernames(String like) throws RemoteException;
}
