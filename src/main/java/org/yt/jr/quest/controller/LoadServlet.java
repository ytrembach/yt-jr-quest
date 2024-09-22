package org.yt.jr.quest.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.yt.jr.quest.GameLoader;
import org.yt.jr.quest.KnownGames;
import org.yt.jr.quest.model.Game;

import java.io.IOException;

@MultipartConfig
@WebServlet(urlPatterns = "/load")
public class LoadServlet extends HttpServlet {

    private final static String GAME_LOADED_JSP = "/game-loaded.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Part filePart = req.getPart("gamejson");
        final Game newGame = new GameLoader(filePart).loadGame();
        KnownGames.getInstance().registerGame(newGame.getTitle(), newGame);
        req.getRequestDispatcher(GAME_LOADED_JSP).forward(req, resp);
    }
}
