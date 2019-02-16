package dsv.ip.client.helpers;

/**
 * @author Fredrik Sander
 *
 * Responsible for storing information regarding the client side state of a challenge.
 */
public class GameChallengeState {
  private boolean promptedChallenge = false;
  private boolean startedPong = false;

  /**
   * Checks whether or not the client has been prompted of the challenge.
   * @return True if the client has been prompted, false if not.
   */
  public boolean hasPromptedChallenge() {
    return promptedChallenge;
  }

  /**
   * Sets whether or not the client has been prompted of the callenge.
   * @param state True or false.
   */
  public void setHasPromptedChallenge(boolean state) {
    promptedChallenge = state;
  };

  /**
   * Checks whether or not the client has started it's pong game.
   * @return True if the pong game has been started false if not.
   */
  public boolean hasStartedPong() {
    return startedPong;
  }

  /**
   * Sets whether or not the client has started pong.
   * @param state True if the client has started pong false if not.
   */
  public void setHasStartedPong(boolean state) {
    startedPong = state;
  }
}
