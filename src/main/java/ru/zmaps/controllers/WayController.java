package ru.zmaps.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.services.WayService;

@RestController
@RequestMapping("/api/way")
public class WayController {

    @Autowired
    private WayService service;

    private final Gson gson = new Gson();

    @GetMapping("/get")
    public String getById(@RequestParam long id) {
        return gson.toJson(service.getById(id));
    }

}
