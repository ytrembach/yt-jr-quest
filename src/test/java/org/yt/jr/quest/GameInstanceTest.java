package org.yt.jr.quest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yt.jr.quest.model.Answer;
import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.Node;
import org.yt.jr.quest.model.Question;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GameInstanceTest {
    final static private String TEST_PLAYER = "Player";
    @Mock
    private Game mockedGame;

    @Mock
    Node currentNode;
    UUID currentNodeId;

    @Mock
    Question currentNodeQuestion;

    @Mock
    Answer currentAnswer;

    private GameInstance tested;

    @BeforeEach
    void createTested() {
        tested = new GameInstance(TEST_PLAYER, mockedGame);
        currentNodeId = UUID.randomUUID();
        tested.setCurrentNode(currentNode);
    }

    @Test
    void changeNodeNull() {
        Assertions.assertThrows(NullPointerException.class, () -> tested.changeNode(null));
    }

    @Test
    void changeNodeTheSame() {
        Mockito.when(currentNode.getId()).thenReturn(currentNodeId);
        Assertions.assertEquals(currentNode, tested.changeNode(currentNodeId.toString()).orElseThrow());
    }

    @Test
    void changeNodeNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(currentNode.getId()).thenReturn(currentNodeId);
        Mockito.when(mockedGame.getNodes()).thenReturn(Collections.emptyList());
        Mockito.when(currentNode.getQuestion()).thenReturn(currentNodeQuestion);
        Assertions.assertTrue(tested.changeNode(id.toString()).isEmpty());
    }

    @Test
    void changeNodeFromFinal() { // final node doesn't have answers
        Node newNode = moveConfigure();
        Assertions.assertTrue(tested.changeNode(newNode.getId().toString()).isEmpty());
    }

    @Test
    void changeNodeOk() { // final node doesn't have answers
        Node newNode = moveConfigure();
        Mockito.when(currentNodeQuestion.getAnswers()).thenReturn(List.of(currentAnswer));
        Mockito.when(currentAnswer.getNextNode()).thenReturn(newNode);
        Assertions.assertEquals(newNode, tested.changeNode(newNode.getId().toString()).orElseThrow());
    }

    private Node moveConfigure() {
        UUID toNodeId = UUID.randomUUID();
        Node newNode = Mockito.mock(Node.class);
        Mockito.when(newNode.getId()).thenReturn(toNodeId);

        Mockito.when(currentNode.getId()).thenReturn(currentNodeId);
        Mockito.when(mockedGame.getNodes()).thenReturn(List.of(newNode));

        Mockito.when(currentNode.getQuestion()).thenReturn(currentNodeQuestion);

        return newNode;
    }

}
