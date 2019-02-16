package dsv.ip.client.helpers;

import javafx.scene.control.Alert;

/**
 * @autor Fredrik Sander
 *
 * Helps with the construction of alerts.
 */
public class AlertHelper {

  /**
   * Creates a new alert with the provided arguments.
   * @param type The type of the alert.
   * @param title The title text of the alert.
   * @param header The header text of the alert.
   * @param show If true shows the alert immediately, if false does not.
   * @return The alert that was created.
   */
  public static Alert createAlert(Alert.AlertType type, String title, String header, boolean show) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);

    if (show)
      alert.show();

    return alert;
  }
}
