package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import ru.zmaps.db.RouteDAO;
import ru.zmaps.db.RouteNodeDAO;
import ru.zmaps.parser.entity.Route;
import ru.zmaps.parser.entity.RouteNode;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

@Log4j
@Service
public class RouteService {

    @Autowired
    RouteNodeDAO routeNodeDAO;

    @Autowired
    RouteDAO routeDAO;

    private final HashSet<Long> visitedRoute = new HashSet<>();

    public Route buildRoute(Long rnId1, Long rnId2) {
        RouteNode rn1 = routeNodeDAO.getById(rnId1);
        RouteNode finish = routeNodeDAO.getById(rnId2);

        if (rn1 == null || finish == null) return null;


        PriorityQueue<RouteSaver> queue = new PriorityQueue<>(Comparator.comparingDouble(a -> getDistance(a.getFrom().getPoint(), finish.getPoint())));

        for (Route route : rn1.getRoutes()) {
            queue.add(new RouteSaver(route, rn1));
        }

        while (queue.peek().getFrom().getId() != finish.getId()) {
            RouteSaver peek = queue.peek();

            log.info("Current first point: " + peek.getFrom());
            log.info("Current delta: " + getDistance(peek.getFrom().getPoint(), finish.getPoint()));

            RouteSaver next = peek.next();

            if (next == null) {
                queue.poll();
                if (queue.size() == 0) {
                    log.error("Can't build way \nFrom: " + rn1 + "\n To: " + finish);
                    return null;
                }
            }
            else {
                if (next.checkNodes(rnId2) != null) {
                    break;
                }
                queue.add(next);
            }
        }

        Route w = new Route(null);
        RouteSaver peek = queue.peek();
        while (peek != null) {
            w.addNode(peek.from);
            peek = peek.prev;
        }

        log.info("Build success");

        return w;
    }

    private double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    class RouteSaver {
        private RouteSaver prev;
        private final Route route;
        private final RouteNode from;

        int cur = 0;

        public RouteSaver(Route route, RouteNode from) {
            visitedRoute.add(route.getId());

            this.route = route;
            this.from = from;
        }

        public RouteSaver(RouteSaver prev, Route route, RouteNode from) {
            this(route, from);
            this.prev = prev;
        }

        public RouteNode getFrom() {
            return from;
        }

        public RouteSaver getPrev() {
            return prev;
        }

        public RouteNode checkNodes(long id) {
            for (RouteNode node : route.getNodes()) {
                if (node.getId() == id) {
                    return node;
                }
            }

            return null;
        }

        public RouteSaver next() {
            while (cur < route.getNodes().size()) {
                RouteNode rn = route.getNodes().get(cur);
                RouteSaver routeSaver = ifOkReturn(rn);

                if (routeSaver != null) {
                    return routeSaver;
                }
                cur++;
            }

            return null;
        }

        private RouteSaver ifOkReturn(RouteNode rn) {
            for (int i = 0; i < rn.getRoutes().size(); i++) {
                Route route = rn.getRoutes().get(i);
                if (visitedRoute.contains(route.getId())) {
                    rn.getRoutes().remove(i--);
                }
            }

            if (rn.getRoutes().size() > 0) {
                return new RouteSaver(this, rn.getRoutes().remove(0), rn);
            }

            return null;
        }

    }

}
