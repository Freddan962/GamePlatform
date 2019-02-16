package dsv.ip.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Fredrik Sander
 *
 * Starts a new GamePlatform client application.
 */
public class Main extends Application {

    public static int WIDTH = 720;
    public static int HEIGHT = 480;
    public static String RPCConnectionURL = "http://127.0.0.1:7777";
    public static String SERVER_HOST = "127.0.0.1";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
        primaryStage.setTitle("Game Platform");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.setOnCloseRequest(e -> onClose());
        primaryStage.show();
    }

    /**
     * Called upon application shutdown.
     */
    private void onClose() {
        System.exit(0);
    }

    /**
     * Program's entry point.
     * @param args Argument with index 0 is the remote server IP, if none specified defaults to localhost.
     */
    public static void main(String[] args) {
        if (args.length > 0)
            SERVER_HOST = args[0];

        launch(args);
    }
}
