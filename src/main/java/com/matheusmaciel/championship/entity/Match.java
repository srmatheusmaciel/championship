package com.matheusmaciel.championship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime date;
    private Integer round;
    private Boolean finished;



}
