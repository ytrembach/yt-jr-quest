package org.yt.jr.quest;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ActiveGames {
    final public static long GAME_INSTANCE_IDLE_TIME = 30; // minutes

    final public static ActiveGames ACTIVE_GAMES = new ActiveGames();

    private List<GameInstance> activeGamesList = new ArrayList<>();

    public void addGameInstance(final GameInstance newGameInstance) {
        activeGamesList.add(newGameInstance);
    }

    public Optional<GameInstance> findGame(final String player) {
        return activeGamesList.stream()
                .filter(g -> g.getPlayer().equals(player))
                .max(Comparator.comparing(GameInstance::getTimestamp));
    }

    public void purgeActiveGamesList(final long minutes) {
        final LocalDateTime now = LocalDateTime.now();
        if (!activeGamesList.isEmpty()) {
            activeGamesList = activeGamesList.stream()
                    .filter(g -> g.getTimestamp().isAfter(now.minusMinutes(minutes)))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }
}
