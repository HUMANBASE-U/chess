package dataaccess;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateTablesNegativeTests {
    @Test
    void TableDoesNotExist() {

        assertThrows(SQLException.class, () -> {
            try (var conn = DatabaseManager.getConnection()) {
                try (var ps = conn.prepareStatement("SELECT * FROM users")) {
                    ps.executeQuery();
                }
            }
        });
    }
}