package handler;




public class HandlerErrorException extends Exception {

    public HandlerErrorException(String message) {

        super(message);
    }

    public HandlerErrorException(String message, Throwable cause) {

        super(message, cause);
    }
}
