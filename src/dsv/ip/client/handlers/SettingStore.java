package dsv.ip.client.handlers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Fredrik Sander
 *
 * Responsible for handling local settings.
 */
public class SettingStore {

  private JSONObject jsonObject;
  private String settingsFilePath;

  /**
   * Constructs a new SettingStore object.
   * Attempts to load existing settings if found on the local file system.
   */
  public SettingStore() {
    JSONParser parser = new JSONParser();
    settingsFilePath = new File(".").getAbsolutePath() + "settings.json";

    try {
      FileReader reader = new FileReader(settingsFilePath);
      jsonObject = (JSONObject)parser.parse(reader);
    } catch (Exception e) {
      jsonObject = new JSONObject();
    }
  }

  /**
   * Puts a new setting and it's value.
   * @param key The key identifying the setting.
   * @param value The value of the setting.
   */
  public void put(Object key, Object value) {
    jsonObject.put(key, value);
  }

  /**
   * Fetches a setting's value from the setting store.
   * @param key The key identifying the settings.
   * @return The value of the setting.
   */
  public Object get(String key) {
    return jsonObject.get(key);
  }

  /**
   * Saves the setting store to the local file system.
   * @return True if successfully saved false if not.
   */
  public boolean save() {
    try (FileWriter writer = new FileWriter(settingsFilePath)) {
      writer.write(jsonObject.toJSONString());
      writer.flush();
    } catch (IOException e) {
      return false;
    }

    return true;
  }
}
