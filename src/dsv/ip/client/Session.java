package dsv.ip.client;

import dsv.ip.server.mail.Mail;

import java.util.List;

/**
 * @autor Fredrik Sander
 *
 * Session is responsible for storing information for the Client's current session.
 */
public class Session {
  private String username;
  private List<Mail> mails;

  public static Session instance = new Session();
  private Session() { }

  /**
   * Fetches the static Session instance.
   * @return Returns the Session singleton.
   */
  public static Session getInstance() { return instance; }

  /**
   * Fetches the session's username.
   * @return The username of the session.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Fetches the mails of the session.
   * @return Returns the mails of the session.
   */
  public List<Mail> getMails() {
    return mails;
  }

  /**
   * Sets the username of the session.
   * @param username The username to set.
   */
  public void setUsername(String username) {
    this.username = username.toLowerCase();
  }

  /**
   * Sets the mails of the session.
   * @param mails The mails to be set.
   */
  public void setMails(List<Mail> mails) {
    this.mails = mails;
  }
}
