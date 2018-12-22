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

    private Point point;

    @DBRef(lazy = true)
    List<Route> routes;

    public RouteNode(Point point, long id) {
        this.id = id;
        this.point = point;
        routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
    }
}
