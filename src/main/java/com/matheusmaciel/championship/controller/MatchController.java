package com.matheusmaciel.championship.controller;


import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchGenerationDTO;
import com.matheusmaciel.championship.dto.TeamDTO;
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
}
