
package dataaccess;

import com.google.gson.Gson;
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
    void CreateUser() throws DataAccessException {
        dao.createUser(new UserData("s","s","s"));

        assertDoesNotThrow(() -> {  //查到了就是成功（查不到报错）
            try (var conn = DatabaseManager.getConnection();
                 var ps = conn.prepareStatement("SELECT * FROM users")) {
                var result = ps.executeQuery();
                assertTrue(result.next());
            }
        });
    }
}
