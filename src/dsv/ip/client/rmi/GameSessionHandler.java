package dsv.ip.client.rmi;

import dsv.ip.client.Main;
import dsv.ip.client.Session;
import dsv.ip.server.game.GameServiceRemote;
import dsv.ip.server.game.GameSession;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventSystem;
import javafx.application.Platform;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fredrik Sander
 *
 * Responsible for providing fucntionality that works together with the
 * server's game session/challenge system functionality.
 */
public class GameSessionHandler {

  private GameServiceRemote service;
  private ExecutorService executor = Executors.newCachedThreadPool();

  private static GameSessionHandler instance = new GameSessionHandler();

  /**
   * Prepares a new GameSessionHandler by initiating connection.
   */
  private GameSessionHandler() {
    initiateConnection();
  }

  /**
   * Fetches the GameSessionHandler instance.
   * @return Returns the GameSessionHandler singleton.
   */
  public static GameSessionHandler getInstance() {
    return instance;
  }

  /**
   * Initiates the RMI connection.
   */
  private void initiateConnection() {
    try {
      service = (GameServiceRemote) Naming.lookup("rmi://" + Main.SERVER_HOST + ":1100/gameservice");
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Challenges a user identified by a username to a game challenge.
   * Later notifies the EventSystem of the response from the challenge (not the user).
   * @param toChallengeUsername The username of the user to be challenged.
   */
  public void challengeToGame(String toChallengeUsername) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        boolean status = service.challengeToGame(Session.getInstance().getUsername(), toChallengeUsername);

        Platform.runLater(() -> {
          EventSystem.getInstance().notifyEvent(Event.ON_CHALLENGE_TO_GAME_RESPONSE, status);
        });
      } catch (RemoteException e) { e.printStackTrace(); }
    }, executor);
  }

  /**
   * Fetches the client's active GameSession if one exists.
   * Uses the EventSystem to notify observers of the result from the asynchronous result.
   */
  public void fetchGameSession() {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        GameSession session = service.getGame(Session.getInstance().getUsername());

        Platform.runLater(() -> {
          EventSystem.getInstance().notifyEvent(Event.ON_GAME_SESSION_FETCHED, session);
        });
      } catch (RemoteException e) { e.printStackTrace(); }
    }, executor);
  }

  /**
   * The client sends a response for it's GameSession if one exists.
   * @param accepted True if the client wishes to accept challenge false if not.
   */
  public void respondToGameSession(boolean accepted) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        service.respondToGameChallenge(Session.getInstance().getUsername(), accepted);;
      } catch (RemoteException e) { e.printStackTrace(); }
    });
  }
}
