package dsv.ip.shared;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fredrik Sander
 *
 * TimeHelper is responsible for providing time utility operations.
 */
public class TimeHelper {

  /**
   * Gets the current time and formats it according to a specific pattern.
   * @return The formatted time string.
   */
  public static String getTimeStringNow() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  }
}
