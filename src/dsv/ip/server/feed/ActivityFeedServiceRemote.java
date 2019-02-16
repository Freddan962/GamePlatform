package dsv.ip.server.feed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * Represents the interface used for Activity Feed related RMI communication.
 */
public interface ActivityFeedServiceRemote extends Remote {
  void shareMessage(String fromUsername, String message) throws RemoteException;
  List<Message> getLatestMessages(Date since) throws RemoteException;
}

