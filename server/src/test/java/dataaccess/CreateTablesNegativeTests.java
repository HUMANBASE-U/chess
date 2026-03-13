package dataaccess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CreateTablesNegativeTests {
    @Test
    @DisplayName("CreateTablesPositiveTests: check the existence")
    void tablesExist() {
        assertDoesNotThrow(() -> {  //查到了就是成功（查不到报错）
            new SqlDao(); //new database
            try (var conn = DatabaseManager.getConnection()) {
                selectQuery(conn, "users");

                selectQuery(conn, "auths");

                selectQuery(conn, "games");
            }
        });

    }

    void selectQuery(Connection conn, String table) throws SQLException {
        try (var ps = conn.prepareStatement("SELECT * FROM " + table)) {
            ps.executeQuery();
        }
    }
}