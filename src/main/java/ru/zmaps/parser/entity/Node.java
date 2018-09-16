package ru.zmaps.parser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Node extends Element {
    private double lat;
    private double lon;

    public Node(double lat, double lon, long id) {
        super(id);

        this.lat = lat;
        this.lon = lon;
    }
}
