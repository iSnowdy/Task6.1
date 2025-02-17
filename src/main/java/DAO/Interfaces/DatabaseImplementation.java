package DAO.Interfaces;

import Utils.Constants;

public interface DatabaseImplementation {
    String db4oDatabaseName = Constants.DB40_DB_NAME;

    void openDB();
    void closeDB();
}
