package com.matheusmaciel.championship.rest;

import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getTeams(){
        if(service.teamList().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.teamList());
    }

    @GetMapping
    public ResponseEntity<Team> getTeamById(@RequestParam Integer id){
        if(service.teamById(id) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.teamById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<Team> registerTeam(@RequestBody Team team){
        service.registerTeam(team);
        return ResponseEntity.ok(team);
    }




}
