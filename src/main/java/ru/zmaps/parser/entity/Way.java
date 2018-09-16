package ru.zmaps.parser.entity;


import lombok.Data;

import java.util.List;

@Data
public class Way extends HasId{
    List<Node> nodes;
}
