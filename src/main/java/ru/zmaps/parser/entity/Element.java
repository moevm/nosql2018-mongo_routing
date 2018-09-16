package ru.zmaps.parser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
