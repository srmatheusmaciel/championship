package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.TeamNotFoundException;
import com.matheusmaciel.championship.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;


    @Test
    @DisplayName("Should register team list successfully")
    void shouldRegisterTeamListSuccessfully() {
        TeamDTO teamDTO = TeamDTO.fromEntity(Team.builder()
                .name("Real Madrid")
                .code("RMA")
                .state("MAD")
                .stadium("Santiago Bernabeu")
                .build());

        Team team = teamDTO.toEntity();

        List<Team> savedTeam = List.of(team);

        when(teamRepository.saveAll(anyList())).thenReturn(savedTeam);

        List<TeamDTO> result = teamService.registerTeam(List.of(teamDTO));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Real Madrid", result.get(0).getName());

        verify(teamRepository).saveAll(anyList());

    }

    @Test
    @DisplayName("Should return all teams as DTO")
    void shouldReturnAllTeamsAsDTO() {

    Team team1 = new Team(1, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu");
    Team team2 = new Team(2, "Barcelona", "BAR", "MAD", "Camp Nou");

    List<Team> mockTeams = List.of(team1, team2);
    when(teamRepository.findAll()).thenReturn(mockTeams);

    List<TeamDTO> result = teamService.teamList();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Real Madrid", result.get(0).getName());
    assertEquals("Barcelona", result.get(1).getName());

    verify(teamRepository).findAll();


    }

    @Test
    void shouldReturnEmptyListWhenNoTeamsExist() {

        when(teamRepository.findAll()).thenReturn(List.of());

        List<TeamDTO> result = teamService.teamList();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(teamRepository).findAll();
    }

    @Test
    @DisplayName("Should return mapped team dto when team exists")
    void shouldReturnMappedTeamDTOWhenTeamExists() {
        Team team1 = new Team(1, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu");

        when(teamRepository.findById(1)).thenReturn(Optional.of(team1));

        TeamDTO result = teamService.teamById(1);

        assertNotNull(result);
        assertEquals(team1.getId(), result.getId());
        assertEquals(team1.getName(), result.getName());
        assertEquals(team1.getCode(), result.getCode());
        assertEquals(team1.getState(), result.getState());
        assertEquals(team1.getStadium(), result.getStadium());

        verify(teamRepository).findById(1);


    }

    @Test
    @DisplayName("Should return exception when team does not exist")
    void shouldReturnExceptionWhenTeamDoesNotExist() {

        int teamId = 50;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class, () -> teamService.teamById(teamId));

        assertEquals("Team with ID " + teamId + " not found", exception.getMessage());

        verify(teamRepository).findById(teamId);
    }

    @Test
    @DisplayName("Should update team successfully")
    void shouldUpdateTeamSuccessfully() {
        Team existingTeam = new Team(1, "Real Madrid", "REA", "MD", "Santiago Silva");

        TeamDTO updatedTeamDTO = new TeamDTO(null, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu"); // Don't pass the ID

        when(teamRepository.findById(existingTeam.getId())).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(existingTeam);

        TeamDTO result = teamService.updateTeam(existingTeam.getId(), updatedTeamDTO);

        assertNotNull(result);
        assertEquals(existingTeam.getId(), result.getId()); // The ID should not change
        assertEquals(updatedTeamDTO.getName(), result.getName());
        assertEquals(updatedTeamDTO.getCode(), result.getCode());
        assertEquals(updatedTeamDTO.getState(), result.getState());
        assertEquals(updatedTeamDTO.getStadium(), result.getStadium());

        verify(teamRepository).findById(existingTeam.getId());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw exception when updating nonexistent team")
    void shouldThrowExceptionWhenUpdatingNonexistentTeam() {
        int teamId = 50;
        TeamDTO updatedTeamDTO = new TeamDTO(null, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu"); // Don't pass the ID

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class, () -> teamService.updateTeam(teamId, updatedTeamDTO));

        assertEquals("Team with ID " + teamId + " not found", exception.getMessage());

        verify(teamRepository).findById(teamId);
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    @DisplayName("Should delete team successfully")
    void shouldDeleteTeamSuccessfully() {
        Team team = new Team(1, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu");

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        teamService.deleteTeam(team.getId());

        verify(teamRepository).findById(team.getId());
        verify(teamRepository).delete(team);
    }

    @Test
    @DisplayName("Should throw exception when deleting nonexistent team")
    void shouldThrowExceptionWhenDeletingNonexistentTeam() {
        int teamId = 50;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamNotFoundException exception = assertThrows(TeamNotFoundException.class,
                                        () -> teamService.deleteTeam(teamId));

        assertEquals("Team with ID " + teamId + " not found", exception.getMessage());

        verify(teamRepository).findById(teamId);
        verify(teamRepository, never()).delete(any(Team.class));
    }

    @Test
    @DisplayName("Should return all teams as DTO")
    void shouldReturnAllEntities() {

        List<Team> teams = List.of(
                new Team(1, "Real Madrid", "RMA", "MAD", "Santiago Bernabeu"),
                new Team(2, "Barcelona", "BAR", "MAD", "Camp Nou")
        );


        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.findAllEntities();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Real Madrid", result.get(0).getName());
        assertEquals("Barcelona", result.get(1).getName());

        verify(teamRepository).findAll();


    }







}