package dsv.ip.server.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * MessageStore is responsible for storing activity feed messages.
 */
public class MessageStore {

  private final int MAX_STORE_SIZE = 25;
  private List<Message> messages = new ArrayList<Message>();

  /**
   * Adds a new message to the store.
   * If the store is full, the store gets rid of the oldest message.
   * @param message The message to add.
   */
  public void addMessage(Message message) {
    if (messages.size() >= MAX_STORE_SIZE)
      messages.remove(0);

    messages.add(message);
  }

  /**
   * Fetchhes all the messages since a specific date from the MessageStore.
   * @param since The date to fetch the messages from.
   * @return A list containing all the messages since the provided date.
   */
  public List<Message> getMessages(Date since) {
    List<Message> found = new ArrayList<Message>();

    messages.forEach((message) -> {
      if (message.getSentAt().after(since))
        found.add(message);
    });

    return found;
  }

  /**
   * Fetchhes all the messages.
   * @return A list of all the messages in the store.
   */
  public List<Message> getMessages() { return messages; }
}
