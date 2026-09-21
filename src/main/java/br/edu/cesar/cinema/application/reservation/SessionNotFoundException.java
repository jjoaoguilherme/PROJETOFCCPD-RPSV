package br.edu.cesar.cinema.application.reservation;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(Long sessionId) {
        super("Sessao " + sessionId + " nao encontrada.");
    }
}
