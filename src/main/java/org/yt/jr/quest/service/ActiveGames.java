package org.yt.jr.quest.service;

import lombok.Getter;
import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.GameInstance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ActiveGames {

    final public static ActiveGames ACTIVE_GAMES = new ActiveGames();

    final public static long GAME_INSTANCE_IDLE_TIME = 30; // minutes

    private List<GameInstance> activeGamesList = new ArrayList<>();

    public void addGame(final String player, final Game game) {
        activeGamesList.add(new GameInstance(player, game));
    }

    public Optional<GameInstance> findGame(final String player) {
        return activeGamesList.stream()
                .filter(g -> g.getPlayer().equals(player))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp())) // reverse order
                .findFirst();
    }

    public void purgeActiveGamesList(final long minutes) {
        final LocalTime now = LocalTime.now();
        if (activeGamesList.size() > 0 && activeGamesList.get(0).getTimestamp().isBefore(now.minusMinutes(minutes))) {
            activeGamesList = activeGamesList.stream()
                    .filter(g -> g.getTimestamp().isBefore(now.minusMinutes(minutes)))
                    .toList();
        }
    }
}
