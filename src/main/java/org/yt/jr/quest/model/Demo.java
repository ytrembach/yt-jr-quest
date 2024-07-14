package org.yt.jr.quest.model;

import lombok.Getter;

public class Demo {
    public final static String DEMO_NAME = "Test quest";
    private final static String DEMO_INTRO = "Test quest introduction";
    private final static int DEMO_MAX_NODES = 10;

    @Getter
    private final Game demo = generateDefaultGame();

    private Game generateDefaultGame() {
        final Game defaultGame = new Game(DEMO_NAME, DEMO_INTRO);

        for (int i = 0; i < DEMO_MAX_NODES; i++) {
            final boolean isFinal = i == DEMO_MAX_NODES - 1;
            final String locName = "Loc" + i;
            final Node loc = new Node(locName, locName, isFinal);
            defaultGame.addNode(loc);
        }

        final Node deadEnd = new Node("Loc fail", "You lose! Game over!", true);
        defaultGame.addNode(deadEnd);

        defaultGame.setStartNode(defaultGame.getNodes().get(0));
        Node last = defaultGame.getNodes().get(DEMO_MAX_NODES - 1);

        for (int i = 0; i < DEMO_MAX_NODES - 1; i++) {
            final Node node = defaultGame.getNodes().get(i);
            final Node next = defaultGame.getNodes().get(i + 1);
            node.setQuestion(new Question("Q" + node.getName())
                    .addAnswer(new Answer("Slow", "Do next slow step", Math.random() < 0.8 ? next : deadEnd))
                    .addAnswer(new Answer("Fast", "It was fast", last)));
        }

        return defaultGame;
    }
}
