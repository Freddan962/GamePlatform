package dsv.ip.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * Responsible for handling hidden mails for a user.
 */
public class HiddenMailStore {

  private List<Integer> hiddenMails = new ArrayList<>();
  private User owner;

  /**
   * Constructs a new HiddenMailStore and initializes it's owner.
   * @param owner The owner of the Hidden Mail store.
   */
  public HiddenMailStore(User owner) {
    this.owner = owner;
  }

  /**
   * Adds a new mail UID to be hidden from the user.
   * @param UID The UID of the mail to be hidden.
   */
  public void addHiddenMail(int UID) {
    if (isHidden(UID))
      return;

    hiddenMails.add(UID);
  }

  /**
   * Fetches the hidden mails.
   * @return The hidden mails.
   */
  public List<Integer> getHiddenMails() {
    return hiddenMails;
  }

  /**
   * Checks if a mail provided it's UID is hidden or not.
   * @param UID The UID of the mail.
   * @return True if the mail is hidden false if not.
   */
  public boolean isHidden(int UID) {
    return hiddenMails.contains(UID);
  }

  /**
   * Fetches the owner of the hidden mail store.
   * @return The owner of the hidden mail store.
   */
  public User getOwner() {
    return owner;
  }
}
