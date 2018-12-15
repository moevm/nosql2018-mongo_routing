package ru.zmaps.parser.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route extends Element {
    public Route(long id) {
        super(id);
    }

    @DBRef
    final List<RouteNode> nodes = new ArrayList<>();

    public void addNode(RouteNode node) {
        nodes.add(node);
    }

    public static Route cloneFromWay(Way way) {
        Route route = new Route(way.getId());

        for (Node node : way.getNodes()) {
            route.addNode(new RouteNode(node));
        }

        route.setTags(way.getTags());

        return route;
    }
}
