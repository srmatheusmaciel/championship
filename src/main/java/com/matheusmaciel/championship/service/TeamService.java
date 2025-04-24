package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.TeamNotFoundException;
import com.matheusmaciel.championship.repository.TeamRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }


    public List<TeamDTO> registerTeam(List<TeamDTO> teamDTOs) {
        List<Team> teams = teamDTOs.stream()
                .map(TeamDTO::toEntity)
                .collect(Collectors.toList());
        List<Team> savedTeams = repository.saveAll(teams);


        return savedTeams.stream()
                .map(TeamDTO::fromEntity)
                .collect(Collectors.toList());
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

    public List<Team> findAllEntities() {
        return repository.findAll();
    }



}
