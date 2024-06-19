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
}
