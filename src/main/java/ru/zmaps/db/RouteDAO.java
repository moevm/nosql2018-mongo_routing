package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Route;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class RouteDAO {
    @Autowired
    private MongoOperations mongo;

    @PostConstruct
    public void initIndexes() {
        //mongo.indexOps(Way.class).ensureIndex(new GeospatialIndex("point"));
    }

    public List<Route> get() {
        return mongo.findAll(Route.class);
    }

    public void save(Route way) {
        mongo.save(way);
    }

}
