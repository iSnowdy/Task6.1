package Hugo.PostgreSQL;

import DAO.Interfaces.DatabaseImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class responsible for managing the connection to a PostgreSQL database.
 */
public class DatabaseManager implements DatabaseImplementation {
    // Strings to build the valid URL
    private final String URL = "jdbc:postgresql://";
    private final String HOST = "192.168.56.111";
    private final String PORT = ":5432/";
    private final String DBNAME = "empresa";
    private String VALIDURL;
    // User information
    private final String USERNAME = "hugo";
    private final String PASSWORD = "1234";
    private Connection connection;

    /**
     * Constructor for DatabaseManager. Automatically opens the database connection.
     */
    public DatabaseManager() {
        openDB();
    }

    /**
     * Opens a connection to the PostgreSQL database.
     * Constructs the database URL using predefined fields and
     * connects using the username and password provided.
     */
    @Override
    public void openDB() {
        this.VALIDURL = URL + HOST + PORT + DBNAME;
        try {
            connection = DriverManager.getConnection(this.VALIDURL, this.USERNAME, this.PASSWORD);
            System.out.println("You have been successfully connected to the PostgreSQL database");
        } catch (SQLException sqlException) {
            System.err.println("Error connecting to PostgreSQL database");
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Closes the connection to the database.
     * If the connection is already closed, a message is printed in case of an error.
     */
    @Override
    public void closeDB() {
        try {
            System.out.println("Closing connection...");
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException sqlException) {
            System.err.println("Error closing connection");
            sqlException.printStackTrace();
        }
    }

    /**
     * Retrieves the current database connection.
     * If the connection is closed, it will attempt to reopen the database connection.
     *
     * @return the current {@link Connection} instance
     * @throws SQLException if an error occurs while accessing the connection
     */
    public Connection getConnection() throws SQLException {
        if (connection.isClosed()) {
            openDB();
        }
        return connection;
    }

    /**
     * Checks if the database connection is open.
     * If the connection object is null, it attempts to reopen the connection.
     *
     * @return true if the connection is closed, false otherwise
     */
    public boolean isConnectionOpen() {
        if (connection == null) {
            openDB();
        }

        try {
            return connection.isClosed();
        } catch (SQLException sqlException) {
            System.err.println("Error checking if connection is closed");
            return false;
        }
    }
}
