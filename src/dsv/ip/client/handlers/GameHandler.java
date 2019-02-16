package dsv.ip.client.handlers;

import dsv.ip.client.Main;
import dsv.ip.client.Session;
import dsv.ip.client.helpers.AlertHelper;
import dsv.ip.client.helpers.GameChallengeState;
import dsv.ip.client.pong.PongController;
import dsv.ip.client.rmi.GameSessionHandler;
import dsv.ip.server.game.GameSession;
import dsv.ip.shared.Event;
import dsv.ip.shared.EventObserver;
import dsv.ip.shared.EventSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Fredrik Sander
 *
 * Responsible for handling client side functionality related to playing/challenging in game.
 */
public class GameHandler implements EventObserver {

  private GameSession activeSession;
  private GameChallengeState activeState;

  private boolean checkingForChallenge = false;
  private static GameHandler instance = new GameHandler();

  /**
   * Constructs a new GameHandler instance and listens to the EventSystem's required events.
   */
  private GameHandler() {
    EventSystem.getInstance().register(Event.ON_CHALLENGE_TO_GAME_RESPONSE, this);
    EventSystem.getInstance().register(Event.ON_GAME_SESSION_FETCHED, this);
  }

  /**
   * Fetches the GameHandler's static instance.
   * @return Returns the GameHandler singleton.
   */
  public static GameHandler getInstance() { return instance; }


  /**
   * When called challenges the selected username from the provided ListView.
   * @param onlineFriendList The ListView to select the challenged from.
   */
  public static void challenge(ListView onlineFriendList) {
    int selectedIndex = onlineFriendList.getSelectionModel().getSelectedIndex();
    String item = (String)onlineFriendList.getSelectionModel().getSelectedItem();

    if (item == "No online friends...")
      return;

    System.out.println("Challenged: " + selectedIndex + " - " + item);
    GameSessionHandler.getInstance().challengeToGame(item);
  }

  /**
   * Starts checking for challenges asynchronously.
   */
  public void startCheckingForChallenges() {
    if (checkingForChallenge)
      return;

    checkingForChallenge = true;
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      do {
        GameSessionHandler.getInstance().fetchGameSession();

        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) { e.printStackTrace(); }
      } while (checkingForChallenge);
    });
  }

  /**
   * Called when a response to a sent game challenge has been received from the server.
   * @param success Whether or not the challenge was successfully sent to the challenged user.
   */
  public void onChallengeToGameResponse(boolean success) {
    String header = success ? "Friend has been challenged." : "Friend is currently busy. Try again later.";
    AlertHelper.createAlert(Alert.AlertType.INFORMATION, "Game Challenge", header, true);
  }

  /**
   * Called when the client's game session has been fetched.
   * @param session The session or null if none exists.
   */
  public void onGameSessionFetched(GameSession session) {
    if (session == null) return;

    // if a new GameSession
    if (activeSession == null || activeSession.getID() != session.getID()) {
      activeSession = session;
      activeState = new GameChallengeState();
    }

    if (session.getChallenged().equalsIgnoreCase(Session.getInstance().getUsername())) {
      handleHaveBeenChallenged(session);
    } else if (session.getChallenger().equalsIgnoreCase(Session.getInstance().getUsername()))
      handleHaveChallenged(session);
  }

  /**
   * Handles logic for when the client have challenged.
   * @param session The GameSession for the challenge.
   */
  private void handleHaveChallenged(GameSession session) {
    if (session.isChallengedReady()) {
      System.out.println("Challenged person is ready!");
      if (!activeState.hasStartedPong()) {
        activeState.setHasStartedPong(true);
        connectToGame();
      }
    }
  }

  /**
   * Handles logic for when the client have been challenged.
   * @param session The GameSession for the challenge.
   */
  private void handleHaveBeenChallenged(GameSession session) {
    if (!activeState.hasPromptedChallenge())
    {
      activeState.setHasPromptedChallenge(true);
      System.out.println("I have been challenged!");
      String title = "Game Challenge from: " + session.getChallenger();

      Alert alert = AlertHelper.createAlert(Alert.AlertType.CONFIRMATION, title, "Do you confirm the challenge?", false);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK)
      {
        GameSessionHandler.getInstance().respondToGameSession(true);
        if (!activeState.hasStartedPong()) {
          activeState.setHasStartedPong(true);
          connectToGame();
        }
      } else if (result.get() == ButtonType.CANCEL) {
        GameSessionHandler.getInstance().respondToGameSession(false);
      }
    }
  }

  @Override
  public void notify(Event event, Object object) {
    switch (event) {
      case ON_CHALLENGE_TO_GAME_RESPONSE:
        onChallengeToGameResponse((boolean)object);
        break;
      case ON_GAME_SESSION_FETCHED:
        onGameSessionFetched((GameSession)object);
        break;
      default:
        break;
    }
  }

  /**
   * Starts the Pong game instance.
   */
  private void connectToGame() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../pong/Pong.fxml"));
      Parent root = loader.load();

      Stage stage = new Stage();
      stage.setTitle("Pong");
      Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

      PongController pongController = loader.getController();

      scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
        pongController.handleKeyDown(key.getCode());
      });

      scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
        pongController.handleKeyUp(key.getCode());
      });

      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
