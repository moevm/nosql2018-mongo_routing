package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import ru.zmaps.db.DbUtils;
import ru.zmaps.parser.entity.Node;

@Log4j
public class PointService {

    private final DbUtils db = DbUtils.getInstance();


    public Node getNearest(double x, double y) {
        log.info("get near x: " + x + " y: " + y);



        return db.getNearest(x,y);
    }

}
