package ru.zmaps.controllers;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.zmaps.db.TipDAO;

@Log4j
@RestController
@RequestMapping("/api/tips")
public class TipsController {
    private final Gson gson = new Gson();

    @Autowired
    TipDAO dao;

    @GetMapping("/get")
    public String getTips(@RequestParam String str) {
        log.info("Get tips by: " + str);
        return gson.toJson(dao.getTips(str));
    }

    @GetMapping("/export")
    public String getNearest(@RequestParam int limit, @RequestParam int skip) {
        return gson.toJson(dao.getByLimit(limit, skip));
    }
}
