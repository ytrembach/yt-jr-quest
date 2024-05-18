package org.yt.jr.quest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {
    final private String title;

    final private List<Node> nodes = new ArrayList<>();

    @Setter
    private Node startNode;

    public Game(String title) {
        this.title = title;
    }

    public void addLocation(final Node loc) {
        nodes.add(loc);
    }

    // default game

    public final static String DEFAULT_GAME_NAME = "Test quest";
    private final static int DEFAULT_GAME_MAX_LOCATIONS = 10;
    public final static Game DEFAULT_GAME = defaultGame();

    private static Game defaultGame() {
        final Game defaultGame = new Game(DEFAULT_GAME_NAME);

        for (int i = 0; i < DEFAULT_GAME_MAX_LOCATIONS; i++) {
            final boolean isFinal = i == DEFAULT_GAME_MAX_LOCATIONS - 1;
            final String locName = "Loc" + i;
            final Node loc = new Node(locName, locName + " banner", isFinal);
            defaultGame.addLocation(loc);
        }

        defaultGame.setStartNode(defaultGame.getNodes().get(0));
        Node last = defaultGame.getNodes().get(DEFAULT_GAME_MAX_LOCATIONS - 1);

        for (int i = 0; i < DEFAULT_GAME_MAX_LOCATIONS - 1; i++) {
            final Node node = defaultGame.getNodes().get(i);
            final Node next = defaultGame.getNodes().get(i + 1);
            node.setQuestion(new Question("Q" + node.getName())
                    .addAnswer(new Answer("Slow", next))
                    .addAnswer(new Answer("Fast", last)));
        }

        return defaultGame;
    }
}
