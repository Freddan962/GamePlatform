package dsv.ip.server.mail;

import dsv.ip.server.Server;
import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;
import dsv.ip.shared.status.EmailStatus;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * MailService is responsible for providing mail related services such as sending and receiving mails.
 */
public class MailService extends UnicastRemoteObject implements MailServiceRemote {

  private MailSender mailSender;
  private HashMap<String, MailFetcher> mailToFetcher = new HashMap<>();

  /**
   * Constructs a new MailService entity by preparing for RMI connections.
   * @throws RemoteException
   */
  public MailService() throws RemoteException {
    mailSender = new MailSender();

    try {
      Naming.rebind("rmi://0.0.0.0:" + Server.RMI_PORT + "/mailservice", this);
    } catch (MalformedURLException e) { e.printStackTrace(); }

    System.out.println("MailService started.");
  }

  /**
   * Fetches any mails for a specific user identified by username, google mail account and belonging password.
   * @param username The username of the user to fetch the emails for.
   * @param mail The mail used to authenticate to Google mail services.
   * @param password The password of the Google mail service account.
   * @return A list of all the mails that could be retrieved.
   * @throws RemoteException Thrown if server side exceptions have occurred.
   */
  @Override
  public List<Mail> getMails(String username, String mail, String password) throws RemoteException {
    if (mailToFetcher.containsKey(mail) && !mailToFetcher.get(mail).isConnected())
      mailToFetcher.remove(mail);

    if (!mailToFetcher.containsKey(mail))
      mailToFetcher.put(mail, new MailFetcher(username, mail, password));

    return mailToFetcher.get(mail).fetchMails();
  }

  /**
   * Attempts to send a email from a user identified by it's username.
   * @param username The username of the user that sends the email.
   * @param subject The subject of the email.
   * @param content The content of the email.
   * @return The status of the mail sending operation.
   * @throws RemoteException Thrown if server side exceptions have occurred.
   */
  @Override
  public EmailStatus sendMail(String username, String subject, String content) throws RemoteException {
    User user = UserManager.getInstance().getUser(username.toLowerCase());
    if (user == null)
      return EmailStatus.FAILED_UNKNOWN_RECIPIENT;

    if (!user.readyToReceiveEmail())
      return EmailStatus.FAILED_NO_CONNECTED_EMAIL;

    mailSender.sendMail(username.toLowerCase(), subject, content);
    return EmailStatus.SENT;
  }

  /**
   * Saves the email for a specific user identified by username.
   * @param username The username of the user to set the email for.
   * @param email The email to set for the user.
   * @throws RemoteException Thrown if server side exceptions have occurred.
   */
  @Override
  public void saveEmailAddress(String username, String email) throws RemoteException {
    User user = UserManager.getInstance().getUser(username);

    if (user == null)
      return;

    user.setEmail(email);
  }

  /**
   * Hides a email identified by UID for the user identified by a username.
   * @param username The username of the user.
   * @param uid The UID of the email to be hidden.
   * @throws RemoteException Thrown if server side exceptions have occurred.
   */
  @Override
  public void hideMail(String username, int uid) throws RemoteException {
    User user = UserManager.getInstance().getUser(username);
    if (user == null)
      return;

    user.getHiddenMailStore().addHiddenMail(uid);
  }
}
