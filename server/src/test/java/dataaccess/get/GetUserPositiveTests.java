package dataaccess.get;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GetUserPositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void getUserPositive() throws DataAccessException {
        dao.createUser(new UserData("s", "s", "s"));
        assertNotNull(dao.getUser("s"));
    }

}