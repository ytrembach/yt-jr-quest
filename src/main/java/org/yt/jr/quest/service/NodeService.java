package org.yt.jr.quest.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.Node;

import java.util.*;

public class NodeService {

    public static void prepareNode(final HttpServletRequest req, final String player, final Game game) {
        final String nodeParam = req.getParameter("node");
        if (nodeParam == null) {
            // new game starts
            prepareStartNode(req, player, game);
        } else {
            final Node node = KnownGames.getInstance().findNode(game, UUID.fromString(nodeParam)).orElseThrow();
            prepareNormalNode(req, game, node);
        }
    }

    @SneakyThrows
    private static void prepareStartNode(final HttpServletRequest req, final String player, final Game game) {
        ActiveGames.ACTIVE_GAMES.addGame(player, game);
        req.setAttribute("gameTitle", game.getTitle());
        req.setAttribute("node", game.getStartNode());
    }

    @SneakyThrows
    private static void prepareNormalNode(final HttpServletRequest req,
                                          final Game game, final Node node) {
        req.setAttribute("gameTitle", game.getTitle());
        req.setAttribute("node", node);
    }
}