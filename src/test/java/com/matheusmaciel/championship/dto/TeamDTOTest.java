package com.matheusmaciel.championship.dto;

import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.repository.TeamRepository;
import com.matheusmaciel.championship.service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamDTOTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;



    @Test
    @DisplayName("Should return correct dto list from saved entities")
    void shouldReturnCorrectDTOListFromSavedEntities() {
        TeamDTO inputDTO = TeamDTO.fromEntity(Team.builder()
                .name("Real Madrid")
                .code("RMA")
                .state("MAD")
                .stadium("Santiago Bernabeu")
                .build());


        List<TeamDTO> inputList = List.of(inputDTO);

        Team savedEntity = Team.builder()
                .id(1)
                .name("Real Madrid")
                .code("RMA")
                .state("MAD")
                .stadium("Santiago Bernabeu")
                .build();



        when(teamRepository.saveAll(anyList())).thenReturn(List.of(savedEntity));

        List<TeamDTO> result = teamService.registerTeam(inputList);

        assertNotNull(result);
        assertEquals(1, result.size());

        TeamDTO resultDTO = result.get(0);

        assertEquals("Real Madrid", resultDTO.getName());
        assertEquals("RMA", resultDTO.getCode());
        assertEquals("MAD", resultDTO.getState());
        assertEquals("Santiago Bernabeu", resultDTO.getStadium());


    }

    @Test
    @DisplayName("Should convert dto to entity correctly")
    void shouldConvertDtoToEntityCorrectly() {

        TeamDTO teamDTO = TeamDTO.fromEntity(Team.builder()
                .name("Barcelona")
                .code("BAR")
                .state("MAD")
                .stadium("Camp Nou")
                .build());

        List<Team> savedEntities = List.of(teamDTO.toEntity());

        when(teamRepository.saveAll(anyList())).thenReturn(savedEntities);

        teamService.registerTeam(List.of(teamDTO));

        var captor = ArgumentCaptor.forClass(List.class);


        verify(teamRepository).saveAll(captor.capture());

        List<Team> capturedTeams = captor.getValue();
        Team captured = capturedTeams.get(0);

        assertEquals(teamDTO.getName(), captured.getName());
        assertEquals(teamDTO.getCode(), captured.getCode());
        assertEquals(teamDTO.getState(), captured.getState());
        assertEquals(teamDTO.getStadium(), captured.getStadium());

    }



}