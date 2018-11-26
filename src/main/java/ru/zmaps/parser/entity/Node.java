package ru.zmaps.parser.entity;

import lombok.Data;

@Data
public class Node extends Element {
    private double lat;
    private double lon;

    public Node(double lat, double lon, long id) {
        super(id);

        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Node{" + "lat=" + lat + ", lon=" + lon + ", id=" + id + ", tags=" + tags + '}';
    }
}
