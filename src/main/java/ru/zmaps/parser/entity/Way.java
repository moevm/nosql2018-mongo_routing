package ru.zmaps.parser.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Way extends Element {
    public Way(long id) {
        super(id);
    }

    final List<Node> nodes = new ArrayList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }
}
