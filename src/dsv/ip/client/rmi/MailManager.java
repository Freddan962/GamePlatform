package dsv.ip.client.rmi;

import dsv.ip.client.Main;
import dsv.ip.client.Session;
import dsv.ip.server.mail.Mail;
import dsv.ip.server.mail.MailServiceRemote;
import dsv.ip.shared.status.EmailStatus;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventSystem;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Fredrik Sander
 *
 * Responsible for providing functionality that works together with the
 * server's mail related functionality.
 */
public class MailManager {

  private MailServiceRemote service;
  private Executor executor = Executors.newCachedThreadPool();

  private int updateIncomingInterval = 25 * 1000;
  private boolean updatingIncoming = false;
  private List<Mail> listMapping;

  private ListView incoming;

  private static MailManager instance = new MailManager();

  /**
   * Prepares a new MailManager by initiating connection.
   */
  private MailManager() {
    initiateConnection();
  }

  /**
   * Fetches the static MailManager instance.
   * @return Returns the MailManager singleton.
   */
  public static MailManager getInstance() {
    return instance;
  }

  /**
   * Initiates the RMI connection.
   */
  private void initiateConnection() {
    try {
      service = (MailServiceRemote) Naming.lookup("rmi://" + Main.SERVER_HOST + ":1100/mailservice");
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Updates the incoming email list and dispatches a EventSystem event to be handled.
   * @param incoming The ListView to fill with incoming emails.
   * @param emailField The TextField containing the client's email username/address.
   * @param passwordField The TextField containing the client's email password.
   */
  public void updateIncoming(ListView incoming, TextField emailField, TextField passwordField) {
    if (updatingIncoming)
      return;

    updatingIncoming = true;
    this.incoming = incoming;

    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      do  {
        try {
          List<Mail> mails = service.getMails(Session.getInstance().getUsername(), emailField.getText(), passwordField.getText());
          listMapping = mails;
          Session.getInstance().setMails(listMapping);
          Platform.runLater(() -> {
            EventSystem.getInstance().notifyEvent(Event.ON_INCOMING_MESSAGES_UPDATED, listMapping);
          });
        } catch (RemoteException e) { e.printStackTrace(); }

        try {
          Thread.sleep(updateIncomingInterval);
        } catch (InterruptedException e) { e.printStackTrace(); }
      } while (true);

    }, executor);
  }

  /**
   * Saves the email address for a specific username.
   * @param username The username to save the email for.
   * @param email The actual email address.
   */
  public void saveEmail(String username, String email) {
    try {
      service.saveEmailAddress(username, email);
    } catch (RemoteException e) { e.printStackTrace(); }
  }

  /**
   * Sends a email from the client to a recipient identified by username.
   * @param recipientUsername The username of the recipient.
   * @param content The content of the email.
   * @return The status of the sending email operation.
   */
  public EmailStatus sendEmail(String recipientUsername, String content) {
    try {
      String subject = "Message from " + Session.getInstance().getUsername();
      return service.sendMail(recipientUsername, subject, content);
    } catch (RemoteException e) { e.printStackTrace(); }

    return EmailStatus.FAILED_UNKNOWN;
  }

  /**
   * Fetches a specific mail based on a index.
   * E.g. during selection in ListView.
   * @param selectedIndex The index of the email to be selected.
   * @return The email with the index if found else null.
   */
  public Mail getMail(int selectedIndex) {
    if (incoming.getItems().size() <= selectedIndex)
      return null;

    return listMapping.get(selectedIndex);
  }

  /**
   * Hides a mail identified by it's UID.
   * @param mailUID The UID of the mail to hide.
   */
  public void hideMail(int mailUID) {
    if (mailUID < 0) return;

    try {
      service.hideMail(Session.getInstance().getUsername(), mailUID);
    } catch (RemoteException e) { e.printStackTrace(); }
  }
}
