package com.matheusmaciel.championship.dto;


import com.matheusmaciel.championship.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO{

    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "Fluminense", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;


    @Schema(example = "FLU")
    @Size(min = 3, max = 4, message = "Code must be between 3 and 4 characters")
    private String code;

    @Schema(example = "RJ")
    @Size(min = 2, max = 3, message = "State must be between 2 and 3 characters")
    @Pattern(regexp = "^[A-Z]{2}$", message = "State must contain only uppercase letters")
    private String state;

    @Schema(example = "Maracana")
    private String stadium;


    public static TeamDTO fromEntity(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .code(team.getCode())
                .state(team.getState())
                .stadium(team.getStadium())
                .build();
    }


    public Team toEntity() {
        return Team.builder()
                .name(this.name)
                .code(this.code)
                .state(this.state)
                .stadium(this.stadium)
                .build();
    }
}
