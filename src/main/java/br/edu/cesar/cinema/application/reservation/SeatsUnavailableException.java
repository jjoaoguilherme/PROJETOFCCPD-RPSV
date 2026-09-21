package br.edu.cesar.cinema.application.reservation;

public class SeatsUnavailableException extends RuntimeException {

    public SeatsUnavailableException(String message) {
        super(message);
    }
}
