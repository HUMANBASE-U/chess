package client;

import com.google.gson.Gson;

public class ResponseException extends Exception {
    public enum Code {
        BadRequest,
        Unauthorized,
        Forbidden,
        ServerError
    }

    private final Code code;
    private static final Gson GSON = new Gson();

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public ResponseException(Code code, String message, Throwable ex) {
        super(message, ex);
        this.code = code;
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int status) {
        return switch (status) {
            case 400 -> Code.BadRequest;
            case 401 -> Code.Unauthorized;
            case 403 -> Code.Forbidden;
            default -> Code.ServerError;
        };
    }



    public static ResponseException fromJson(int status, String json) {
        Code code = fromHttpStatusCode(status);
        try {
            ErrorResponse err = GSON.fromJson(json, ErrorResponse.class);
            String msg = (err != null
                    && err.message != null
                    && !err.message.isBlank())
                    ? err.message
                    : "Error: request failed";
            return new ResponseException(code, msg);
        } catch (Exception e) {
            return new ResponseException(code, "Error: request failed");
        }
    }

    private static class ErrorResponse {
        String message;
    }
}