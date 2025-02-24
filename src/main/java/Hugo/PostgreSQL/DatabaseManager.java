package Hugo.PostgreSQL;

import DAO.Interfaces.DatabaseImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public DatabaseManager() {
        openDB();
    }

    @Override
    public void openDB() {
        this.VALIDURL = URL + HOST + PORT + DBNAME;
        try {
            connection = DriverManager.getConnection(this.VALIDURL, this.USERNAME, this.PASSWORD);
            System.out.println("You have been succesfully connected to the PostgreSQL" +
                    " database");
        } catch (SQLException sqlException) {
            System.err.println("Error connecting to PostgreSQL" +
                    "");
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    // Closes the connection to the DB
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

    public Connection getConnection() throws SQLException {
        if (connection.isClosed()) {
            openDB();
        }
        return connection;
    }

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
