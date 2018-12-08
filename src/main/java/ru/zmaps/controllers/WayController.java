package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.services.PointService;
import ru.zmaps.services.WayService;

@RestController
@RequestMapping("/api/way")
public class WayController {

    private final WayService service = new WayService();

    private final Gson gson = new Gson();


    @GetMapping("/near")
    public String getNearest(@RequestParam double x, @RequestParam double y) {
        //return gson.toJson(service.getNearest(x, y));
        return null;
    }

}
