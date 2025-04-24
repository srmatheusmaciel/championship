package com.matheusmaciel.championship.controller;

import com.matheusmaciel.championship.dto.StandingDTO;
import com.matheusmaciel.championship.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standings")
public class StandingController {

    private final MatchService matchService;

    public StandingController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<StandingDTO>> getStandings() {
        List<StandingDTO> standings = matchService.calculateStandings();
        return ResponseEntity.ok(standings);
    }
}
