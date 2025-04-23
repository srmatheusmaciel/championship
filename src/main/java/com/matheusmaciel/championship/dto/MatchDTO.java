package com.matheusmaciel.championship.dto;

import com.matheusmaciel.championship.entity.Match;
import com.matheusmaciel.championship.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;


    private TeamDTO team1;

    private TeamDTO team2;

    private Integer goalsTeam1;

    private Integer goalsTeam2;

    private Integer attendance;

    private LocalDateTime date;

    private Integer round;

    private Boolean finished;

    public static MatchDTO fromEntity(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .team1(TeamDTO.fromEntity(match.getTeam1()))
                .team2(TeamDTO.fromEntity(match.getTeam2()))
                .goalsTeam1(match.getGoalsTeam1())
                .goalsTeam2(match.getGoalsTeam2())
                .attendance(match.getAttendance())
                .date(match.getDate())
                .round(match.getRound())
                .finished(match.getFinished())
                .build();
    }

    public Match toEntity() {

        Team team1Entity = this.team1 != null ? this.team1.toEntity() : null;
        Team team2Entity = this.team2 != null ? this.team2.toEntity() : null;

        return Match.builder()
                .team1(this.team1.toEntity())
                .team2(this.team2.toEntity())
                .goalsTeam1(this.goalsTeam1)
                .goalsTeam2(this.goalsTeam2)
                .attendance(this.attendance)
                .date(this.date)
                .round(this.round)
                .finished(this.finished)
                .build();
    }
}
