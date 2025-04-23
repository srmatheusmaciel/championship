package com.matheusmaciel.championship.repository;

import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {

}
