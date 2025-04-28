package com.matheusmaciel.championship.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchNotFoundExceptionTest {

    @Test
    @DisplayName("Should return correct message when exception is thrown")
    void shouldReturnCorrectMessageWhenExceptionIsThrown() {
        int matchId = 1;

        MatchNotFoundException exception = new MatchNotFoundException(matchId);

        assertEquals("Match with ID " + matchId + " not found", exception.getMessage());

    }

}