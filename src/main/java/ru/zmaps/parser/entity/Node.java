package ru.zmaps.parser.entity;

import lombok.Data;
import org.springframework.data.geo.Point;

@Data
public class Node extends Element {
    private Point point;

    public Node() {
        super(-1);
    }

    public Node(long id, Point point) {
        super(id);

        this.point = point;
    }

    public Node(double lat, double lon, long id) {
        super(id);

        this.point = new Point(lat, lon);
    }
}
