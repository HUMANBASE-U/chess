package dataaccess;

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
        dao.createUser(new UserData("s", "s", "s"));
        assertNotNull(dao.getUser("s"));
    }

}