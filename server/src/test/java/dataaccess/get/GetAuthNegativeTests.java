package dataaccess.get;

import dataaccess.DataAccessException;
import dataaccess.SqlDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class GetAuthNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        assertNull(dao.getAuth("s"));
    }
}
