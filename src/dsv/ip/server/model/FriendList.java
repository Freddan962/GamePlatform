package dsv.ip.server.model;

import dsv.ip.server.database.UserHelper;
import dsv.ip.server.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * Responsible for handling the friends for a user.
 */
public class FriendList {

  List<Integer> friends = new ArrayList<>();
  private User owner;

  /**
   * Constructs a new FriendList and initializes it's owner.
   * @param owner The owner of the FriendList.
   */
  public FriendList(User owner) {
    this.owner = owner;
  }

  /**
   * Adds a friend to the friend list provided a ID.
   * @param id The ID of the friend to be user.
   * @return True if successful false if not.
   */
  public boolean addFriend(int id) {
    if (id == -1 || id == owner.getId())
      return false;

    friends.add(id);
    return true;
  }

  /**
   * Adds a new friend to the friend list provided a username.
   * @param friend The username of the friend to be added to the friendlist.
   * @return True if successful false if not.
   */
  public boolean addFriend(String friend) {
    int id = UserHelper.getUserIdByName(friend);
    if (id == -1 || id == owner.getId())
      return false;

    friends.add(id);
    return true;
  }

  /**
   * Checks if friends with another user provided a username.
   * @param friend The username of the friend to be checked with.
   * @return True if friends false if not.
   */
  public boolean isFriendsWith(String friend) {
    int id = UserHelper.getUserIdByName(friend);
    if (id == -1) return false;
    return friends.contains(id);
  }

  /**
   * Fetches all the friends.
   * @return A List with all the friends IDs.
   */
  public List<Integer> getFriendsIds() {
    return friends;
  }

  /**
   * Fetches all the friends currently online on the server.
   * @return A list of all the online friends.
   */
  public List<User> getOnlineFriends() {
    List<User> list = new ArrayList<User>();
    for (int id : getFriendsIds())
    {
      User user = UserManager.getInstance().getUser(id);
      if (user != null)
        list.add(user);
    }

    return list;
  }

  /**
   * Fetches the usernames of all the online friends.
   * @return A list of all online friend's usernames.
   */
  public List<String> getOnlineFriendNames() {
    List<User> users = getOnlineFriends();
    List<String> names = new ArrayList<>();
    users.forEach(user -> { names.add(user.getUsername()); });
    return names;
  }

  /**
   * Removes a friend from the friend list provided a username.
   * @param friendToRemove The username of the friend to remove.
   */
  public void removeFriend(String friendToRemove) {
    int id = UserHelper.getUserIdByName(friendToRemove);
    if (id == -1)
      return;

    if (friends.contains(id))
      friends.remove(friends.indexOf(id));
  }

  /**
   * Fetches the owner of the friend list instance.
   * @return The owner of the friend list instance.
   */
  public User getOwner() {
    return owner;
  }
}
