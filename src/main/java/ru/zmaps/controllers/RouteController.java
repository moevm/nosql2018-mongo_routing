package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.services.RouteService;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    @Autowired
    private RouteService service;

    private final Gson gson = new Gson();


    @GetMapping("/build")
    public String buildRoute(@RequestParam Long id1, @RequestParam Long id2) {
        return gson.toJson(service.buildRoute(id1, id2));
    }

    @GetMapping("/export")
    public String getNearest(@RequestParam int limit, @RequestParam int skip) {
        return gson.toJson(service.export(limit, skip));
    }

}
