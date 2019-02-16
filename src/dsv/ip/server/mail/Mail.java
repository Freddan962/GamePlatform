package dsv.ip.server.mail;

import java.io.Serializable;

/**
 * @autor Fredrik Sander
 *
 * Represents mails, e.g. a google mail.
 */
public class Mail implements Serializable {

  // The unique ID of the mail.
  private int uid;
  private String subject;
  private String content;

  /**
   * Constructs a new Mail instance by setting it's information.
   * @param uid The UID of the mail.
   * @param subject The subject of the mail.
   * @param content The content of the mail.
   */
  public Mail(int uid, String subject, String content) {
    this.uid = uid;
    this.subject = subject;
    this.content = content;
  }

  /**
   * Fetches the UID of this email.
   * @return The UID of this email.
   */
  public int getUid() { return uid; }

  /**
   * Fetches the subject of this email.
   * @return The subject of this email.
   */
  public String getSubject() { return subject; }

  /**
   * Fetches the content of this email.
   * @return The content of this email.
   */
  public String getContent() { return content; }

  /**
   * Sets the UID of this email.
   * @param uid The UID to be set.
   */
  public void setUid(int uid) { this.uid = uid; }

  /**
   * Sets the subject of this email.
   * @param subject The subject to be set.
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Sets the content of this email.
   * @param content The content to be set.
   */
  public void setContent(String content) {
    this.content = content; }

}
