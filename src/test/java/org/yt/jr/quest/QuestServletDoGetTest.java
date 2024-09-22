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

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class QuestServletDoGetTest {
    final private static String TEST_PLAYER = "TestPlayer";
    final private static String NEXT_NODE_ID = "e817b778-2e82-433d-86f5-9e60ed881d0f";

    @Mock
    HttpServletRequest mockedReq;

    @Mock
    RequestDispatcher mockedRequestDispatcher;

    @Mock
    HttpServletResponse mockedResp;

    @Mock
    HttpSession mockedSession;

    @Mock
    PrintWriter writer;

    QuestServlet tested;

    @BeforeEach
    void initTest() {
        tested = new QuestServlet();
    }

    @AfterEach
    void cleanTest() {
        ActiveGames.ACTIVE_GAMES.purgeActiveGamesList(0);
    }

    @Test
    @SneakyThrows
    void playerInSessionIsEmpty() {
        final Method doGet = setUpDoGet(false).orElseThrow();
        doGet.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedSession).setAttribute(eq("errorMessage"), anyString());
        Mockito.verify(mockedResp).sendRedirect(QuestServlet.LOGIN_JSP);
    }

    @Test
    @SneakyThrows
    void gameNotFound() {
        final Method doGet = setUpDoGet(false).orElseThrow();
        Mockito.lenient().when(mockedSession.getAttribute("player")).thenReturn(TEST_PLAYER);
        doGet.invoke(tested, mockedReq, mockedResp);
        Mockito.verify(mockedSession).setAttribute(eq("errorMessage"), anyString());
        Mockito.verify(mockedResp).sendRedirect(QuestServlet.SELECT_GAME_JSP);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource( strings = NEXT_NODE_ID)
    @SneakyThrows
    void nextNode(final String nextNode) {
        final Method doGet = setUpDoGet(true).orElseThrow();

        final GameInstance mockedGameInstance = Mockito.mock(GameInstance.class);
        if (nextNode != null) {
            Mockito.when(mockedGameInstance.changeNode(anyString())).thenReturn(Optional.empty());
        }
        Mockito.when(mockedGameInstance.getTimestamp()).thenReturn(LocalDateTime.MIN);
        Mockito.when(mockedGameInstance.getPlayer()).thenReturn(TEST_PLAYER);
        ActiveGames.ACTIVE_GAMES.addGameInstance(mockedGameInstance);

        Mockito.lenient().when(mockedSession.getAttribute("player")).thenReturn(TEST_PLAYER);
        Mockito.when(mockedReq.getParameter("node")).thenReturn(nextNode);

        doGet.invoke(tested, mockedReq, mockedResp);
        if (nextNode == null) {
            Mockito.verify(mockedGameInstance, Mockito.never()).changeNode(anyString());
        } else {
            Mockito.verify(mockedGameInstance).changeNode(anyString());
        }
        Mockito.verify(mockedReq).getRequestDispatcher(QuestServlet.NODE_JSP);
    }

    @SneakyThrows
    private Optional<Method> setUpDoGet(final boolean shouldForward) {
        Mockito.when(mockedReq.getSession()).thenReturn(mockedSession);
        Mockito.when(mockedResp.getWriter()).thenReturn(writer);
        if (shouldForward) {
            Mockito.when(mockedReq.getRequestDispatcher(Mockito.anyString())).thenReturn(mockedRequestDispatcher);
        }

        final Method doGet = tested.getClass().getDeclaredMethod(
                "doGet",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        doGet.setAccessible(true);
        return Optional.of(doGet);

    }
}
