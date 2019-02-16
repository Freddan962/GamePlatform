package dsv.ip.client.pong;

import dsv.ip.client.Main;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Fredrik Sander
 *
 * Responsible for controlling the Pong game.
 */
public class PongController implements Initializable
{
  private GraphicsContext gc;

  private ArrayList<GameEntity> gameEntities = new ArrayList<GameEntity>();

  private Player playerOne = new Player(0, 0);
  private Player playerTwo = new Player(Main.WIDTH - 10, 0);
  private Ball ball = new Ball();

  private int playerOneScore = 0;
  private int playerTwoScore = 0;

  @FXML
  Canvas canvas;

  /**
   * Prepares the Pong game by preparing canvas, keybindings and different game entities.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gc = canvas.getGraphicsContext2D();

    new AnimationTimer() {
      public void handle(long currentNanoTime) {
        update();
        render();
      }
    }.start();

    playerOne.setMoveUpKey(KeyCode.W);
    playerOne.setMoveDownKey(KeyCode.S);

    playerTwo.setMoveUpKey(KeyCode.UP);
    playerTwo.setMoveDownKey(KeyCode.DOWN);

    registerEntity(playerOne);
    registerEntity(playerTwo);
    registerEntity(ball);
  }

  /**
   * Called whenever a key is down.
   * @param code The KeyCode of the pressed key.
   */
  public void handleKeyDown(KeyCode code) {
    for (GameEntity entity : gameEntities)
      entity.handleKeyDown(code);
  }

  /**
   * Called whenever a key is released.
   * @param code The KeyCode of the released key.
   */
  public void handleKeyUp(KeyCode code) {
    for (GameEntity entity : gameEntities)
      entity.handleKeyUp(code);
  }

  /**
   * The primary logic process, responsible for handling game logic.
   */
  private void update()
  {
    for (GameEntity entity : gameEntities)
      entity.update();

    ball.checkPlayer(playerOne);
    ball.checkPlayer(playerTwo);

    updateGoal();
  }

  /**
   * The primary rendering process, responsible for handling graphics and rendering.
   */
  private void render() {
    gc.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);

    for (GameEntity entity : gameEntities)
      entity.render(gc);

    gc.fillText(playerOneScore + " - " + playerTwoScore, Main.WIDTH/2, 15);
  }

  /**
   * Register a game entity to the engine.
   * Will make the entity's rendering and update functions be called by the primary processes.
   * @param entity
   */
  private void registerEntity(GameEntity entity) {
    gameEntities.add(entity);
  }

  /**
   * Checks and handles scores during pong gameplay.
   */
  private void updateGoal() {
    if (ball.getBody().getX() < 0) {
      playerTwoScore += 1;
      ball.reset();
    }
    else if (ball.getBody().getX() > Main.WIDTH) {
      playerOneScore += 1;
      ball.reset();
    }
  }
}
