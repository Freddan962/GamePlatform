package dsv.ip.server.game;

import dsv.ip.server.Server;
import dsv.ip.server.model.User;
import dsv.ip.server.user.UserManager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Fredrik Sander
 *
 * GameService is responsible for providing Game related services such as challenging other users to a game.
 */
public class GameService extends UnicastRemoteObject implements GameServiceRemote {

  private GameSessionStore store = new GameSessionStore();

  /**
   * Constructs a new GameService entity by preparing for RMI connections.
   * @throws RemoteException
   */
  public GameService() throws RemoteException {
    try {
      Naming.rebind("rmi://0.0.0.0:" + Server.RMI_PORT  + "/gameservice", this);
    } catch (MalformedURLException e) { e.printStackTrace(); }

    System.out.println("GameService started.");
  }

  /**
   * Challenges a user identified by a username to a GameSession.
   * @param username The username of the challenger.
   * @param toChallengeUsername The username of the user to be challenged.
   * @return True if challenge was successful false if not.
   * @throws RemoteException Thrown if server side exception have occurred.
   */
  @Override
  public boolean challengeToGame(String username, String toChallengeUsername) throws RemoteException {
    username = username.toLowerCase();
    toChallengeUsername = toChallengeUsername.toLowerCase();

    User challenger = UserManager.getInstance().getUser(username);
    User challenged = UserManager.getInstance().getUser(toChallengeUsername);

    if (challenger == null || challenged == null)
      return false;

    if (store.hasChallenge(username) || store.hasChallenge(toChallengeUsername))
      return false;

    GameSession session = new GameSession(
      username,
      toChallengeUsername
    );

    store.addChallenge(session);
    return true;
  }

  /**
   * Fetches an active game challenge for a user identifed by a username.
   * @param forUsername The username of the user to fetch challenge for.
   * @return A GameSession instance if found null if not.
   * @throws RemoteException Thrown if server side exception have occurred.
   */
  @Override
  public GameSession getGame(String forUsername) throws RemoteException {
    forUsername = forUsername.toLowerCase();

    User forUser = UserManager.getInstance().getUser(forUsername);
    if (forUser == null)
      return null;

    return store.getSession(forUsername);
  }

  /**
   * Responds to a game callenge for a user identified by a username.
   * @param forUsername The username of the user to accept the challenge for.
   * @param accepted True if the user accepts the challenge false if not.
   * @throws RemoteException Thrown if server side exception have occurred.
   */
  @Override
  public void respondToGameChallenge(String forUsername, boolean accepted) throws RemoteException {
    forUsername = forUsername.toLowerCase();

    User respondingUser = UserManager.getInstance().getUser(forUsername);
    if (respondingUser == null)
      return;

    if (!store.hasChallenge(forUsername))
      return;

    GameSession respondingSession = store.getSession(forUsername);
    if (!respondingSession.getChallenged().equalsIgnoreCase(forUsername))
      return;

    if (!accepted) {
      store.removeChallenge(respondingSession);
      return;
    }

    respondingSession.challengedReady();
  }
}
