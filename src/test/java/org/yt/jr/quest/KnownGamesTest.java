package org.yt.jr.quest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yt.jr.quest.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class KnownGamesTest {
    private final static String builtinDemoGame = "Test quest";
    private final static int TEST_GAMES_COUNT = 5;
    private final static String TEST_GAME_NAME_PREFIX = "TestGameName";

    private final static String EXAMPLE_GAME_NAME = "Example game name";
    private final static String EXAMPLE_GAME_TITLE = "Example game title";

    private static KnownGames tested;

    private static List<String> testGameNames;

    @BeforeAll
    static void prepareTested() {
        tested = KnownGames.getInstance();

        testGameNames = new ArrayList<>();
        final Random random = new Random();

        // auto added when KnownGame initialized
        testGameNames.add(builtinDemoGame);

        // hard coded game name to search in test below
        final Game knownGame = Mockito.mock(Game.class);
        Mockito.when(knownGame.getTitle()).thenReturn(EXAMPLE_GAME_TITLE);
        testGameNames.add(EXAMPLE_GAME_NAME);
        tested.registerGame(EXAMPLE_GAME_NAME, knownGame);

        for (int i = 0; i < TEST_GAMES_COUNT - 2; i++) {
            String testGameName;
            do {
                testGameName = TEST_GAME_NAME_PREFIX + random.nextInt(1, 99);
            } while (tested.getGame(testGameName).isPresent());
            testGameNames.add(testGameName);
            tested.registerGame(testGameName, Mockito.mock(Game.class));
        }
    }

    @Test
    void getGameNotExist() {
        final String unknownGameName = "Something";
        Assertions.assertTrue(tested.getGame(unknownGameName).isEmpty());
    }

    @Test
    void getGameExists() {
        Assertions.assertTrue(tested.getGame(EXAMPLE_GAME_NAME).isPresent());
        Assertions.assertEquals(EXAMPLE_GAME_TITLE, tested.getGame(EXAMPLE_GAME_NAME).get().getTitle());
    }

    @Test
    void getAllGameNames() {
        final List<String> returnedNames = tested.getAllGameNames();

        Assertions.assertNotNull(returnedNames);
        Assertions.assertEquals(TEST_GAMES_COUNT, returnedNames.size());
        Assertions.assertTrue(returnedNames.stream().noneMatch(Objects::isNull));

        final List<String> sorted = testGameNames.stream().sorted().toList();
        for (int i = 0; i < TEST_GAMES_COUNT; i++) {
            Assertions.assertEquals(sorted.get(i), returnedNames.get(i));
        }
    }
}
