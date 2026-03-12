package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.List;

public class SqlDao implements DataAccess {
    private final Gson gson = new Gson();

    public SqlDao() throws DataAccessException {
        DatabaseManager.createDatabase();
        createTables();
    }

    private void createTables() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {

            try (var[] preparedStatement = (
                """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(256) PRIMARY KEY,
                    password VARCHAR(60) NOT NULL,
                    email    VARCHAR(256) NOT NULL
                )
                """,

                """
                CREATE TABLE IF NOT EXISTS auth (
                    auth_token CHAR(36) PRIMARY KEY,
                    username   VARCHAR(256) NOT NULL,
                    INDEX(username),
                    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS games (
                    game_id        INT PRIMARY KEY AUTO_INCREMENT,
                    game_name      VARCHAR(256) NOT NULL,
                    white_username VARCHAR(256) NULL,
                    black_username VARCHAR(256) NULL,
                    game_json      LONGTEXT NOT NULL
                )
                """


                    )) {
                preparedStatement.executeUpdate();
            }



        } catch (SQLException e) {
            throw new DataAccessException("table create error", e);
        }
    }




    //C

    @Override
    public void clear() throws DataAccessException {
        
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public int createGame(GameData game) throws DataAccessException{
        String sql =                 """
                INSERT INTO games (
                    game_id,
                    game_name,
                    white_username,
                    black_username,
                    game_json
                )
                """;

        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql)) {

            ps.setString(2, game.gameName());
            ps.setString(3, game.whiteUsername());
            ps.setString(4, game.blackUsername());
            //Execute
            ps.executeUpdate();

            var result = ps.executeQuery();
            ps.setString(1, game.gameName());


        } catch (SQLException e) {
            throw new DataAccessException("failed to clear", e);
            }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }


}