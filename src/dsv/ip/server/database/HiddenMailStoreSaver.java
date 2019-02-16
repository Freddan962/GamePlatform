package dsv.ip.server.database;

import dsv.ip.server.model.HiddenMailStore;

import java.util.Iterator;

/**
 * @author Fredrik Sander
 *
 * Responsible for saving HiddenMailStores to the database.
 */
public class HiddenMailStoreSaver {

  /**
   * Saves a HiddenMailStore instance to the database.
   * @param store The HiddenMailStore instance to be saved.
   */
  public void save(HiddenMailStore store) {
    if (store.getHiddenMails().isEmpty())
      return;

    DatabaseConnector.getInstance().execute("DELETE FROM hidden_mails WHERE user_id=" + store.getOwner().getId() + ";");

    String query = "INSERT INTO hidden_mails (user_id, mail_uid) VALUES ";

    Iterator iterator = store.getHiddenMails().iterator();
    while (iterator.hasNext()) {
      int id = (int)iterator.next();
      query += String.format("(%d, %d)", store.getOwner().getId(), id);

      if (iterator.hasNext())
        query += ", ";
    }

    query += ";";
    DatabaseConnector.getInstance().execute(query);
  }
}
