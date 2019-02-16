package dsv.ip.server.user;

import dsv.ip.server.database.UserSaver;
import dsv.ip.server.model.User;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventObserver;
import dsv.ip.shared.EventSystem;

import java.util.HashMap;

/**
 * @author Fredrik Sander
 *
 * Responsible for managing servers on the server side of the platform.
 * Listens to Event.ON_USER_CONNECT.
 */
public class UserManager implements EventObserver {

  private static final UserManager instance = new UserManager();
  private HashMap<String, User> users = new HashMap<>();
  private boolean saveUsers = true;

  /**
   * Contructs a new UserManager instance and register to listen for
   * the event when a user connects to the server.
   */
  private UserManager() {
    EventSystem.getInstance().register(Event.ON_USER_CONNECT, this);
    initiateSaveUsersTask();
  }

  /**
   * Fetches the UserManager singleton.
   * @return The userManager singleton.
   */
  public static UserManager getInstance() {
    return instance;
  }

  /**
   * Called by the EventSystem when a event that the UserManager
   * registers to has occurred.
   * @param event The event that occurred.
   * @param object The optional object arguments passed during the event.
   */
  @Override
  public void notify(Event event, Object object) {
    if (event == Event.ON_USER_CONNECT)
    {
      User user = (User) object;
      users.put(user.getUsername(), user);
    }
  }

  /**
   * Fetches a user instance provided a username.
   * @param username The provided username.
   * @return The fetched user instance or null if not found.
   */
  public User getUser(String username) {
    username = username.toLowerCase();

    if (users.containsKey(username))
      return users.get(username);

    return null;
  }

  /**
   * Fetches a user instance provided a id.
   * @param id The id to fetch the user by.
   * @return The fetched user instance or null if not found.
   */
  public User getUser(int id) {
    for (User user : users.values()) {
      if (user.getId() == id) {
        return user;
      }
    }

    return null;
  }

  /**
   * Initiates a save user task that runs on a thread.
   * Responsible for saving users that should be saved on a set interval.
   */
  public void initiateSaveUsersTask() {

    Thread thread = new Thread(() -> {
      UserSaver saver = new UserSaver();

      while (saveUsers) {
        try {
          Thread.sleep(5000);
          if (users.isEmpty()) continue;

          for (User user : users.values())
          {
            if (user.shouldBeSaved())
              saver.save(user);
          }

        } catch (InterruptedException e) { e.printStackTrace(); }
      }
    });

    thread.setDaemon(true);
    thread.start();
  }
}
