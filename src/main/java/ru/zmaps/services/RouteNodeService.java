package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import ru.zmaps.db.RouteNodeDAO;
import ru.zmaps.parser.entity.RouteNode;

@Log4j
@Service
public class RouteNodeService {

    @Autowired
    RouteNodeDAO routeNodeDAO;


    public RouteNode getNearest(double x, double y) {
        RouteNode rn = routeNodeDAO.getNearest(new Point(x, y));
        rn.setRoutes(null);

        return rn;
    }

}
