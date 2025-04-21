package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.repository.TeamRepository;
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


    public void registerTeam(Team team) {
        repository.save(team);
    }

    public List<Team> teamList() {
        return repository.findAll();
    }

    public Team teamById(Integer id) {
        return repository.findById(id).get();
    }

    public Team updateTeam(Integer id, Team updatedTeam) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        team.setName(updatedTeam.getName());
        team.setCode(updatedTeam.getCode());
        team.setState(updatedTeam.getState());

        return repository.save(team);
    }

    public void deleteTeam(Integer id) {
        Team team = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        repository.delete(team);
    }


}
