package com.matheusmaciel.championship.dto;

import lombok.Data;

@Data
public class StandingDTO {
    private Integer teamId;
    private String teamName;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    public StandingDTO(Integer teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public void addMatch(int goalsScored, int goalsConceded) {
        played++;
        goalsFor += goalsScored;
        goalsAgainst += goalsConceded;

        if (goalsScored > goalsConceded) {
            wins++;
            points += 3;
        } else if (goalsScored == goalsConceded) {
            draws++;
            points += 1;
        } else {
            losses++;
        }
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }
}
