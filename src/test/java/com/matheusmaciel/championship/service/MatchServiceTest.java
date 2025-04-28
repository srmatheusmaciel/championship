package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchFinishedDTO;
import com.matheusmaciel.championship.dto.StandingDTO;
import com.matheusmaciel.championship.entity.Match;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.MatchNotFoundException;
import com.matheusmaciel.championship.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private List<Team> teamList;

    @BeforeEach
    void setUp() {
        teamList = new ArrayList<>();

        Team team1 = Team.builder().name("Manchester City").code("MCI").state("ENG").stadium("Etihad Stadium").build();
        Team team2 = Team.builder().name("Manchester United").code("MUN").state("ENG").stadium("Old Trafford").build();
        Team team3 = Team.builder().name("Liverpool").code("LIV").state("ENG").stadium("Anfield").build();
        Team team4 = Team.builder().name("Chelsea").code("CHE").state("ENG").stadium("Stamford Bridge").build();

        teamList.add(team1);
        teamList.add(team2);
        teamList.add(team3);
        teamList.add(team4);

        when(teamService.findAllEntities()).thenReturn(teamList);
    }

    @Test
    @DisplayName("Should generate matches")
    void shouldGenerateMatches() {

        LocalDateTime firstRoundDate = LocalDateTime.now();
        List<LocalDate> invalidDates = new ArrayList<>();

        matchService.generateMatches(firstRoundDate, invalidDates);

        verify(matchRepository, times(2)).saveAll(anyList());

    }

    @Test
    @DisplayName("Should Not generate matches in invalid dates and skip to next valid date")
    void shouldNotGenerateMatchesInInvalidDatesAndSkipToNextValidDate() {
        LocalDateTime firstRoundDate = LocalDateTime.now();
        List<LocalDate> invalidDates = List.of(
                LocalDate.now().plusDays(7),
                LocalDate.now().plusDays(14)
        );


        matchService.generateMatches(firstRoundDate, invalidDates);


        verify(matchRepository, times(2)).saveAll(anyList());
    }


    @Test
    @DisplayName("Should not generate matches when no teams")
    void shouldNotGenerateMatchesWhenNoTeams() {
        when(teamService.findAllEntities()).thenReturn(new ArrayList<>());

        LocalDateTime firstRoundDate = LocalDateTime.now();
        List<LocalDate> invalidDates = new ArrayList<>();

        matchService.generateMatches(firstRoundDate, invalidDates);

        verify(matchRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should return list of match DTOs")
    void shouldReturnListOfMatchDTOs() {

        Team team1 = Team.builder().name("Manchester City").code("MCI").state("ENG").stadium("Etihad Stadium").build();
        Team team2 = Team.builder().name("Manchester United").code("MUN").state("ENG").stadium("Old Trafford").build();

        Match match1 = Match.builder().date(LocalDateTime.now()).round(1).team1(team1).team2(team2).build();
        Match match2 = Match.builder().date(LocalDateTime.now()).round(1).team1(team2).team2(team1).build();

        when(matchRepository.findAll()).thenReturn(List.of(match1, match2));

        List<MatchDTO> result = matchService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Manchester City", result.get(0).getTeam1().getName());
        assertEquals("Manchester United", result.get(0).getTeam2().getName());
        assertEquals("Manchester United", result.get(1).getTeam1().getName());
        assertEquals("Manchester City", result.get(1).getTeam2().getName());



    }

    @Test
    @DisplayName("Should return empty list when no matches found")
    void shouldReturnEmptyListWhenNoMatchesFound() {
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());

        List<MatchDTO> result = matchService.findAll();

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "The result list should be empty");
    }

    @Test
    @DisplayName("Should return MatchDTO when match is found")
    void shouldReturnMatchDTOWhenMatchIsFound() {
        int matchId = 1;

        Team team1 = teamList.get(0);
        Team team2 = teamList.get(1);

        Match match = Match.builder()
                .id(matchId)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team1)
                .team2(team2)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        MatchDTO result = matchService.findById(matchId);

        assertNotNull(result, "Result should not be null");
        assertEquals(matchId, result.getId());
        assertEquals(team1.getName(), result.getTeam1().getName());
        assertEquals(team2.getName(), result.getTeam2().getName());
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match is not found")
    void shouldThrowMatchNotFoundExceptionWhenMatchIsNotFound() {
        int matchId = 999;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () -> matchService.findById(matchId));

    }

    @Test
    @DisplayName("Should finish match successfully")
    void shouldFinishMatchSuccessfully(){

        Integer matchId = 1;

        Team team3 = teamList.get(2); //from beforeEach
        Team team4 = teamList.get(3);

        Match match = Match.builder()
                .id(matchId)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team3)
                .team2(team4)
                .finished(false)
                .goalsTeam1(0)
                .goalsTeam2(0)
                .attendance(0)
                .build();

        MatchFinishedDTO matchFinishedDTO = MatchFinishedDTO.builder()
                .goalsTeam1(2)
                .goalsTeam2(1)
                .attendance(5000)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        matchService.finishMatch(matchId, matchFinishedDTO);

        assertEquals(2, match.getGoalsTeam1());
        assertEquals(1, match.getGoalsTeam2());
        assertEquals(5000, match.getAttendance());
        assertTrue(match.getFinished());

        verify(matchRepository).save(match);



    }

    @Test
    @DisplayName("Should throw exception when match is already finished")
    void shouldThrowExceptionWhenMatchIsAlreadyFinished() {

        Integer matchId = 1;

        Team team3 = teamList.get(2); //from beforeEach
        Team team4 = teamList.get(3);

        Match match = Match.builder()
                .id(matchId)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team3)
                .team2(team4)
                .finished(true)
                .goalsTeam1(0)
                .goalsTeam2(0)
                .attendance(150000)
                .build();

        MatchFinishedDTO matchFinishedDTO = MatchFinishedDTO.builder()
                .goalsTeam1(2)
                .goalsTeam2(1)
                .attendance(5000)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> matchService.finishMatch(matchId, matchFinishedDTO
                ));

        assertEquals("Match already finished", exception.getMessage());

        verify(matchRepository, never()).save(any());


    }

    @Test
    @DisplayName("Should calculate standings correctly")
    void shouldCalculateStandingsCorrectly(){

        Team team1 = teamList.get(0);
        Team team3 = teamList.get(2);

        Match match1 = Match.builder()
                .id(1)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team1)
                .team2(team3)
                .goalsTeam1(3)
                .goalsTeam2(2)
                .attendance(50000)
                .build();

        Match match2 = Match.builder()
                .id(2)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team1)
                .team2(team3)
                .goalsTeam1(3)
                .goalsTeam2(3)
                .attendance(60000)
                .build();

        when(matchRepository.findAllByFinishedTrue())
                .thenReturn(Arrays.asList(match1, match2));

        List<StandingDTO> standings = matchService.calculateStandings();

        assertEquals(2, standings.size());

        StandingDTO team1Standing = standings.get(0);
        assertThat(team1Standing.getTeamName()).isEqualTo(team1.getName());
        assertThat(team1Standing.getPoints()).isEqualTo(4);
        assertThat(team1Standing.getGoalsFor()).isEqualTo(6);
        assertThat(team1Standing.getGoalsAgainst()).isEqualTo(5);
        assertThat(team1Standing.getGoalDifference()).isEqualTo(1);

        StandingDTO team3Standing = standings.get(1);
        assertThat(team3Standing.getTeamName()).isEqualTo(team3.getName());
        assertThat(team3Standing.getPoints()).isEqualTo(1);
        assertThat(team3Standing.getGoalsFor()).isEqualTo(5);
        assertThat(team3Standing.getGoalsAgainst()).isEqualTo(6);
        assertThat(team3Standing.getGoalDifference()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should calculate standings correctly when there are no matches")
    void shouldCalculateStandingsCorrectlyWhenThereAreNoMatches() {
        when(matchRepository.findAllByFinishedTrue())
                .thenReturn(Collections.emptyList());

        List<StandingDTO> standings = matchService.calculateStandings();

        assertThat(standings).isNotNull();
        assertTrue(standings.isEmpty());
    }

    @Test
    @DisplayName("Should return matches by team name")
    void shouldReturnMatchesByTeamName(){

        Team team1 = teamList.get(0);
        Team team3 = teamList.get(2);

        Match match1 = Match.builder()
                .id(1)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team1)
                .team2(team3)
                .goalsTeam1(3)
                .goalsTeam2(2)
                .attendance(50000)
                .build();

        Match match2 = Match.builder()
                .id(2)
                .date(LocalDateTime.now())
                .round(1)
                .team1(team1)
                .team2(team3)
                .goalsTeam1(3)
                .goalsTeam2(3)
                .attendance(60000)
                .build();

        when(matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase("Manchester City", "Manchester City"))
                .thenReturn(Arrays.asList(match1, match2));

        List<MatchDTO> result = matchService.getMatchesByTeamName("Manchester City");

        assertThat(result).hasSize(2);

        MatchDTO firstMatch = result.get(0);
        assertThat(firstMatch.getTeam1().getName()).isEqualTo("Manchester City");
        assertThat(firstMatch.getTeam2().getName()).isEqualTo("Liverpool");
        assertThat(firstMatch.getGoalsTeam1()).isEqualTo(3);
        assertThat(firstMatch.getGoalsTeam2()).isEqualTo(2);
        assertThat(firstMatch.getAttendance()).isEqualTo(50000);

        MatchDTO secondMatch = result.get(1);
        assertThat(secondMatch.getTeam1().getName()).isEqualTo("Manchester City");
        assertThat(secondMatch.getTeam2().getName()).isEqualTo("Liverpool");
        assertThat(secondMatch.getGoalsTeam1()).isEqualTo(3);
        assertThat(secondMatch.getGoalsTeam2()).isEqualTo(3);
        assertThat(secondMatch.getAttendance()).isEqualTo(60000);
    }
}