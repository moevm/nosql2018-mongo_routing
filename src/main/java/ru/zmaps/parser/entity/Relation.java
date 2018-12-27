package ru.zmaps.parser.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Relation extends Element {
    Map<String, Long> members;
    String type;

    public Relation(Long id) {
        super(id);
        members = new HashMap<>();
    }

    public Long getMember(String k) {
        return members.get(k);
    }

    public void addMember(String k, Long v) {
        members.put(k, v);
    }

    @Override
    public String toString() {
        return "Relation{" + "members=" + members + ", type='" + type + '\'' + ", id=" + id + ", tags=" + tags + '}';
    }
}
