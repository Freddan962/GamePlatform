package dsv.ip.server;

public class Main {

  /**
   * @author Fredrik Sander
   *
   * Creates and starts Gamer Platform's server entity.
   */
  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }
}
