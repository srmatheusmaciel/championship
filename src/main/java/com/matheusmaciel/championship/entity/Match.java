package com.matheusmaciel.championship.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "team1")
    private Team team1;

    @ManyToOne
    @JoinColumn(name = "team2")
    private Team team2;

    private Integer goalsTeam1;
    private Integer goalsTeam2;
    private Integer attendance;

    public Match() {}

}
