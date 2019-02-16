package dsv.ip.client.handlers;

import dsv.ip.client.helpers.AlertHelper;
import dsv.ip.client.rmi.MailManager;
import dsv.ip.shared.status.EmailStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @autor Fredrik Sander
 *
 * Responsible for handling message on the client side.
 */
public class MessageHandler {

  private String recipient;
  private MailManager mailHandler;
  private Executor executor = Executors.newCachedThreadPool();

  /**
   * Constructs a new Messagehandler and sets the MailManager that will be used for managing mails.
   * @param mailHandler
   */
  public MessageHandler(MailManager mailHandler) {
    this.mailHandler = mailHandler;
  }

  /**
   * Sets the recipient.
   * @param name The username of the recipient.
   */
  public void setRecipient(String name) {
    this.recipient = name;
  }

  /**
   * Creates and diplays the send message UI dialogue.
   */
  public void sendMessageDialogue() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Send a message");
    dialog.setHeaderText("Recipient: " + recipient);
    dialog.setContentText("Message: ");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(message -> handleSendMessage(message));
  }

  /**
   * Asynchronously sends the message to the recipient.
   * Listens on response from the server and informs the client of the operation's result.
   * @param message The message to be sent.
   */
  private void handleSendMessage(String message) {
    CompletableFuture<EmailStatus> future = CompletableFuture.supplyAsync(() -> mailHandler.sendEmail(recipient, message), executor);

    future.thenAcceptAsync((result) -> {
      Platform.runLater(() -> {

        if (result != EmailStatus.SENT) {
          AlertHelper.createAlert(Alert.AlertType.ERROR, "Something went wrong when sending message...", "Error: " + result.toString(), true);
          return;
        }

        AlertHelper.createAlert(Alert.AlertType.INFORMATION, "Message has been successfully sent!", message, true);
      });
    });
  }
}
