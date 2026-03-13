package dataaccess;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static javax.swing.UIManager.getInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearPositiveTests {
    private SqlDao dao;
    private Gson gson;

    @BeforeEach
    void setUp() throws DataAccessException {
        dao = new SqlDao();
        gson = new Gson();
        dao.clear();
    }


    @Test
    @DisplayName("clear test")
    void clearDeletesEverything() throws Exception {
//INSERT
        String sql1 =                 """
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

        String sqlTest1 = """
                SELECT COUNT* FROM games;
                """;
        try(var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sqlTest1)){

            var result = ps.executeQuery();
            result.next();
            assertEquals(0, result.getInt(1));
        }



    }
}