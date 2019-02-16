package dsv.ip.shared;

/**
 * @author Fredrik Sander
 *
 * Enum collection of all the events that EventObservers can be notified of.
 */
public enum Event {
  ON_USER_CONNECT,
  ON_INCOMING_MESSAGES_UPDATED,
  ON_ONLINE_FRIENDLIST_UPDATED,
  ON_REMOVED_FRIEND,
  ON_CHALLENGE_TO_GAME_RESPONSE,
  ON_GAME_SESSION_FETCHED,
}
