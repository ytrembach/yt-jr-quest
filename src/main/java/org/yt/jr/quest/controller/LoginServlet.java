package org.yt.jr.quest.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.SneakyThrows;
import org.yt.jr.quest.service.KnownGames;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    public final static String URL = "/quest/login.jsp";
    private final static String LOGIN_JSP = "/login.jsp";

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String enteredPlayer = req.getParameter("player");
        final String enteredGame = req.getParameter("game");
        final HttpSession session = req.getSession();

        if (!isValidPlayer(enteredPlayer)) {
            session.setAttribute("errorMessage", "Invalid user");
            req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
        }

        if (!isValidGame(enteredGame)) {
            session.setAttribute("errorMessage", "Unknown game");
            req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
        }

        session.setAttribute("player", enteredPlayer);
        session.setAttribute("game", enteredGame);
        resp.sendRedirect(MainServlet.URL);
    }

    private boolean isValidPlayer(final String enteredPlayer) {
        return enteredPlayer != null && !enteredPlayer.isEmpty() && !enteredPlayer.isBlank();
    }

    private boolean isValidGame(final String enteredGame) {
        return enteredGame != null && KnownGames.getInstance().getNames().contains(enteredGame);
    }

}
