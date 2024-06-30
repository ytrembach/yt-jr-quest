package org.yt.jr.quest.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.*;

import lombok.SneakyThrows;

import org.yt.jr.quest.GameInstance;
import org.yt.jr.quest.ActiveGames;
import org.yt.jr.quest.KnownGames;

@WebServlet(urlPatterns = { "/", "/main" })
public class QuestServlet extends HttpServlet {
    public final static String URL = "/quest/main";
    private final static String LOGIN_JSP = "/quest/login.jsp";
    private final static String SELECT_GAME_JSP = "/select-game.jsp";
    private final static String NODE_JSP = "/node.jsp";

    @SneakyThrows
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ActiveGames.ACTIVE_GAMES.purgeActiveGamesList(ActiveGames.GAME_INSTANCE_IDLE_TIME);
        super.service(req, resp);
    }

    // post - login

    @SneakyThrows
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) {
        final HttpSession session = req.getSession();

        String player = (String) session.getAttribute("player");
        if (player == null) {
            final String enteredPlayer = req.getParameter("player");
            if (!isValidPlayer(enteredPlayer)) {
                req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
            }
            player = enteredPlayer;
            session.setAttribute("player", player);
        }

        if (ActiveGames.ACTIVE_GAMES.findGame(player).isEmpty()) {
            final String enteredGame = req.getParameter("game");
            if (isValidGame(enteredGame)) {
                ActiveGames.ACTIVE_GAMES.addGame(player, KnownGames.getInstance().getGame(enteredGame).orElseThrow());
                resp.sendRedirect(URL);
            } else {
                req.getRequestDispatcher(SELECT_GAME_JSP).forward(req, resp);
            }
        } else {
            resp.sendRedirect(URL);
        }
    }

    private boolean isValidPlayer(final String enteredPlayer) {
        return enteredPlayer != null && !enteredPlayer.isEmpty() && !enteredPlayer.isBlank();
    }

    private boolean isValidGame(final String enteredGame) {
        return enteredGame != null && KnownGames.getInstance().getAllGameNames().contains(enteredGame);
    }

    // get - main

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final PrintWriter writer = resp.getWriter();
        final HttpSession session = req.getSession();

        getErrorMessage(session).ifPresent(writer::print);

        final String player = getPlayer(req, resp, session);
        if (player.isEmpty()) {
            return;
        }
        final Optional<GameInstance> gameInstance = getGameInstance(req, resp, session, player);
        if (gameInstance.isEmpty()) {
            return;
        }

        final String nextNode = req.getParameter("node");
        if (nextNode != null) {
            gameInstance.get().changeNode(nextNode);
        }
        req.getRequestDispatcher(NODE_JSP).forward(req, resp);
    }

    private String getPlayer(
            final HttpServletRequest req,
            final HttpServletResponse resp,
            final HttpSession session) {
        final String player = Objects.requireNonNullElse((String) session.getAttribute("player"), "");
        if (player.isEmpty()) {
            redirectWithError(session, resp, LOGIN_JSP, "You have to login");
        } else {
            req.setAttribute("player", player);
        }
        return player;
    }

    private Optional<GameInstance> getGameInstance(
            final HttpServletRequest req,
            final HttpServletResponse resp,
            final HttpSession session,
            final String player) {
        final Optional<GameInstance> lastGameInstance = ActiveGames.ACTIVE_GAMES.findGame(player);
        if (lastGameInstance.isEmpty()) {
            redirectWithError(session, resp, SELECT_GAME_JSP, "You have to select the game");
        }
        return lastGameInstance;
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
