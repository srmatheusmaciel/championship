package com.matheusmaciel.championship.repository;

import com.matheusmaciel.championship.entity.Match;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.service.MatchService;
import com.matheusmaciel.championship.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchRepositoryTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamService teamService;

    private List<Team> teamList;

    private Match finishedMatch;
    private Match unfinishedMatch;
    private Team manchesterCity;
    private Team manchesterUnited;
    private Team liverpool;
    private Team chelsea;



    @BeforeEach
    void setUp() {
        teamList = new ArrayList<>();

        manchesterCity = Team.builder()
                .name("Manchester City")
                .code("MCI")
                .state("ENG")
                .stadium("Etihad Stadium")
                .build();

        manchesterUnited = Team.builder()
                .name("Manchester United")
                .code("MUN")
                .state("ENG")
                .stadium("Old Trafford").build();

        liverpool = Team.builder()
                .name("Liverpool")
                .code("LIV")
                .state("ENG")
                .stadium("Anfield")
                .build();

        chelsea = Team.builder()
                .name("Chelsea")
                .code("CHE")
                .state("ENG")
                .stadium("Stamford Bridge")
                .build();

        teamList.add(manchesterCity);
        teamList.add(manchesterUnited);
        teamList.add(liverpool);
        teamList.add(chelsea);


        finishedMatch = Match.builder()
                .id(1)
                .date(LocalDateTime.now())
                .round(1)
                .finished(true)
                .team1(manchesterCity)
                .team2(liverpool)
                .goalsTeam1(3)
                .goalsTeam2(2)
                .attendance(50000)
                .build();

        unfinishedMatch = Match.builder()
                .id(2)
                .date(LocalDateTime.now())
                .round(1)
                .finished(false)
                .team1(manchesterCity)
                .team2(liverpool)
                .goalsTeam1(0)
                .goalsTeam2(0)
                .attendance(0)
                .build();




    }

    @Test
    @DisplayName("Should return only completed matches")
    void shouldReturnOnlyCompletedMatches() {

        when(matchRepository.findAllByFinishedTrue()).thenReturn(List.of(finishedMatch));

        List<Match> matches = matchRepository.findAllByFinishedTrue();

        assertEquals(1, matches.size());

        for(Match match : matches) {
            assertTrue(Boolean.TRUE.equals(match.getFinished()),
                    "Match " + match.getId() + " should be finished");

        }

        boolean containsUnfinishedMatch = matches.stream()
                .anyMatch(match -> !match.getFinished());

        assertFalse(containsUnfinishedMatch,
                "There should be no unfinished matches");

    }

    @Test
    @DisplayName("Should return empty list when no completed matches")
    void shouldReturnEmptyListWhenNoCompletedMatches() {
        when(matchRepository.findAllByFinishedTrue())
                .thenReturn(new ArrayList<>());

        List<Match> matches = matchRepository.findAllByFinishedTrue();

        assertNotNull(matches, "The list of matches should not be null");
        assertTrue(matches.isEmpty(), "The list of matches should be empty");

    }

    @Test
    @DisplayName("Should return matches when team name matches")
    void shouldReturnMatchesWhenTeamNameMatches() {
        when(matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(manchesterCity.getName(), manchesterCity.getName())
                ).thenReturn(List.of(finishedMatch));

        List<Match> matches = matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(manchesterCity.getName(), manchesterCity.getName());

        assertNotNull(matches, "The list of matches should not be null");
        assertFalse(matches.isEmpty(), "The list of matches should not be empty");
        assertTrue(matches.stream()
                        .anyMatch(match -> manchesterCity.getName().equalsIgnoreCase(match.getTeam1().getName())
                                || manchesterCity.getName().equalsIgnoreCase(match.getTeam2().getName())),
                "The list of matches should contain matches where team1 or team2 is " + manchesterCity.getName());

    }

    @Test
    @DisplayName("Should return empty list when team name is null")
    void shouldReturnEmptyListWhenTeamNameIsNull() {
        when(matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(null, null))
                .thenReturn(new ArrayList<>());

        List<Match> matches = matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(null, null);

        assertNotNull(matches, "The list of matches should not be null");
        assertTrue(matches.isEmpty(), "The list of matches should be empty");
    }
}