package ru.zmaps.parser.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
public class RouteNode {
    @Id
    long id;

    @DBRef
    private Node node;

    @DBRef(lazy = true)
    List<Route> routes;

    public RouteNode(Node node) {
        id = node.getId();
        this.node = node;
        routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
    }
}
