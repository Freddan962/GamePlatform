package dsv.ip.client.pong;

import dsv.ip.client.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * @author Fredrik Sander
 *
 * Ball represents a Ball entity in the Pong game.
 */
public class Ball implements GameEntity {

  private Player playerOne;
  private Player playerTwo;

  private Body body;
  private float speedX = 1;
  private float speedY = 2;

  /**
   * Constructs a new Ball instance.
   * Computes the initial position based on the screen size.
   */
  public Ball() {
    this.body = new Body(Main.WIDTH/2 - 10/2, Main.HEIGHT/2 - 10/2, 10, 10);
    reset();
  }

  /**
   * Updates the Ball's game logic.
   */
  @Override
  public void update() {
    body.setY(body.getY() + speedY);
    body.setX(body.getX() + speedX);

    if (body.getY() < 0 || body.getY() > Main.HEIGHT)
      speedY *= -1;
  }

  /**
   * Renders the Ball on a specific GraphicsContext.
   * @param gc The GraphicsContext to render the ball on.
   */
  @Override
  public void render(GraphicsContext gc) {
    gc.fillOval(body.getX(), body.getY(), body.getWidth(), body.getHeight());
  }

  @Override
  public void handleKeyDown(KeyCode key) { }
  @Override
  public void handleKeyUp(KeyCode key) { }

  /**
   * Updates logic based on a player, if the player
   * intersects the ball then reverse travel direction.
   * @param player The player to check intersection for.
   */
  public void checkPlayer(Player player) {
    if (body.intersects(player.getBody()))
      speedX *= -1;
  }

  /**
   * Resets the ball by setting it's position to the center of the screen.
   */
  public void reset() {
    body.setX(Main.WIDTH/2 - body.getWidth()/2);
    body.setY(Main.HEIGHT/2 - body.getHeight()/2);
  }

  /**
   * Fetches the ball's body.
   * @return Returns the body of the ball.
   */
  public Body getBody() { return body; }
}
