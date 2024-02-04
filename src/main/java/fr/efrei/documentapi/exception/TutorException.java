package fr.efrei.documentapi.exception;

public class TutorException extends RuntimeException{
    public TutorException(String message) {
        super(message);
    }


    public TutorException(String message,Throwable cause) {
        super(message,cause);
    }
}
