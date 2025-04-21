package com.matheusmaciel.championship.repository;

import com.matheusmaciel.championship.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
