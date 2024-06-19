package org.yt.jr.quest.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Question {
    final private String question;
    final private List<Answer> answers = new ArrayList<>();

    public Question(final String question) {
        this.question = question;
    }

    public Question addAnswer(final Answer answer) {
        answers.add(answer);
        return this;
    }
}
