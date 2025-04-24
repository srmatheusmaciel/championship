package com.matheusmaciel.championship.dto;

import lombok.Data;

@Data
public class MatchFinishedDTO {
  private Integer goalsTeam1;
  private Integer goalsTeam2;
  private Integer attendance;
}
