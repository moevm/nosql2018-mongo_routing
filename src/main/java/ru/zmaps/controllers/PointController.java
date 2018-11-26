package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.services.PointService;

import java.util.Arrays;

@RestController
public class PointController {

    private final PointService service = new PointService();

    private final Gson gson = new Gson();


    @GetMapping("/api/point/near")
    public String getNearest(@RequestParam double x, @RequestParam double y) {
        return gson.toJson(service.getNearest(x, y));
    }

}
