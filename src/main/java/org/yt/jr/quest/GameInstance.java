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

    @Setter
    private String message = "";

    public GameInstance(final String player, final Game game) {
        this.player = player;
        this.game = game;
        currentNode = game.getStartNode();
    }

    public Optional<Node> changeNode(final String nextNode) {
        final UUID newNodeId = UUID.fromString(nextNode);
        if (!currentNode.getId().equals(newNodeId)) {
            final Optional<Node> findNewNode = findNode(newNodeId);
            final Optional<Answer> currAnswer = currentNode.getQuestion().getAnswers()
                    .stream()
                    .filter(answer -> newNodeId.equals(answer.getNextNode().getId()))
                    .findFirst();
            if (findNewNode.isEmpty() || currAnswer.isEmpty()) {
                return Optional.empty();
            }
            setMessage(currAnswer.get().getMessage());
            setCurrentNode(findNewNode.get());
        }
        return Optional.of(currentNode);
    }

    private Optional<Node> findNode(final UUID nodeId) {
        return game.getNodes().stream()
                .filter(node -> node.getId().equals(nodeId))
                .findFirst();
    }
}
