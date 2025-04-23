package com.matheusmaciel.championship.service;

import com.matheusmaciel.championship.dto.MatchDTO;
import com.matheusmaciel.championship.dto.TeamDTO;
import com.matheusmaciel.championship.entity.Match;
import com.matheusmaciel.championship.entity.Team;
import com.matheusmaciel.championship.exception.MatchNotFoundException;
import com.matheusmaciel.championship.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

    public void finishMatch(Integer id, MatchDTO matchDTO) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        if(match.getFinished()) {
            throw new IllegalStateException("Match already finished");
        }

        match.setGoalsTeam1(matchDTO.getGoalsTeam1());
        match.setGoalsTeam2(matchDTO.getGoalsTeam2());
        match.setFinished(true);
        match.setAttendance(matchDTO.getAttendance());


        matchRepository.save(match);
    }

}