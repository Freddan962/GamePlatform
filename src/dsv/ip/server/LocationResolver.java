package dsv.ip.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Fredrik Sander
 *
 * LocationREsolver is responsible for performing location lookups through a web API.
 * Retrives information such as city and country for the client's IP address.
 */
public class LocationResolver {

  private String accessKey = "c5596bb9a9679a537b0391d132177e87";

  /**
   * Fetches the city name from a provided IP address.
   * @param ip The IP to fetch the city for.
   * @return The city name.
   */
  public String getCityFromIP(String ip) {
    JSONObject data = getJSONData(ip);
    return (String)data.get("city");
  }

  /**
   * Fetches the country from a provided IP address.
   * @param ip The ip to fetch the country name for.
   * @return The country name.
   */
  public String getCountryFromIP(String ip) {
    JSONObject data = getJSONData(ip);
    return (String)data.get("country_name");
  }

  /**
   * Fetches JSON geodata for provided IP.
   * @param ip The IP to fetch geodata from.
   * @return The JSONObject containing the relevant geodata.
   */
  private JSONObject getJSONData(String ip) {
    BufferedReader reader = getReaderForIP(ip);
    JSONParser parser = new JSONParser();

    try {
      return (JSONObject)parser.parse(reader);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Interacts with the remote webserver and constructs a buffered reader to read required geo data.
   * @param ip The IP to fetch
   * @return The buffered reader for the provided IP.
   */
  private BufferedReader getReaderForIP(String ip) {
    try {
      URL target = new URL("http://api.ipstack.com/" + ip + "?access_key=" + accessKey);
      return new BufferedReader(new InputStreamReader(target.openStream()));
    } catch (Exception e) {
      return null;
    }
  }
}
