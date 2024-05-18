package org.yt.jr.quest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Answer {
    final private String answer;

    final private Node nextNode;

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                ", nextNode=" + nextNode.getName() +
                '}';
    }
}