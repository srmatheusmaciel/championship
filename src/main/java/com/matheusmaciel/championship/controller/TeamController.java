package com.matheusmaciel.championship.controller;

import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@Validated
public class TeamController {

    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get a team by ID",
            description = "Returns the details of a specific team based on its ID"
    )
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getTeams(){
        if(service.teamList().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(service.teamList());
    }

    @Operation(
            summary = "Get a team by ID",
            description = "Retrieves the details of a specific team using its unique identifier."
    )
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id){
        if(service.teamById(id) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(service.teamById(id));
    }

    @Operation(
            summary = "Create a new team",
            description = "Adds a new team to the database with name, code, and state."
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerTeam(@Valid @RequestBody List<TeamDTO> teamDTOs){
        service.registerTeam(teamDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body("Team registered successfully");
    }

    @Operation(
            summary = "Update a team",
            description = "Updates the information of an existing team using its ID."
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Integer id, @RequestBody TeamDTO teamDTO){
        TeamDTO updatedTeam = service.updateTeam(id, teamDTO);
        return ResponseEntity.ok(updatedTeam);
    }

    @Operation(
            summary = "Delete a team",
            description = "Deletes a team from the database by its ID."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id){
        service.deleteTeam(id);
        return ResponseEntity.ok().build();
    }




}
