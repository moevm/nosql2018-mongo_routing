package ru.zmaps.services;

import ru.zmaps.db.DbUtils;

import java.util.List;

public class SearchService {
    private final DbUtils db = DbUtils.getInstance();

    public List<String> getTips(String str) {
        return db.getTipsName(str);
    }

}
