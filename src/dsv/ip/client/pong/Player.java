package dsv.ip.client.pong;

import dsv.ip.client.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * @author Fredrik Sander
 *
 * Player represents a Player entity in the Pong game.
 */
public class Player implements GameEntity {

  private Body body;
  private float speed = 5;

  private KeyCode moveDownKey;
  private KeyCode moveUpKey;

  private boolean movingDown = false;
  private boolean movingUp = false;

  /**
   * Constructs a new Player instance.
   * @param x The initial X position.
   * @param y The initial Y position.
   */
  Player(float x, float y) {
    this.body = new Body(x, y, 10, 75);
  }

  /**
   * Updates the Player's game logic.
   */
  @Override
  public void update() {
    if (movingUp)
    {
      body.setY(body.getY() - speed);

      if (body.getY() < 0)
        body.setY(0);
    }

    if (movingDown) {
      body.setY(body.getY() + speed);

      if (body.getY() > Main.HEIGHT - body.getHeight())
        body.setY(Main.HEIGHT - body.getHeight());
    }
  }

  /**
   * Renders the player on a specific GraphicsContext.
   * @param gc The specific GraphicsContext to render the player on.
   */
  @Override
  public void render(GraphicsContext gc) {
    gc.fillRect(body.getX(), body.getY(), body.getWidth(), body.getHeight());
  }

  /**
   * Updates the player's logic based on key down values.
   * @param key The key that was pressed down.
   */
  @Override
  public void handleKeyDown(KeyCode key) {
    if (key == moveDownKey)
      movingDown = true;
    else if (key == moveUpKey)
      movingUp = true;
  }

  /**
   * Updates the player's logic based on key up values.
   * @param key The key that was released.
   */
  @Override
  public void handleKeyUp(KeyCode key) {
    if (key == moveDownKey)
      movingDown = false;
    else if (key == moveUpKey)
      movingUp = false;
  }

  /**
   * Sets the key to be used for the player to move up.
   * @param key The KeyCode to be set.
   */
  public void setMoveUpKey(KeyCode key) {
    moveUpKey = key;
  }

  /**
   * Sets the key to be used for the player to move down.
   * @param key The KeyCode to be set.
   */
  public void setMoveDownKey(KeyCode key) {
    moveDownKey = key;
  }

  /**
   * Fetches the Body of the player.
   * @return Returns the player's body.
   */
  public Body getBody() {
    return body;
  }
}
