package org.yt.jr.quest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.yt.jr.quest.model.Game;

import java.time.LocalDateTime;
import java.util.*;

public class ActiveGamesTest {
    final private static String NOT_FOUND_PLAYER = "Unknown";
    final private static String FOUND_PLAYER = "Known";

    final private static int ACTIVE_GAMES_COUNT = 10;
    final private static int MAX_GAME_INSTANCE_AGE = 120; // minutes
    final private static int GENERATED_PLAYERS = 3;

    private ActiveGames tested;

    @BeforeEach
    void initActiveGames() {
        tested = new ActiveGames();
    }

    private record PreparedGame(String player, LocalDateTime timestamp) {
    }

    private List<PreparedGame> prepareActiveGames(final int maxAge) {
        final Random random = new Random();
        final LocalDateTime now = LocalDateTime.now();

        final List<PreparedGame> gameStartList = new ArrayList<>();
        for (int i = 0; i < ACTIVE_GAMES_COUNT; i++) {
            final String player = FOUND_PLAYER + random.nextInt(GENERATED_PLAYERS);
            gameStartList.add(new PreparedGame(player, now.minusMinutes(random.nextInt(maxAge))));
        }

        MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class);
        for (final PreparedGame preparedGame : gameStartList) {
            mockedLocalDateTime.when(LocalDateTime::now)
                    .thenReturn(preparedGame.timestamp);
            tested.addGameInstance(new GameInstance(preparedGame.player, Mockito.mock(Game.class)));
        }
        mockedLocalDateTime.close();
        return gameStartList;
    }

    @Test
    void findGameNotExist() {
        Assertions.assertTrue(tested.findGame(NOT_FOUND_PLAYER).isEmpty());
    }

    @Test
    void findGameExist() {
        final List<PreparedGame> prepared = prepareActiveGames(MAX_GAME_INSTANCE_AGE);
        final String foundPlayer = prepared.get(0).player;
        final LocalDateTime foundTimestamp = prepared.stream()
                .filter(preparedGame -> preparedGame.player.equals(foundPlayer))
                .max((Comparator.comparing(PreparedGame::timestamp))).get().timestamp();
        Assertions.assertFalse(tested.findGame(foundPlayer).isEmpty());
        Assertions.assertEquals(foundTimestamp, tested.findGame(foundPlayer).get().getTimestamp());
    }

    @Test
    void purgeTest() {
        final LocalDateTime maxTimestamp;

        prepareActiveGames(MAX_GAME_INSTANCE_AGE);
        tested.purgeActiveGamesList(ActiveGames.GAME_INSTANCE_IDLE_TIME);
        final Optional<GameInstance> lastGame = tested.getActiveGamesList().stream()
                .max(Comparator.comparing(GameInstance::getTimestamp));
        Assertions.assertTrue(
                lastGame.isEmpty() ||
                        LocalDateTime.now().minusMinutes(ActiveGames.GAME_INSTANCE_IDLE_TIME)
                                .isBefore(lastGame.get().getTimestamp()));
    }
}
