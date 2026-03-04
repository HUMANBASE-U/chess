package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class ClearService {
    private final DataAccess dao;

    public ClearService(DataAccess dao) {
        this.dao = dao;
    }

    public void deleteData() throws DataAccessException {
        dao.clear();
    }
}
