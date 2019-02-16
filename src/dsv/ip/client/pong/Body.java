package dsv.ip.client.pong;

/**
 * @author Fredrik Sander
 *
 * Body represents a entitity's body in 2D space.
 */
public class Body {
  public float x;
  public float y;
  public float width;
  public float height;

  /**
   * Constructs a new body instance with the specific values.
   * @param x The initial X position.
   * @param y The initial Y position.
   * @param width The initial width.
   * @param height The initial height.
   */
  public Body(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Checks whether or not this body intersects another body.
   * @param other The other body to check again.
   * @return True if they intersect false if not.
   */
  public boolean intersects(Body other) {
    boolean horizontalIntersection = this.getLeft() < other.getRight() && this.getRight() > other.getLeft();
    boolean verticalIntersection = this.getTop() < other.getBottom() && this.getBottom() > other.getTop();
    return horizontalIntersection && verticalIntersection;
  }

  /**
   * Fetches the left coordinate of the body.
   * @return The left coordinate of the body.
   */
  public float getLeft() {
    return x;
  }

  /**
   * Fetches the right coordinate of the body.
   * @return The right coordinate of the body.
   */
  public float getRight() {
    return x + width;
  }

  /**
   * Fetches the top coordinate of the body.
   * @return The top coordinate of the body.
   */
  public float getTop() { return y; }

  /**
   * Fetches the bottom coordinate of the body.
   * @return The bottom coordinate of the body.
   */
  public float getBottom() {
    return y + height;
  }

  /**
   * Sets the X coordinate of the body.
   * @param x The X to be set.
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Sets the Y coordinate of the body.
   * @param y The Y coordinate to be set.
   */
  public void setY(float y) { this.y = y; }

  /**
   * Fetches the X coordinate of the body.
   * @return Returns the X coordinate.
   */
  public float getX() {
    return x;
  }

  /**
   * Fetches the Y coordinate of the body.
   * @return Returns the Y coordinate.
   */
  public float getY() {
    return y;
  }

  /**
   * Fetches the width of the body.
   * @return The width of the body.
   */
  public float getWidth() {
    return width;
  }

  /**
   * Fetches the height of the body.
   * @return The height of the body.
   */
  public float getHeight() {
    return height;
  }
}
