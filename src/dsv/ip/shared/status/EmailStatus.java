package dsv.ip.shared.status;

/**
 * @author Fredrik Sander
 *
 * Email statuses for sending emails.
 */
public enum EmailStatus {
  SENT,
  FAILED_UNKNOWN,
  FAILED_UNKNOWN_RECIPIENT,
  FAILED_NO_CONNECTED_EMAIL
}
