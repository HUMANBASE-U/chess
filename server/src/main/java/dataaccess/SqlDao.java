package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
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
                CREATE TABLE IF NOT EXISTS auths (
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




    //Clear

    @Override
    public void clear() throws DataAccessException {
        String sql1 =" DROP TABLE users";
        String sql2 =" DROP TABLE auths";
        String sql3 =" DROP TABLE games";
        try(var conn = DatabaseManager.getConnection()){
            var ps1 = conn.prepareStatement(sql1);
            ps1.executeUpdate();

            var ps2 = conn.prepareStatement(sql2);
            ps2.executeUpdate();

            var ps3 = conn.prepareStatement(sql3);
            ps3.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("failed to clear",e);
        }
    }

    //C
    @Override
    public void createUser(UserData user) throws DataAccessException {
        String sql =                 """
                INSERT INTO users (
                    game_name,
                    white_username,
                    black_username,
                    game_json
                ) VALUES (?,?,?,?)
                """;
        String gameJson = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, game.gameName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4, gameJson);

            //Execute
            ps.executeUpdate();

            var result = ps.getGeneratedKeys();
            result.next();

            int gameId = result.getInt(1);
            return gameId;


        } catch (SQLException e) {
            throw new DataAccessException("failed to clear", e);
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public int createGame(GameData game) throws DataAccessException{
        String sql =                 """
                INSERT INTO games (
                    game_name,
                    white_username,
                    black_username,
                    game_json
                ) VALUES (?,?,?,?)
                """;
        String gameJson = new Gson().toJson(game.game());
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, game.gameName());
            ps.setString(2, game.whiteUsername());
            ps.setString(3, game.blackUsername());
            ps.setString(4, gameJson);

            //Execute
            ps.executeUpdate();

            var result = ps.getGeneratedKeys();
            result.next();

            int gameId = result.getInt(1);
            return gameId;


        } catch (SQLException e) {
            throw new DataAccessException("failed to clear", e);
            }
    }


    //R
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


    //U
    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }


    //D
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }


}