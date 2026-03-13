package dataaccess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CreateUserPositiveTests {

    @Test
    @DisplayName("CreateTablesPositiveTests: check the existence")
    void tablesExist() {
        assertDoesNotThrow(() -> {  //查到了就是成功（查不到报错）
            new SqlDao(); //new database
            String sql = ""
            try (var conn = DatabaseManager.getConnection()) {
                selectQuery(conn, "users");

                selectQuery(conn, "auths");

                selectQuery(conn, "games");
            }
        });

    }
}
