package client;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import com.google.gson.Gson;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }
//Clear
    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null,null);
    }

//User
    public RR.RegisterResult register(RR.RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RR.RegisterResult.class);
    }

    public RR.LoginResult login(RR.LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, RR.LoginResult.class);
    }

    public void logout(RR.LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, logoutRequest.authToken());
    }

//GAME
    public RR.ListGameResult listGames(RR.ListGameRequest listGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, RR.ListGameResult.class, listGameRequest.authToken());
    }

    public RR.CreateGameResult createGame(RR.CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest(
                "POST", path,new RR.CreateGameBody(
                createGameRequest.gameName()),
                RR.CreateGameResult.class,
                createGameRequest.authToken());
    }

    public void joinGame(RR.JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, 
                new RR.JoinGameBody(joinGameRequest.gameID(),
                        joinGameRequest.playerColor()),
                        RR.EmptyResult.class,
                        joinGameRequest.authToken());
    }


    private <T> T makeRequest(String method, String path, Object body, Class<T> responseClass)
            throws ResponseException {
        return makeRequest(method, path, body, responseClass, null);
    }

    private <T> T makeRequest(String method, String path, Object body, Class<T> responseClass, String authToken)
            throws ResponseException {
        HttpRequest request = buildRequest(method, path, body, authToken);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, responseClass);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeJsonBody(body));
        if (body != null) {
            builder.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.setHeader("authorization", authToken);
        }
        return builder.build();
    }


    private HttpRequest.BodyPublisher makeJsonBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }


    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null && !body.isBlank()) {
                throw ResponseException.fromJson(status, body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
