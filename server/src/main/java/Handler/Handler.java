package Handler;

import com.google.gson.Gson;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Handler {
    private final Gson gson = new Gson();

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Handler(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;
    }


}
