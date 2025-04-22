package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.TeamNotFoundException;
import com.matheusmaciel.championship.repository.TeamRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TeamService {

    private TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }


    public TeamDTO registerTeam(TeamDTO teamDTO) {
        Team teamEntity = teamDTO.toEntity();
        teamEntity = repository.save(teamEntity);
        return TeamDTO.fromEntity(teamEntity);
    }


    public List<TeamDTO> teamList() {
        return repository.findAll().stream().map(TeamDTO::fromEntity).toList();
    }

    public TeamDTO teamById(Integer id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        return TeamDTO.fromEntity(team);
    }


    public TeamDTO updateTeam(Integer id, TeamDTO updatedTeamDTO) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        team.setName(updatedTeamDTO.getName());
        team.setCode(updatedTeamDTO.getCode());
        team.setState(updatedTeamDTO.getState());
        team.setStadium(updatedTeamDTO.getStadium());

        Team updatedTeam = repository.save(team);
        return TeamDTO.fromEntity(updatedTeam);
    }


    public void deleteTeam(Integer id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        repository.delete(team);
    }


}
