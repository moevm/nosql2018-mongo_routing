package ru.zmaps.parser;

import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.zmaps.db.RelationDAO;
import ru.zmaps.db.RouteDAO;
import ru.zmaps.db.RouteNodeDAO;
import ru.zmaps.db.WayDAO;
import ru.zmaps.parser.entity.*;
import ru.zmaps.start.Application;

import java.io.FileInputStream;
import java.util.*;


@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestParser {

    @Autowired
    ReaderXmlFile reader;

    @Autowired
    WayDAO wayDAO;

    @Autowired
    RouteDAO routeDAO;

    @Autowired
    RouteNodeDAO routeNodeDAO;

    @Autowired
    RelationDAO relationDAO;


    @Test
    public void parsing() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\len.osm");
        reader.read(in);
    }

    @Test
    public void parsingWays() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\len.osm");

        reader.read(in);
    }

    @Test
    public void step2() throws InterruptedException {

        int steps = 50_000;

        List<String> highwayValues = Arrays.asList("motorway",
                "trunk",
                "primary",
                "secondary",
                "tertiary",
                "unclassified",
                "residential",
                "living_street",
                "service",
                "secondary",
                "motorway_link",
                "trunk_link",
                "primary_link",
                "secondary_link",
                "tertiary_link",
                "track",
                "pedestrian",
                "escape",
                "road");

        List<Route> routesList = new ArrayList<>();

        HashMap<Long, RouteNode> rnMap = new HashMap<>();

        HashSet<Long> ids = new HashSet<>();

        int skip;

        List<RouteNode> lAddToDb = Collections.synchronizedList(new ArrayList<>());
        for (String highwayValue : highwayValues) {
            skip = 0;
            List<Way> way = wayDAO.get(Criteria.where("tags.highway").is(highwayValue), steps, skip);
            skip += steps;
            while (way.size() != 0) {

                for (Way w : way) {
                    Route route = Route.cloneFromWay(w);
                    for (RouteNode node : route.getNodes()) {
                        if (!ids.contains(node.getId())) {
                            node.addRoute(route);
                            rnMap.put(node.getId(), node);
                            ids.add(node.getId());
                        }
                        else {
                            if (rnMap.containsKey(node.getId())) {
                                rnMap.get(node.getId()).addRoute(route);
                            }
                            else {
                                RouteNode e = new RouteNode(node.getPoint(), node.getId());
                                e.addRoute(route);
                                lAddToDb.add(e);
                            }
                        }
                    }
                    routesList.add(route);
                }

                try {
                    routeDAO.save(routesList);
                } catch (Exception e) {
                    log.error(e);
                    for (Route route : routesList) {
                        routeDAO.save(route);
                    }
                }

                way = wayDAO.get(Criteria.where("tags.highway").is(highwayValue), steps, skip);
                skip += steps;

                routesList.clear();
            }
        }

        routeNodeDAO.save(rnMap.values());
        rnMap.clear();

        while (lAddToDb.size() > 0) {
            log.debug("Waiting end of route ways, size:" + lAddToDb.size());
            Thread.sleep(100);
        }
    }


    @Test
    public void step3() {
        int limit = 50_000;
        int skip = 0;
        List<Relation> relations = relationDAO.getByCriteria(Criteria.where("tags.type").is("restriction"), limit, skip);
        HashMap<Long, List<Restriction>> rts = new HashMap<>();
        while (relations.size() > 0) {
            skip += limit;
            for (Relation relation : relations) {
                Restriction restr = Restriction.convertRelation(relation);
                if (restr == null) {
                    log.error("Restriction have error!!! Relation:" + relation);
                    continue;
                }

                if (!rts.containsKey(restr.getFrom())) {
                    ArrayList<Restriction> rs = new ArrayList<>();
                    rs.add(restr);
                    rts.put(restr.getFrom(), rs);
                }
                else {
                    rts.get(restr.getFrom()).add(restr);
                }
            }

            rts.forEach((k, v) -> {
                Route route = routeDAO.getById(k);

                if (route == null) return;

                if (route.getRestrictions() == null) {
                    route.setRestrictions(v);
                }
                else {
                    route.getRestrictions().addAll(v);
                }

                routeDAO.save(route);
            });

            rts.clear();
            relations = relationDAO.getByCriteria(Criteria.where("tags.type").is("restriction"), limit, skip);
        }
    }

    @Test
    public void testtt() {
        log.info(routeDAO.get());
    }


}
