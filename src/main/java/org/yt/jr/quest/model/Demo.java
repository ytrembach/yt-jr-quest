package org.yt.jr.quest.model;

import lombok.Getter;

public class Demo {
    private final String DEMO_NAME = "Test quest";
    private final int DEMO_MAX_LOCATIONS = 10;

    @Getter
    private final Game demo = generateDefaultGame();

    private Game generateDefaultGame() {
        final Game defaultGame = new Game(DEMO_NAME);

        for (int i = 0; i < DEMO_MAX_LOCATIONS; i++) {
            final boolean isFinal = i == DEMO_MAX_LOCATIONS - 1;
            final String locName = "Loc" + i;
            final Node loc = new Node(locName, locName + " banner", isFinal);
            defaultGame.addLocation(loc);
        }

        defaultGame.setStartNode(defaultGame.getNodes().get(0));
        Node last = defaultGame.getNodes().get(DEMO_MAX_LOCATIONS - 1);

        for (int i = 0; i < DEMO_MAX_LOCATIONS - 1; i++) {
            final Node node = defaultGame.getNodes().get(i);
            final Node next = defaultGame.getNodes().get(i + 1);
            node.setQuestion(new Question("Q" + node.getName())
                    .addAnswer(new Answer("Slow", next))
                    .addAnswer(new Answer("Fast", last)));
        }

        return defaultGame;
    }
}
