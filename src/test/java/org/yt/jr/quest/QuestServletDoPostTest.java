package org.yt.jr.quest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yt.jr.quest.controller.QuestServlet;
import org.yt.jr.quest.model.Demo;
import org.yt.jr.quest.model.Game;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class QuestServletDoPostTest {
    final private static String TEST_PLAYER = "TestPlayer";

    @Mock
    HttpServletRequest mockedReq;

    @Mock
    RequestDispatcher mockedRequestDispatcher;

    @Mock
    HttpServletResponse mockedResp;

    @Mock
    HttpSession mockedSession;

    QuestServlet tested;

    @BeforeEach
    void initTest() {
        tested = new QuestServlet();
    }

    @AfterEach
    void cleanTest() {
        ActiveGames.ACTIVE_GAMES.purgeActiveGamesList(0);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @SneakyThrows
    void doPostNoPlayerAndEnteredPlayerIsInvalid(final String enteredPlayer) {
        final Method doPost = setUpDoPost(null, enteredPlayer, null, true).orElseThrow();
        doPost.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedReq).getRequestDispatcher(QuestServlet.LOGIN_JSP);
    }

    @Test
    @SneakyThrows
    void doPostNoPlayerAndNewPlayerIsValid() {
        final Method doPost = setUpDoPost(null, TEST_PLAYER, null, true).orElseThrow();
        doPost.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedSession).setAttribute("player", TEST_PLAYER);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @SneakyThrows
    void doPostPlayerOkAndNotActiveGameAndEnteredGameIsInvalid(final String enteredGame) {
        final Method doPost = setUpDoPost(TEST_PLAYER, null, enteredGame, true).orElseThrow();
        doPost.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedReq).getRequestDispatcher(QuestServlet.SELECT_GAME_JSP);
    }

    @Test
    @SneakyThrows
    void doPostPlayerOkAndNotActiveGameAndEnteredGameIsValid() {
        final Method doPost = setUpDoPost(TEST_PLAYER, null, Demo.DEMO_NAME, false).orElseThrow();
        doPost.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedResp).sendRedirect(QuestServlet.URL);
    }

    @Test
    @SneakyThrows
    void doPostPlayerOkAndActiveGame() {
        ActiveGames.ACTIVE_GAMES.addGameInstance(new GameInstance(TEST_PLAYER, Mockito.mock(Game.class)));
        final Method doPost = setUpDoPost(TEST_PLAYER, null, null, false).orElseThrow();
        doPost.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedResp).sendRedirect(QuestServlet.URL);
    }

    @SneakyThrows
    private Optional<Method> setUpDoPost(
            final String savedPlayer,
            final String enteredPlayer,
            final String enteredGame,
            final boolean shouldForward) {
        Mockito.when(mockedSession.getAttribute("player")).thenReturn(savedPlayer);
        Mockito.when(mockedReq.getSession()).thenReturn(mockedSession);
        Mockito.lenient().when(mockedReq.getParameter("player")).thenReturn(enteredPlayer);

        if (enteredGame != null) {
            Mockito.lenient().when(mockedReq.getParameter("game")).thenReturn(enteredGame);
        }
        if (shouldForward) {
            Mockito.when(mockedReq.getRequestDispatcher(anyString())).thenReturn(mockedRequestDispatcher);
        }

        final Method doPost = tested.getClass().getDeclaredMethod(
                "doPost",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        doPost.setAccessible(true);
        return Optional.of(doPost);
    }
}
