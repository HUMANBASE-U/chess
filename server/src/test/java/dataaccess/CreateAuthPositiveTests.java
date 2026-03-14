
package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateAuthPositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void createAuth() throws DataAccessException {
        dao.createUser(new UserData("s", "s", "s"));
        dao.createAuth(new AuthData("sx1", "s"));

        assertDoesNotThrow(() -> {
            try (var conn = DatabaseManager.getConnection();
                 var ps = conn.prepareStatement("SELECT * FROM auths");
                 var result = ps.executeQuery()) {
                assertTrue(result.next());
            }
        });
    }
}
