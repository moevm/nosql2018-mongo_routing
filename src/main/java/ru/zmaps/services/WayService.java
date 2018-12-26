package ru.zmaps.services;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zmaps.db.WayDAO;
import ru.zmaps.parser.entity.Way;

@Log4j
@Service
public class WayService {
    @Autowired
    WayDAO dao;

    public Way getById(Long id) {
        return dao.getById(id);
    }
}
