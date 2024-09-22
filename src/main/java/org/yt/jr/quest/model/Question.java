package org.yt.jr.quest.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Question {
    final private String questionText;

    final private List<Answer> answers = new ArrayList<>();

    public Question(final String questionText) {
        this.questionText = questionText;
    }

    public Question addAnswer(final Answer answer) {
        answers.add(answer);
        return this;
    }
}
