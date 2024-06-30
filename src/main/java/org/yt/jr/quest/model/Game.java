package org.yt.jr.quest.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Game {
    final private String title;

    final private String intro;

    final private List<Node> nodes = new ArrayList<>();

    @Setter
    private Node startNode;

    public Game(final String title, final String intro) {
        this.title = title;
        this.intro = intro;
    }

    public void addNode(final Node node) {
        nodes.add(node);
    }

    public Optional<Node> findByName(final String nameToSearch) {
        return nodes.stream()
                .filter(node -> node.getName().equals(nameToSearch))
                .findFirst();
    }
}
