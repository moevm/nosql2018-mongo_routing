package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import ru.zmaps.db.DbUtils;
import ru.zmaps.parser.entity.Way;

@Log4j
public class WayService {

    private final DbUtils db = DbUtils.getInstance();


    public Way getNearest(double x, double y) {
        log.info("get near x: " + x + " y: " + y);

        return db.getNearestWay(x,y);
    }

}
