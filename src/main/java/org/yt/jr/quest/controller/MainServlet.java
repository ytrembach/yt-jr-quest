package org.yt.jr.quest.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.*;

import lombok.SneakyThrows;

import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.GameInstance;
import org.yt.jr.quest.service.ActiveGames;
import org.yt.jr.quest.service.KnownGames;
import org.yt.jr.quest.service.NodeService;

@WebServlet(urlPatterns = "/main")
public class MainServlet extends HttpServlet {
    public final static String URL = "/quest/main";
    private final static String NODE_JSP = "/node.jsp";

    @SneakyThrows
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ActiveGames.ACTIVE_GAMES.purgeActiveGamesList(ActiveGames.GAME_INSTANCE_IDLE_TIME);
        super.service(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final PrintWriter writer = resp.getWriter();
        final HttpSession session = req.getSession();

        getErrorMessage(session).ifPresent(msg -> writer.print(msg));

        checkSessionPlayer(req, resp, session);
        final String player = (String) req.getAttribute("player");

        checkSessionGame(req, resp, session);
        final Game game = (Game) req.getAttribute("game");

        NodeService.prepareNode(req, player, game);
        req.getRequestDispatcher(NODE_JSP).forward(req, resp);
    }

    // checks

    private void checkSessionPlayer(final HttpServletRequest req, final HttpServletResponse resp, final HttpSession session) {
        final String player = getSessionPlayer(session).orElse("");
        if (player.isEmpty()) {
            redirectWithError(session, resp, LoginServlet.URL, "You have to login");
        } else {
            req.setAttribute("player", player);
        }
    }

    private Optional<String> getSessionPlayer(final HttpSession session) {
        return Optional.ofNullable((String) session.getAttribute("player"));
    }

    //

    private void checkSessionGame(final HttpServletRequest req, final HttpServletResponse resp, final HttpSession session) {
        final String player = (String) req.getAttribute("player");
        final String gameName = getSessionGame(session).orElse("");
        if (gameName.isEmpty()) {
            final Optional<GameInstance> activeGame = ActiveGames.ACTIVE_GAMES.findGame(player);
            if (activeGame.isEmpty()) {
                redirectWithError(session, resp, LoginServlet.URL, "You have to select the game");
            } else {
                req.setAttribute("game", activeGame.get().getGame());
            }
        } else {
            req.setAttribute("game", KnownGames.getInstance().getGame(gameName).orElseThrow());
        }
    }

    private Optional<String> getSessionGame(final HttpSession session) {
        return Optional.ofNullable((String) session.getAttribute("game"));
    }

    //

    private Optional<String> getErrorMessage(final HttpSession session) {
        return Optional.ofNullable((String) session.getAttribute("errorMessage"));
    }

    @SneakyThrows
    private void redirectWithError(
            final HttpSession session,
            final HttpServletResponse resp,
            final String URL,
            final String errorMessage) {
        session.setAttribute("errorMessage", errorMessage);
        resp.sendRedirect(URL);
    }
}
