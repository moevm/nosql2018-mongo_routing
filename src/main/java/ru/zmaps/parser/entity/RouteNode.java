package ru.zmaps.parser.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteNode {
    @Id
    long id;

    Point point;

    @DBRef(lazy = true)
    List<Route> routes;

    public RouteNode(Point point, long id) {
        this.id = id;
        this.point = point;
        routes = new ArrayList<>();
    }

    public void setRoutes(List<Route> rs) {
        routes = rs;
    }

    public void addRoute(Route route) {
        routes.add(route);
    }
}
