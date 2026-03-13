package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryDao implements DataAccess{
    private final Map<String, UserData> userMap = new HashMap<>();
    private final Map<String, AuthData> authMap = new HashMap<>();
    private final Map<Integer, GameData> gameMap = new HashMap<>();
    private int gameId = 1;

    //clear
    @Override
    public void clear() {
        userMap.clear();
        authMap.clear();
        gameMap.clear();
    }

    //C
    @Override
    public void createUser(UserData user) throws DataAccessException{
        userMap.put(user.username(), user);
    }
    @Override
    public void createAuth(AuthData auth) throws DataAccessException{
        authMap.put(auth.authToken(), auth);
    }
    @Override
    public int createGame(GameData game) throws DataAccessException{

        gameMap.put(++gameId, game);
        return gameId;
    }

    //R
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        return authMap.get(authToken);
    }
    @Override
    public UserData getUser(String username) throws DataAccessException{
        return userMap.get(username);
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        return gameMap.get(gameID);
    }
    @Override
    public List<GameData> listGames() throws DataAccessException{
        return new ArrayList<>(gameMap.values());
    }

    //U
    @Override
    public void updateGame(GameData game) throws DataAccessException{
        gameMap.put(game.gameID(), game);
    }

    //D
    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        authMap.remove(authToken);
    }
}
