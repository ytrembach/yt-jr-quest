package org.yt.jr.quest;

import lombok.Getter;
import lombok.Setter;
import org.yt.jr.quest.model.Answer;
import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.Node;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class GameInstance {

    final private UUID id = UUID.randomUUID();
    final private LocalTime timestamp = LocalTime.now();

    final private String player;
    final private Game game;

    @Setter
    private Node currentNode;

    public GameInstance(final String player, final Game game) {
        this.player = player;
        this.game = game;
        currentNode = game.getStartNode();
    }

    public Optional<Node> changeNode(final String nextNode) {
        final UUID newNodeId = UUID.fromString(nextNode);
        final Optional<Node> findNewNode = findNode(newNodeId);

        if (findNewNode.isEmpty() ||
                currentNode.getQuestion().getAnswers().stream()
                .map(Answer::getNextNode)
                .noneMatch(node -> newNodeId.equals(node.getId()))) {
            return Optional.empty();
        }

        setCurrentNode(findNewNode.get());
        return Optional.of(currentNode);
    }

    private Optional<Node> findNode(final UUID nodeId) {
        return game.getNodes().stream()
                .filter(node -> node.getId().equals(nodeId))
                .findFirst();
    }
}
