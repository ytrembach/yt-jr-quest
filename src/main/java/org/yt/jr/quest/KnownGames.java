package org.yt.jr.quest;

import org.yt.jr.quest.model.Demo;
import org.yt.jr.quest.model.Game;

import java.util.*;

public class KnownGames {
    private final Map<String, Game> knownGamesMap = new HashMap<>();

    private static KnownGames instance;

    public static KnownGames getInstance() {
        if (instance == null) {
            instance = new KnownGames();

            Game demoGame = new Demo().getDemo();
            instance.registerGame(demoGame.getTitle(), demoGame);
        }
        return instance;
    }

    public List<String> getAllGameNames() {
        return knownGamesMap.keySet()
                .stream()
                .sorted()
                .toList();
    }

    public Optional<Game> getGame(final String gameName) {
        return Optional.ofNullable(knownGamesMap.get(gameName));
    }

    public void registerGame(final String gameName, final Game game) {
        knownGamesMap.put(gameName, game);
    }


}