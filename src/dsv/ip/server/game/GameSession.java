package dsv.ip.server.game;

import java.io.Serializable;

/**
 * Represents a game instance
 */
public class GameSession implements Serializable {

  private static int count = 0;
  private int ID;
  private String challengerUsername;
  private String challengedUsername;

  private boolean challengedReady = false;

  /**
   * Constructs a new GameSession and sets the participants.
   * @param challengerUsername The challenger user.
   * @param challengedUsername The challenged user.
   */
  public GameSession(String challengerUsername, String challengedUsername) {
    count++;
    ID = count;

    this.challengerUsername = challengerUsername;
    this.challengedUsername = challengedUsername;
  }

  /**
   * Fetches the challenger from the Game Session.
   * @return The challenger user.
   */
  public String getChallenger() {
    return challengerUsername;
  }

  /**
   * Fetches the challenged user from the Game Session.
   * @return The challenged user.
   */
  public String getChallenged() {
    return challengedUsername;
  }

  /**
   * Ready up the challenged user.
   */
  public void challengedReady() {
    challengedReady = true;
  }

  /**
   * Checks if the challenged is ready.
   * @return True if the challenger user is ready false if not.
   */
  public boolean isChallengedReady() {
    return challengedReady;
  }

  /**
   * Fetches the GameSessions unique ID.
   * @return The GameSession instance unique ID.
   */
  public int getID() {
    return ID;
  }
}
