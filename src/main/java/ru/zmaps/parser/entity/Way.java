package ru.zmaps.parser.entity;


import lombok.Data;

import java.util.List;

@Data
public class Way extends Element {
    public Way(long id) {
        super(id);
    }

    List<Node> nodes;
}
