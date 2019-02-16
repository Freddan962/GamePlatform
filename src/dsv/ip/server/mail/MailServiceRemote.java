package dsv.ip.server.mail;

import dsv.ip.shared.status.EmailStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Represents the interface used for Mail related RMI communication.
 *
 * @author Fredrik Sander
 */
public interface MailServiceRemote extends Remote {
  List<Mail> getMails(String username, String mail, String password) throws RemoteException;
  EmailStatus sendMail(String username, String subject, String content) throws RemoteException;
  void saveEmailAddress(String username, String email) throws RemoteException;
  void hideMail(String username, int uid) throws RemoteException;
}
