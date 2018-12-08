package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.db.NodeDAO;
import ru.zmaps.parser.entity.Node;
import ru.zmaps.services.PointService;

@RestController
@RequestMapping("/api/point")
public class PointController {

    private final PointService service = new PointService();

    @Autowired
    NodeDAO dao;

    private final Gson gson = new Gson();


    @GetMapping("/create")
    public String create(@RequestParam double x, @RequestParam double y) {
        dao.save(new Node(x, y, (long) y));
        return gson.toJson(null);
    }


    @GetMapping("/near")
    public String getNearest(@RequestParam double x, @RequestParam double y) {
        return gson.toJson(dao.getNearest(new Point(x, y)));
    }
}
