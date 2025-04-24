package com.matheusmaciel.championship.controller;



import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchFinishedDTO;
import com.matheusmaciel.championship.dto.MatchGenerationDTO;
import com.matheusmaciel.championship.service.MatchService;
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

    @PostMapping("/generate-matches")
    public ResponseEntity<Void> generateMatches(@RequestBody @Valid MatchGenerationDTO dto) {
        service.generateMatches(dto.getFirstRoundDate(), dto.getInvalidDates());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getMatches(){
        if(service.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<MatchDTO> finishMatch(@PathVariable Integer id, @RequestBody MatchFinishedDTO matchFinishedDTO) {
        service.finishMatch(id, matchFinishedDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findMatchById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/update-match/{id}")
    public ResponseEntity<Void> updateMatchResult(@PathVariable Integer id, @RequestBody @Valid MatchFinishedDTO matchFinishedDTO) {
    service.finishMatch(id, matchFinishedDTO);
    return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-team")
    public List<MatchDTO> getMatchesByTeam(@RequestParam String name) {
        return service.getMatchesByTeamName(name);
    }


}
