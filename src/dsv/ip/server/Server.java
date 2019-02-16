package dsv.ip.server;

import dsv.ip.server.codec.EncryptionCodec;
import dsv.ip.server.database.DatabaseConnector;
import dsv.ip.server.feed.ActivityFeedService;
import dsv.ip.server.game.GameService;
import dsv.ip.server.mail.MailService;
import dsv.ip.server.user.UserManager;
import dsv.ip.server.user.UserService;
import dsv.ip.shared.EventSystem;
import org.apache.xmlrpc.WebServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * @author Fredrik Sander
 *
 *  Server is responsible for Gamer Platform's server entity.
 */
public class Server {

  // The port that the RMI services will listen on.
  public static int RMI_PORT = 1100;

  /**
   * Starts the server and prepares all required entities.
   */
  public void run() {
    WebServer server = new WebServer(7777);

    // Register handlers
    server.addHandler("connection_handler", new ConnectionHandler());
    server.addHandler("location_handler", new LocationResolver());
    server.start();

    // Load singletons
    EventSystem.getInstance();
    UserManager.getInstance();
    DatabaseConnector.getInstance();
    EncryptionCodec.getInstance();

    try {
      LocateRegistry.createRegistry(RMI_PORT);
      new MailService();
      new UserService();
      new ActivityFeedService();
      new GameService();
    } catch (RemoteException e) { e.printStackTrace(); }

    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter anything to terminate server...");
    scanner.next();
  }
}
