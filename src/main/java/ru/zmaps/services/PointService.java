package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import ru.zmaps.db.DbUtils;

import java.util.Arrays;
import java.util.List;

@Log4j
public class PointService {

    private final DbUtils db = DbUtils.getInstance();


    public List<Double> getNearest(double x, double y) {
        log.info("get near x: " + x + " y: " + y);

        return Arrays.asList(x, y);
    }

}
