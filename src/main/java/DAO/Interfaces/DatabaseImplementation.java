package DAO.Interfaces;

public interface DatabaseImplementation {
    String db4oDatabaseName = "Company_DB4O.db4o";

    void openDB();
    void closeDB();
}
