package ru.zmaps.parser;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.zmaps.db.RouteDAO;
import ru.zmaps.db.RouteNodeDAO;
import ru.zmaps.db.WayDAO;
import ru.zmaps.parser.entity.Route;
import ru.zmaps.parser.entity.RouteNode;
import ru.zmaps.parser.entity.Way;
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


    @Test
    public void parsing() throws Exception {
        step1();//Add in db all objects


    }

    @Test
    public void parsingWays() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\len.osm");

        reader.read(in);
    }

    @SneakyThrows
    private void step1() {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\len.osm");
        reader.read(in);
    }

    @Test
    public void step2() throws InterruptedException {

        int steps = 50_000;

        List<Way> way = wayDAO.get(Criteria.where("tags.motor_vehicle").exists(true), steps, 0);

        List<Route> routesList = new ArrayList<>();

        HashMap<Long, RouteNode> rnMap = new HashMap<>();

        HashSet<Long> ids = new HashSet<>();

        int skip = steps;

        List<RouteNode> lAddToDb = Collections.synchronizedList(new ArrayList<>());

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

            routeDAO.save(routesList);

            way = wayDAO.get(Criteria.where("tags.motor_vehicle").exists(true), steps, skip);
            skip += steps;

            routesList.clear();
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

        List<Route> way = routeDAO.get();

        for (Route route : way) {
            for (RouteNode node : route.getNodes()) {
                node.addRoute(route);
                routeNodeDAO.save(node);
            }
        }

    }

    @Test
    public void testtt() {
        log.info(routeDAO.get());
    }


}
