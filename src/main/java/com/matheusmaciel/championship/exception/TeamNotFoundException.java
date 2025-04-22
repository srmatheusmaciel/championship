package com.matheusmaciel.championship.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Integer id) {
        super("Team with ID " + id + " not found");
    }
}
