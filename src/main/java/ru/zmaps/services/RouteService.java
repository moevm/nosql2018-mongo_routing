package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import ru.zmaps.db.RouteDAO;
import ru.zmaps.db.RouteNodeDAO;
import ru.zmaps.parser.entity.Route;
import ru.zmaps.parser.entity.RouteNode;

import java.util.*;

@Log4j
@Service
public class RouteService {

    @Autowired
    RouteNodeDAO routeNodeDAO;

    @Autowired
    RouteDAO routeDAO;


    public Route buildRoute(Long rnId1, Long rnId2) {
        RouteNode rn1 = routeNodeDAO.getById(rnId1);
        RouteNode finish = routeNodeDAO.getById(rnId2);

        if (rn1 == null || finish == null) return null;

        final HashSet<Long> vr = new HashSet<>();

        PriorityQueue<RouteSaver> queue = new PriorityQueue<>(Comparator.comparingDouble(a -> getDistance(a.getFrom().getPoint(), finish.getPoint())));

        for (Route route : rn1.getRoutes()) {
            vr.add(route.getId());

            queue.add(new RouteSaver(route, rn1));
        }

        while (queue.peek().getFrom().getId() != finish.getId()) {
            RouteSaver peek = queue.peek();

            log.info("Current first point: " + peek.getFrom());
            log.info("Current delta: " + getDistance(peek.getFrom().getPoint(), finish.getPoint()));

            RouteSaver next = peek.next(vr);

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
        Long prev = null;
        while (peek != null) {
            w.getNodes().addAll(peek.getRoute(prev));

            prev = peek.from.getId();
            peek = peek.prev;
        }

        w.getNodes().forEach(o -> o.setRoutes(null));

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

        public RouteSaver next(HashSet<Long> vr) {
            while (cur < route.getNodes().size()) {
                RouteNode rn = route.getNodes().get(cur);
                RouteSaver routeSaver = ifOkReturn(rn, vr);

                if (routeSaver != null) {
                    return routeSaver;
                }
                cur++;
            }

            return null;
        }

        public List<RouteNode> getRoute(Long to) {
            long from = this.from.getId();

            if (to == null || from == to) return Collections.singletonList(this.from);

            int fi = -1;
            int ti = -1;

            List<RouteNode> nodes = route.getNodes();

            for (int i = 0; i < nodes.size(); i++) {
                RouteNode node = nodes.get(i);
                if (node.getId() == from) fi = i;
                else {
                    if (node.getId() == to) ti = i;
                }
            }

            if (fi > ti) {
                List<RouteNode> list = route.getNodes().subList(ti, fi);
//                Collections.reverse(list);
                return list;
            }

            List<RouteNode> routeNodes = route.getNodes().subList(fi, ti);
            Collections.reverse(routeNodes);
            return routeNodes;
        }

        private RouteSaver ifOkReturn(RouteNode rn, HashSet<Long> vr) {
            for (int i = 0; i < rn.getRoutes().size(); i++) {
                Route route = rn.getRoutes().get(i);
                if (vr.contains(route.getId())) {
                    rn.getRoutes().remove(i--);
                }
            }

            if (rn.getRoutes().size() > 0) {
                Route rm = rn.getRoutes().remove(0);
                vr.add(rm.getId());

                return new RouteSaver(this, rm, rn);
            }

            return null;
        }

    }

}
