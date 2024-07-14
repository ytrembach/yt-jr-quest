package org.yt.jr.quest;

import com.google.gson.Gson;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import org.yt.jr.quest.model.Answer;
import org.yt.jr.quest.model.Game;
import org.yt.jr.quest.model.Node;
import org.yt.jr.quest.model.Question;
import org.yt.jr.quest.model.beans.AnswerData;
import org.yt.jr.quest.model.beans.GameData;
import org.yt.jr.quest.model.beans.NodeData;

import java.io.InputStreamReader;

public class GameLoader {
    final private Part jsonBody;

    private GameData loaded;

    private Game newGame;

    public GameLoader(Part jsonBody) {
        this.jsonBody = jsonBody;
    }

    @SneakyThrows
    public Game loadGame() {
        loaded = new Gson().fromJson(new InputStreamReader(jsonBody.getInputStream()), GameData.class);
        buildStructure();
        reLink();
        return newGame;
    }

    private void buildStructure() {
        newGame = new Game(loaded.getTitle(), loaded.getIntro());
        for (final NodeData nodeData : loaded.getNodes()) {
            final Node newNode = new Node(nodeData.getName(), nodeData.getBanner(), nodeData.isFinal());
            nodeData.setNodeBuilt(newNode);
            if (!nodeData.isFinal()) {
                final Question newQuestion = new Question(nodeData.getQuestion());
                for (final AnswerData answerData : nodeData.getAnswers()) {
                    final Answer newAnswer = new Answer(answerData.getAnswer(), answerData.getMessage(), null);
                    answerData.setAnswerBuilt(newAnswer);
                    newQuestion.addAnswer(newAnswer);
                }
                newNode.setQuestion(newQuestion);
            }
            newGame.addNode(newNode);
        }
        newGame.setStartNode(newGame.getNodes().get(0));
    }

    private void reLink() {
        for (final NodeData node : loaded.getNodes()) {
            if (!node.isFinal()) {
                for (final AnswerData answerData : node.getAnswers()) {
                    answerData.getAnswerBuilt()
                            .setNextNode(newGame.findByName(answerData.getNext()).orElseThrow());
                }
            }
        }
    }
}
