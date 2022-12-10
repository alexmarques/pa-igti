package br.com.pa.exceptions;

public class ReceitaNotFoundException extends RuntimeException {
    public ReceitaNotFoundException(String message) {
        super(message);
    }
}
