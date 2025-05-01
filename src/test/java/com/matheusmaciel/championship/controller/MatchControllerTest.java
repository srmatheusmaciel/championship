package com.matheusmaciel.championship.controller;

import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchFinishedDTO;
import com.matheusmaciel.championship.dto.MatchGenerationDTO;
import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.MatchNotFoundException;
import com.matheusmaciel.championship.service.MatchService;
import com.matheusmaciel.championship.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

class MatchControllerTest {

    private MatchService service;
    private MatchController controller;

    private TeamService teamService = mock(TeamService.class);

    private List<Team> teamList;

    @BeforeEach
    void setUp() {
        service = mock(MatchService.class);
        controller = new MatchController(service);

        teamList = new ArrayList<>();

        Team team1 = Team.builder().name("Manchester City").code("MCI").state("ENG").stadium("Etihad Stadium").build();
        Team team2 = Team.builder().name("Manchester United").code("MUN").state("ENG").stadium("Old Trafford").build();

        teamList.add(team1);
        teamList.add(team2);

        when(teamService.findAllEntities()).thenReturn(teamList);
    }

    @Test
    @DisplayName("Should call service with correct parameters and return ok")
    void shouldCallServiceWithCorrectParametersAndReturnOk() {
        MatchGenerationDTO dto = new MatchGenerationDTO();
        LocalDateTime firstRoundDate = LocalDateTime.of(2025, 5, 1, 0, 0);
        List<LocalDate> invalidDates = List.of(
                LocalDate.of(2025, 5, 15),
                LocalDate.of(2025, 6, 12)
        );
        dto.setFirstRoundDate(firstRoundDate);
        dto.setInvalidDates(invalidDates);

        ResponseEntity<Void> response = controller.generateMatches(dto);

        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<List<LocalDate>> invalidDatesCaptor = ArgumentCaptor.forClass(List.class);

        verify(service, times(1)).generateMatches(dateCaptor.capture(), invalidDatesCaptor.capture());

        assertThat(dateCaptor.getValue()).isEqualTo(firstRoundDate);
        assertThat(invalidDatesCaptor.getValue()).isEqualTo(invalidDates);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should return ok when matches exist")
    void shouldReturnOkWhenMatchesExist() {

        // Dado
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setId(1);
        matchDTO.setTeam1(TeamDTO.fromEntity(teamList.get(0)));
        matchDTO.setTeam2(TeamDTO.fromEntity(teamList.get(1)));
        matchDTO.setGoalsTeam1(0);
        matchDTO.setGoalsTeam2(0);
        matchDTO.setAttendance(0);
        matchDTO.setDate(LocalDateTime.now());
        matchDTO.setRound(1);
        matchDTO.setFinished(false);

        List<MatchDTO> matchList = List.of(matchDTO);

        when(service.findAll()).thenReturn(matchList);

        ResponseEntity<List<MatchDTO>> response = controller.getMatches();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(matchList);
    }

    @Test
    @DisplayName("Should return not found when no matches exist")
    void shouldReturnNotFoundWhenNoMatchesExist() {

        when(service.findAll()).thenReturn(Collections.emptyList());


        ResponseEntity<List<MatchDTO>> response = controller.getMatches();

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Should return 200 ok and match when id exists")
    void shouldReturnOkAndMatchWhenIdExists() {

        Integer matchId = 1;

        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setId(matchId);
        matchDTO.setTeam1(TeamDTO.fromEntity(teamList.get(0)));
        matchDTO.setTeam2(TeamDTO.fromEntity(teamList.get(1)));
        matchDTO.setGoalsTeam1(0);
        matchDTO.setGoalsTeam2(2);
        matchDTO.setAttendance(10000);
        matchDTO.setDate(LocalDateTime.now());
        matchDTO.setRound(1);
        matchDTO.setFinished(false);

        when(service.findById(matchId)).thenReturn(matchDTO);

        ResponseEntity<MatchDTO> response = controller.findMatchById(matchId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(matchDTO);

        verify(service, times(1)).findById(matchId);


    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when id does not exist")
    void shouldThrowMatchNotFoundExceptionWhenIdDoesNotExist() {
        Integer nonExistingId = 999;

        when(service.findById(nonExistingId)).thenThrow(new MatchNotFoundException(nonExistingId));

        assertThrows(
                MatchNotFoundException.class,
                () -> controller.findMatchById(nonExistingId)
        );

        verify(service, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("Should call finishMatch and return no content")
    void shouldCallFinishMatchAndReturnNoContent() {
        Integer matchId = 1;

        MatchFinishedDTO matchFinishedDTO = new MatchFinishedDTO(2,1,5000);

        ResponseEntity<Void> response = controller.updateMatchResult(matchId,
                                                                    matchFinishedDTO);

        verify(service, times(1)).finishMatch(eq(matchId),eq(matchFinishedDTO));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

    }


}
