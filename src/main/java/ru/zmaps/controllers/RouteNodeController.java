package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.services.RouteNodeService;

@RestController
@RequestMapping("/api/route/node")
public class RouteNodeController {

    @Autowired
    RouteNodeService service;

    private final Gson gson = new Gson();

    @GetMapping("/near")
    public String getNearest(@RequestParam double x, @RequestParam double y) {
        return gson.toJson(service.getNearest(x, y));
    }
}
