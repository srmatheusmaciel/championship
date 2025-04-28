package com.matheusmaciel.championship.repository;


import com.matheusmaciel.championship.entity.Match;

import java.util.ArrayList;
import java.util.List;

import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.service.MatchService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {



  List<Match> findAllByFinishedTrue();
  List<Match> findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(String team1, String team2);

}
