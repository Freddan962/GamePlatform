package dsv.ip.server.mail;

import dsv.ip.server.codec.DecryptionHandler;
import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Fredrik Sander
 *
 * MailFetcher is responsible for providing functionality to fetch emails from gmail using imap.
 */
public class MailFetcher {

  private Session session;
  private Store mailStore;
  private DecryptionHandler decryptor = new DecryptionHandler();
  private User owner;
  private String username;

  /**
   * Constructs a new MailFetcher instance for a user with provided credentials.
   * Initiates a connection to the mail store using IMAP.
   * @param username The username of the user to construct the MailFetcher for.
   * @param emailUsername The email username for google's mail services.
   * @param password The email password for google's mail services.
   */
  public MailFetcher(String username, String emailUsername, String password) {
    this.username = username;
    owner = UserManager.getInstance().getUser(username);

    Properties props = new Properties();
    props.setProperty("mail.imap.ssl.enable", "true");

    session = Session.getInstance(props);
    try {
      mailStore = session.getStore("imap");
      mailStore.connect("imap.gmail.com", 993, emailUsername, password);
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Fetches all the emails found in the mail store with matching GamerPlatform subject title.
   * Ignores previously hidden emails.
   * @return A list of all the mails with decrypted content that was successfully fetched.
   */
  public List<Mail> fetchMails() {
    List<Mail> messages = new ArrayList<>();

     if (mailStore == null)
      return messages;

    try {
      Folder emailFolder = mailStore.getFolder("INBOX");
      UIDFolder uidEmailFolder = (UIDFolder) emailFolder;
      emailFolder.open(Folder.READ_ONLY);

      Message[] readMessages = emailFolder.getMessages();

      // Order mails so that most recent received shows first
      Arrays.sort(readMessages, (messageOne, messageTwo) -> {
        try {
          return messageTwo.getSentDate().compareTo(messageOne.getSentDate());
        } catch (MessagingException e) { return 0; }
      });

      for (int i = 0; i < readMessages.length; i++) {
        String subject = readMessages[i].getSubject();
        if (!subject.contains("GamerPlatform"))
          continue;

        while (owner == null) {
          owner = UserManager.getInstance().getUser(username);
          Thread.sleep(1000);
        }

        int uid = (int)uidEmailFolder.getUID(readMessages[i]);
        if (owner.getHiddenMailStore().isHidden(uid))
          continue;

        subject = subject.replace("GamerPlatform", "");
        String content = getContent(readMessages[i]);
        messages.add(new Mail(uid, subject, content));
      }
    } catch (Exception e) { e.printStackTrace(); }

    return messages;
  }

  /**
   * Checks if the mail store to Google's mail services is connected.
   * @return True if connected false if not.
   */
  public boolean isConnected() {
    return mailStore.isConnected();
  }

  /**
   * Gets the content from a provided message.
   * Encrypts any found encrypted message.
   * @param message The message to fetch the content from.
   * @return The content as a string.
   */
  private String getContent(Message message) {
    StringBuilder contentBuilder = new StringBuilder();

    try {
      String content;
      if (message.getContent() instanceof java.lang.String) {
        content = (String)message.getContent();
      } else {
        Multipart multiPart = (Multipart)message.getContent();
        BodyPart part = multiPart.getBodyPart(0);
        content = part.getContent().toString();
      }

      int firstSplitIndex = content.indexOf(MailSender.SIGN_MESSAGE_BEGIN) + MailSender.SIGN_MESSAGE_BEGIN.length();
      int secondSplitIndex = content.indexOf(MailSender.SIGN_MESSAGE_END);
      if (firstSplitIndex != -1 && secondSplitIndex != -1)
        content = content.substring(firstSplitIndex, secondSplitIndex);

      content = decryptor.decrypt(content);
      contentBuilder.append(content);
    } catch (Exception e) { e.printStackTrace(); }

    return contentBuilder.toString();
  }
}
