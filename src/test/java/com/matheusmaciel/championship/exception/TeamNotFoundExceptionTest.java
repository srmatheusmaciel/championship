package com.matheusmaciel.championship.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamNotFoundExceptionTest {

    @Test
    @DisplayName("Should return correct message when exception is thrown")
    void shouldReturnCorrectMessageWhenExceptionIsThrown() {
        int teamId = 1;

        TeamNotFoundException exception = new TeamNotFoundException(teamId);

        assertEquals("Team with ID " + teamId + " not found", exception.getMessage());

    }

}