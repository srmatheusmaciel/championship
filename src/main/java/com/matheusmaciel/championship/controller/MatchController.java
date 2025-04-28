package com.matheusmaciel.championship.controller;



import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchFinishedDTO;
import com.matheusmaciel.championship.dto.MatchGenerationDTO;
import com.matheusmaciel.championship.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService service;
    

    public MatchController(MatchService service) {
        this.service = service;
    }

    @Operation(
            summary = "Generate matches",
            description = "Generates matches based on the provided date and invalid dates."
    )
    @PostMapping("/generate-matches")
    public ResponseEntity<Void> generateMatches(@RequestBody @Valid MatchGenerationDTO dto) {
        service.generateMatches(dto.getFirstRoundDate(), dto.getInvalidDates());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all matches",
            description = "Retrieves a list of all matches."
    )
    @GetMapping
    public ResponseEntity<List<MatchDTO>> getMatches(){
        if(service.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(service.findAll());
    }

    @Operation(
            summary = "Finish a match",
            description = "Updates the status of a match to 'finished'."
    )
    @PostMapping("/finish/{id}")
    public ResponseEntity<MatchDTO> finishMatch(@PathVariable Integer id, @RequestBody MatchFinishedDTO matchFinishedDTO) {
        service.finishMatch(id, matchFinishedDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get a match by ID",
            description = "Retrieves the details of a specific match using its unique identifier."
    )
    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findMatchById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Operation(
            summary = "Update match result",
            description = "Updates the result of a match."
    )
    @PutMapping("/update-match/{id}")
    public ResponseEntity<Void> updateMatchResult(@PathVariable Integer id, @RequestBody @Valid MatchFinishedDTO matchFinishedDTO) {
    service.finishMatch(id, matchFinishedDTO);
    return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get matches by team name",
            description = "Retrieves a list of matches by team name."
    )
    @GetMapping("/by-team")
    public List<MatchDTO> getMatchesByTeam(@RequestParam String name) {
        return service.getMatchesByTeamName(name);
    }


}
