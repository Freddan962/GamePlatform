package dsv.ip.client.pong;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * @author Fredrik Sander
 *
 * Represents an implementable interface for GameEntities in the pong game.
 */
public interface GameEntity {
  void update();
  void render(GraphicsContext gc);
  void handleKeyDown(KeyCode key);
  void handleKeyUp(KeyCode key);
}
