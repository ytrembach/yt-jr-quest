package org.yt.jr.quest.service;

import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.Node;

import java.util.*;

import static org.yt.jr.quest.model.Game.DEFAULT_GAME;
import static org.yt.jr.quest.model.Game.DEFAULT_GAME_NAME;

public class KnownGames {
    private static Map<String, Game> knownGamesMap = new HashMap<>();

    private static KnownGames instance;

    public static KnownGames getInstance() {
        if (instance == null) {
            instance = new KnownGames();
        }
        return instance;
    }

    static {
        KnownGames.getInstance().registerGame(DEFAULT_GAME_NAME, DEFAULT_GAME);
    }

    //

    public Set<String> getNames() {
        return knownGamesMap.keySet();
    }

    public Optional<Game> getGame(final String gameName) {
        return Optional.ofNullable(knownGamesMap.getOrDefault(gameName, DEFAULT_GAME));
    }

    public void registerGame(final String gameName, final Game game) {
        knownGamesMap.put(gameName, game);
    }

    public Optional<Node> findNode(final Game game, final UUID nodeId) {
        return game.getNodes().stream()
                .filter(node -> node.getId().equals(nodeId))
                .findFirst();
    }
}