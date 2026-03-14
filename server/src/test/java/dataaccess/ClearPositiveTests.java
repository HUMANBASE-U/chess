package dataaccess;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static javax.swing.UIManager.getInt;
import static org.junit.jupiter.api.Assertions.*;

public class ClearPositiveTests {
    private SqlDao dao;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        dao.clear();
    }


    @Test
    @DisplayName("clear test")
    void clearDeletesEverything() throws Exception {
//INSERT
        String sql1 ="""
                INSERT INTO users (
                    username,
                    password,
                    email
                ) VALUES (?,?,?)
                """;

        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql1)){
            ps.setString(1, "s");
            ps.setString(2, "d");
            ps.setString(3, "u");
            ps.executeUpdate();
        }

        //TEST
        dao.clear();
        String sqlTest = "SELECT * FROM users";
        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sqlTest);
            var result = ps.executeQuery()){

            assertFalse(result.next());
        }

    }
}