package service;


import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class ClearAllService {

    private final DataAccess dbAccess;

    public ClearAllService(DataAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public void clearAll() throws DataAccessException {
            dbAccess.getAuthDAO().clear();
            dbAccess.getUserDAO().clear();
            dbAccess.getGameDAO().clear();
    }

}
