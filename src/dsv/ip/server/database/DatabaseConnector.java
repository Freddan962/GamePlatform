package dsv.ip.server.database;

import java.sql.*;

/**
 * @author Fredrik Sander
 *
 * Database Connector is responsible for providing a interface to interact with a local MySQL database.
 */
public class DatabaseConnector {

  private Connection connection;

  public static DatabaseConnector instance = new DatabaseConnector();

  /**
   * Constructs a new DatabaseConnector instance by preparing the MySQL server connection.
   */
  private DatabaseConnector() {
    try {
      String url = "jdbc:mysql://127.0.0.1/gameplatform";
      connection = DriverManager.getConnection(url, "root", "ascent");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  };

  /**
   * Fetches the static instance of the EventSystem.
   * @return The static DatabaseConnector instance.
   */
  public static DatabaseConnector getInstance() {
    return instance;
  }

  /**
   * Performs a query with the previous established MySQL connection and returns the result.
   * @param query The query to execute.
   * @return The ResultSet of the query.
   */
  public ResultSet query(String query) {
    try {
      Statement stmt = connection.createStatement();
      return stmt.executeQuery(query);
    } catch (Exception e) { e.printStackTrace(); }

    return null;
  }

  /**
   * Executes a query with the previous established MySQL connection and returns the result.
   * @param query The query to execute.
   */
  public void execute(String query) {
    try {
      Statement stmt = connection.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
