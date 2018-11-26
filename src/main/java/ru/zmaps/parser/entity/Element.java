package ru.zmaps.parser.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Element {
    long id;
    List<Tag> tags;

    public Element(long id) {
        this.id = id;

        tags = new ArrayList<>();
    }

    public long getId() {
        return id;
    }



    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
