package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.models.Game;
import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.MatchStatus;
import ar.edu.utn.frc.tup.lciii.models.Player;
import ar.edu.utn.frc.tup.lciii.models.rps.MatchRps;
import ar.edu.utn.frc.tup.lciii.models.rps.PlayRps;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MatchFactory {

    public static Match createMatch(Player player1, Player player2, Game game) {
        switch (game.getCode()) {
            case "RPS":
                return createMatchRps(player1, player2, game);
            default:
                return createMatchRps(player1, player2, game);
        }
    }

    public static Class<? extends Match> getTypeOfMatch(String gameCode) {
        switch (gameCode) {
            case "RPS":
                return MatchRps.class;
            default:
                return MatchRps.class;
        }
    }

    private static Match createMatchRps(Player player1, Player player2, Game game) {
        MatchRps match = (MatchRps) getBasicMatch(player1, player2, game);
        match.setNumberOfPlays(10);
        match.setRemainderPlays(10);
        match.setPlayer1Score(0);
        match.setPlayer2Score(0);
        match.setPlays(new ArrayList<PlayRps>());
        return match;
    }

    private static Match getBasicMatch(Player player, Player player2, Game game) {
        Match match = getMatchInstance(game.getCode());
        match.setPlayer1(player);
        match.setPlayer2(player2);
        match.setGame(game);
        match.setCreatedAt(LocalDateTime.now());
        match.setStatus(MatchStatus.STARTED);
        return match;
    }

    private static Match getMatchInstance(String gameCode) {
        switch (gameCode) {
            case "RPS":
                return new MatchRps();
            default:
                return new MatchRps();
        }
    }
}
