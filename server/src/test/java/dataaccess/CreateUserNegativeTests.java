package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserNegativeTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }

    @Test
    void CreateUser() throws DataAccessException {
        assertThrows(DataAccessException.class,() -> {  //（查不到报错）
            try (var conn = DatabaseManager.getConnection();
                 var ps = conn.prepareStatement("SELECT * FROM users")) {
                var result = ps.executeQuery();
                assertFalse(result.next());
            }
        });
    }
}
