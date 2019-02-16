package dsv.ip.server.mail;

import dsv.ip.server.codec.EncryptionHandler;
import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author Fredrik Sander
 *
 * MailSender is responsible for providing server-side mail sending functionality.
 */
public class MailSender {

  // Used to sign where the encrypted part of a email message starts.
  public static String SIGN_MESSAGE_BEGIN  = "MSG_SECRET_START";
  // Used to sign where the encrypted part of a email message ends.
  public static String SIGN_MESSAGE_END =  "MSG_SECRET_END";

  private Session session;
  private Transport transport;
  private EncryptionHandler encryptor = new EncryptionHandler();

  /**
   * Constructs a new MailSender instance by preparing SMTP connection
   * using server side pre-picked credentials.
   */
  public MailSender() {
    Properties props = new Properties();
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    session = Session.getInstance(props);
    try {
      transport = session.getTransport("smtp");
      transport.connect("smtp.gmail.com", "frsadev5550@gmail.com", "Lonaren461");
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sends an email with encrypted content to a user identified by a username.
   * @param targetUsername The username of the target user.
   * @param subject The subject of the email.
   * @param content The content of the email.
   */
  public void sendMail(String targetUsername, String subject, String content) {
    Thread thread = new Thread(() -> {

      User target = UserManager.getInstance().getUser(targetUsername);
      if (target == null || target.getEmail().isEmpty())
        return;

      MimeMessage message = new MimeMessage(session);

      try {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(target.getEmail()));
        message.setSubject("GamerPlatform" + subject);
        String actualContent = SIGN_MESSAGE_BEGIN + encryptor.encrypt(content) + SIGN_MESSAGE_END;
        message.setContent(actualContent, "text/plain");
        transport.sendMessage(message, message.getAllRecipients());
      } catch (MessagingException e) {  e.printStackTrace(); }

    });

    thread.setDaemon(true);
    thread.start();
  }
}
