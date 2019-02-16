package dsv.ip.client;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * @author Fredrik Sander
 *
 * Responsible for connection related functionality.
 * e.g. fetching the IP, country, and city of the connection.
 */
public class ConnectionHelper {

  private XmlRpcClient connection;

  ConnectionHelper() {
    try {
      connection = new XmlRpcClient(Main.RPCConnectionURL);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fetches the client's IP through amazon's web services.
   * @return The client's IP.
   */
  public String fetchIP() {
    String ip = null;
    try {
      URL ipCheck = new URL("http://checkip.amazonaws.com");
      BufferedReader in = new BufferedReader(new InputStreamReader(ipCheck.openStream()));
      ip = in.readLine();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }

    return ip;
  }

  /**
   * Fetches the country of origin for the client's outgoing connection.
   * @return The client's country.
   */
  public String fetchCountry() {
    Vector args = new Vector();
    args.add(fetchIP());
    try {
      return (String)connection.execute("location_handler.getCountryFromIP", args);
    } catch (XmlRpcException e) { e.printStackTrace();
    } catch (IOException e) { e.printStackTrace(); }

    throw new RuntimeException("Unable to fetch country");
  }

  /**
   * Fetches the city of origin for the client's outgoing connection.
   * @return The client's city.
   */
  public String fetchCity() {
    Vector args = new Vector();
    args.add(fetchIP());
    try {
      return (String)connection.execute("location_handler.getCityFromIP", args);
    } catch (XmlRpcException e) { e.printStackTrace();
    } catch (IOException e) { e.printStackTrace(); }

    throw new RuntimeException("Unable to fetch city");
  }
}
