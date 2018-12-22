package ru.zmaps.parser.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

@Data
public class Element {
    @Id
    Long id;

    Map<String, String> tags;

    public Element(Long id) {
        this.id = id;

        tags = new HashMap<>();
    }

    public long getId() {
        return id;
    }


    public void addTag(String key, String val) {
        tags.put(key, val);
    }
}
