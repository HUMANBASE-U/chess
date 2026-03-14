package dataaccess.get;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAuthPositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void GetAuthPositive() throws DataAccessException {
        dao.createUser(new UserData("s","s","s"));
        dao.createAuth(new AuthData("s", "sss"));
        assertNotNull(dao.getAuth("s"));
    }

}