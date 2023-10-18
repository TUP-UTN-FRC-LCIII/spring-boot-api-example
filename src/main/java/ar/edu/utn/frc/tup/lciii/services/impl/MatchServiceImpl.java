package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.match.MatchDTO;
import ar.edu.utn.frc.tup.lciii.dtos.play.PlayRequest;
import ar.edu.utn.frc.tup.lciii.entities.MatchEntity;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.repositories.MatchEntityFactory;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.MatchJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private static final Long APP_PLAYER_ID = 1000000L;

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayStrategyFactory playStrategyFactory;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Match> getMatchesByPlayer(Long playerId) {
        List<Match> matches = new ArrayList<>();
        Optional<List<MatchEntity>> optionalMatchEntities= matchJpaRepository.getAllByPlayer1Id(playerId);
        if(optionalMatchEntities.isPresent()) {
            optionalMatchEntities.get().forEach(
                me -> {matches.add(modelMapper.map(me, MatchFactory.getTypeOfMatch(me.getGame().getCode())));}
            );
        }
        return matches;
    }

    @Override
    public Match createMatch(MatchDTO matchDTO) {
        Player player1 = playerService.getPlayerById(matchDTO.getPlayer1Id());
        Player player2;
        if(matchDTO.getPlayer2Id() != null) {
            player2 = playerService.getPlayerById(matchDTO.getPlayer2Id());
        } else {
            player2 = playerService.getPlayerById(APP_PLAYER_ID);
        }
        Game game = gameService.getGame(matchDTO.getGameId());
        Match match = MatchFactory.createMatch(player1, player2, game);
        MatchEntity matchEntity = matchJpaRepository.save(modelMapper.map(match, MatchEntityFactory.getTypeOfMatch(match)));
        return modelMapper.map(matchEntity, match.getClass());
    }

    @Override
    public Match getMatchById(Long id) {
        //INFO: https://www.baeldung.com/hibernate-proxy-to-real-entity-object
        MatchEntity me = (MatchEntity) Hibernate.unproxy(matchJpaRepository.getReferenceById(id));
        if(me != null) {
            Match match = modelMapper.map(me, MatchFactory.getTypeOfMatch(me.getGame().getCode()));
            return match;
        }else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    @Override
    public Play play(Long matchId, PlayRequest playRequest) {
        Match match = this.getMatchById(matchId);
        if(match == null) {
            throw new EntityNotFoundException();
        }
        if(match.getStatus() != MatchStatus.STARTED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("The match is %s", match.getStatus()));
        }
        Play play = PlayFactory.getPlayInstance(playRequest, match.getGame().getCode());
        PlayMatch playMatch = playStrategyFactory.getPlayStrategy(match.getGame().getCode());
        return playMatch.play(play, match);
    }
}
