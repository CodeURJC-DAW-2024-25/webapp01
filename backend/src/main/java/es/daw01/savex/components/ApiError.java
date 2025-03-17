package es.daw01.savex.components;

public class ApiError {
    private final String message;
    
    public ApiError() {
        this.message = "An error occurred";
    }

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiError [message=" + message + "]";
    }
}