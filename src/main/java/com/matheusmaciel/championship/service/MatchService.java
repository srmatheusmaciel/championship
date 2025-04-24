package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.MatchFinishedDTO;
import com.matheusmaciel.championship.dto.StandingDTO;
import com.matheusmaciel.championship.entity.Match;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.MatchNotFoundException;
import com.matheusmaciel.championship.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamService teamService;

    public MatchService(MatchRepository matchRepository, TeamService teamService) {
        this.matchRepository = matchRepository;
        this.teamService = teamService;
    }

    public void generateMatches(LocalDateTime firstRoundDate, List<LocalDate> invalidDates) {
        List<Team> teamList = new ArrayList<>(teamService.findAllEntities());



        matchRepository.deleteAll();

        List<Match> matches = new ArrayList<>();

        int total = teamList.size();
        int half = total / 2;

        LocalDateTime matchDate = firstRoundDate;

        for(int round = 0; round < total - 1; round++) {
            for(int i = 0; i < half; i++) {
                Team team1;
                Team team2;

                if(i % 2 == 1 || (round % 2 == 1 && i == 0)) {
                    team1 = teamList.get(total - i -1);
                    team2 = teamList.get(i);
                } else {
                    team1 = teamList.get(i);
                    team2 = teamList.get(total - i -1);

                }

                matches.add(createMatch(matchDate, round + 1, team1, team2));
                matchDate = matchDate.plusDays(7);

            }
            Collections.rotate(teamList.subList(1, teamList.size()), 1);
        }

        matchRepository.saveAll(matches);

        List<Match> returnMatches = matches.stream()
                .map(match -> createMatch(
                        match.getDate().plusDays(7L * matches.size()),
                        match.getRound() + matches.size(),
                        match.getTeam2(),
                        match.getTeam1()
                )).collect(Collectors.toList());

        matchRepository.saveAll(returnMatches);



    }

    private Match createMatch(LocalDateTime date, Integer round, Team team1, Team team2) {
        return Match.builder()
                .date(date)
                .round(round)
                .team1(team1)
                .team2(team2)
                .finished(false)
                .goalsTeam1(0)
                .goalsTeam2(0)
                .attendance(0)
                .build();
    }

    public List<MatchDTO> findAll() {
        return matchRepository.findAll().stream()
                .map(MatchDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public MatchDTO findById(Integer id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));
        return MatchDTO.fromEntity(match);
    }

    public void finishMatch(Integer id, MatchFinishedDTO matchFinishedDTO) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        if(match.getFinished()) {
            throw new IllegalStateException("Match already finished");
        }

        match.setGoalsTeam1(matchFinishedDTO.getGoalsTeam1());
        match.setGoalsTeam2(matchFinishedDTO.getGoalsTeam2());
        match.setFinished(true);
        match.setAttendance(matchFinishedDTO.getAttendance());


        matchRepository.save(match);
    }

    public List<StandingDTO> calculateStandings() {
        List<Match> matches = matchRepository.findAllByFinishedTrue();
    
        Map<Team, StandingDTO> standings = new HashMap<>();
    
        for (Match match : matches) {
            Team team1 = match.getTeam1();
            Team team2 = match.getTeam2();
            int goals1 = match.getGoalsTeam1();
            int goals2 = match.getGoalsTeam2();
    
            standings.putIfAbsent(team1, new StandingDTO(team1.getId(), team1.getName()));
            standings.putIfAbsent(team2, new StandingDTO(team2.getId(), team2.getName()));
    
            StandingDTO standing1 = standings.get(team1);
            StandingDTO standing2 = standings.get(team2);
    
            standing1.addMatch(goals1, goals2);
            standing2.addMatch(goals2, goals1);
        }
    
        return standings.values().stream()
                .sorted(Comparator.comparingInt(StandingDTO::getPoints).reversed()
                        .thenComparingInt(StandingDTO::getGoalDifference).reversed()
                        .thenComparingInt(StandingDTO::getGoalsFor).reversed())
                .collect(Collectors.toList());
    }

    public List<MatchDTO> getMatchesByTeamName(String name) {
        return matchRepository.findByTeam1_NameIgnoreCaseOrTeam2_NameIgnoreCase(name, name)
                .stream()
                .map(MatchDTO::fromEntity)
                .collect(Collectors.toList());
    }
    

}