package ru.zmaps.parser.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.geo.Point;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Node extends Element {
    Point point;

    public Node() {
        super(-1L);
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
