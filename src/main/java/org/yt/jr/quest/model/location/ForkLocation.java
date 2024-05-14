package org.yt.jr.quest.model.location;

import org.yt.jr.quest.model.Question;

import java.util.List;

public class ForkLocation extends Location {
    final private List<Question> questions;

    public ForkLocation(String name, String banner, List<Question> questions) {
        super(name, banner);
        this.questions = questions;
    }
}
