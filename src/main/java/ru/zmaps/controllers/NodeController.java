package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.db.NodeDAO;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeDAO dao;

    private final Gson gson = new Gson();


    @GetMapping("/near")
    public String getNearest(@RequestParam double x, @RequestParam double y) {
        return gson.toJson(dao.getNearest(new Point(x, y)));
    }


    @GetMapping("/export")
    public String getNearest(@RequestParam int limit, @RequestParam int skip) {
        return gson.toJson(dao.getByLimit(limit, skip));
    }

    @GetMapping("/get")
    public String getById(@RequestParam long id) {
        return gson.toJson(dao.getNodeById(id));
    }

}
