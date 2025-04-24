package com.matheusmaciel.championship.repository;


import com.matheusmaciel.championship.entity.Match;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {

  

  List<Match> findAllByFinishedTrue();
  List<Match> findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(String team1, String team2);

}
