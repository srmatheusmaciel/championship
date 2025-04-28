package com.matheusmaciel.championship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchFinishedDTO {
  private Integer goalsTeam1;
  private Integer goalsTeam2;
  private Integer attendance;
}
