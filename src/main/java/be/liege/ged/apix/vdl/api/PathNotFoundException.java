package be.liege.ged.apix.vdl.api;

import org.springframework.http.HttpStatus;

public class PathNotFoundException extends Exception {

    private final String path;
    private final HttpStatus httpStatus;

    public PathNotFoundException(String path, HttpStatus httpStatus) {
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public PathNotFoundException(String message, String path, HttpStatus httpStatus) {
        super(message);
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public PathNotFoundException(String message, Throwable throwable, String path, HttpStatus httpStatus) {
        super(message, throwable);
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public PathNotFoundException(Throwable throwable, String path, HttpStatus httpStatus) {
        super(throwable);
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public PathNotFoundException(String message, Throwable throwable, boolean b, boolean b1, String path, HttpStatus httpStatus) {
        super(message, throwable, b, b1);
        this.path = path;
        this.httpStatus = httpStatus;
    }
}
