package dataaccess.get;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class GetGameNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void getGameNegative() throws DataAccessException {
        assertNull(dao.getGame(0));
    }
}