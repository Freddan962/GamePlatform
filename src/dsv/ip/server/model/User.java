package dsv.ip.server.model;

/**
 * @author Fredrik Sander
 *
 * User represents a client user.
 */
public class User {

  private int id;
  private String username;
  private String email;

  private FriendList friendList;
  private HiddenMailStore hiddenMailStore;

  private boolean attemptedToLoadFromDB;

  /**
   * Constructs a new User instance provided a username.
   * @param username The username of the User instance.
   */
  public User(String username) {
    this.username = username;
  }

  /**
   * Checks whether or not a user is ready to receive email.
   * @return True if the user has a email, false if not.
   */
  public boolean readyToReceiveEmail() {
    return !email.isEmpty();
  }

  /**
   * Checks whether or not the user should be saved.
   * @return True if the user should be saved false if not.
   */
  public boolean shouldBeSaved() {
    return attemptedToLoadFromDB;
  }

  /**
   * Sets that the user has had an attempt at being loaded from the database.
   */
  public void attemptedToLoadFromDB() { this.attemptedToLoadFromDB = true; }

  /**
   * Sets the username of the user.
   * @param username The username to set.
   */
  public void setUsername(String username) { this.username = username; }

  /**
   * Sets the email of the user.
   * @param email The email to be set.
   */
  public void setEmail(String email) { this.email = email; }

  /**
   * Sets the ID of the user.
   * @param id The ID to be set for the user.
   */
  public void setId(int id) { this.id = id; }

  /**
   * Sets the friend list of the user.
   * @param friendList The friend list to be set for the user.
   */
  public void setFriendList(FriendList friendList) {
    this.friendList = friendList;
  }

  /**
   * Sets the hidden mail store for the user.
   * @param hiddenMailStore The hidden mail store to be set fo rthe user.
   */
  public void setHiddenMailStore(HiddenMailStore hiddenMailStore) {
    this.hiddenMailStore = hiddenMailStore;
  }

  /**
   * Fetches the username of the user.
   * @return The username of the user.
   */
  public String getUsername() { return username; }

  /**
   * Fetches the email of the user.
   * @return The email of the user.
   */
  public String getEmail() { return email; }

  /**
   * Fetches the id of the user.
   * @return The id of the user.
   */
  public int getId() { return id; }

  /**
   * Fetches the friend list of the user.
   * @return The friend list of the user.
   */
  public FriendList getFriendList() { return friendList; }

  /**
   * Fetches the hidden mail store of the user.
   * @return The hidden mail store of the user.
   */
  public HiddenMailStore getHiddenMailStore() { return hiddenMailStore; }
}
