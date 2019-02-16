package dsv.ip.server.feed;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fredrik Sander
 *
 * Represents a message in the activity feed.
 */
public class Message implements Serializable {

  private Date sentAt;
  private String message;

  /**
   * Constructs a new message instance with the time sent and it's containing message.
   * @param sentAt The time when the message was sent.
   * @param message The content of the message.
   */
  public Message(Date sentAt, String message) {
    this.sentAt = sentAt;
    this.message = message;
  }

  /**
   * Fetches the message's content.
   * @return The content of the message.
   */
  public String getMessage() { return message; }

  /**
   * Fetches the date when the message was sent.
   * @return The date when the message was sent.
   */
  public Date getSentAt() { return sentAt; }
}
