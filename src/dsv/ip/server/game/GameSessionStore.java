package dsv.ip.server.game;

import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;

import java.util.HashMap;

public class GameSessionStore {
  HashMap<String, GameSession> sessions = new HashMap<>(); //Username, GameSession

  /**
   * Checks whether or not a user has a challenge in store.
   * @param username The username to check for.
   * @return True or false.
   */
  public boolean hasChallenge(String username) {
    return sessions.containsKey(username);
  }

  /**
   * Adds a new GameSession to the store.
   * @param session The GameSession to add.
   */
  public void addChallenge(GameSession session) {
    sessions.put(session.getChallenger(), session);
    sessions.put(session.getChallenged(), session);
  }

  /**
   * Removes a GameSession from the store.
   * @param session The GameSession to remove from the store.
   */
  public void removeChallenge(GameSession session) {
    if (sessions.containsKey(session.getChallenged()))
      sessions.remove(session.getChallenged());

    if (sessions.containsKey(session.getChallenger()))
      sessions.remove(session.getChallenger());
  }

  /**
   * Fetches a users active session.
   * @param username The username to fetch the session from.
   * @return The session for the user or null if not existing.
   */
  public GameSession getSession(String username) {
    return sessions.get(username);
  }
}
