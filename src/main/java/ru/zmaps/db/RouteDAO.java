package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Route;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Repository
public class RouteDAO {
    @Autowired
    private MongoOperations mongo;

    @PostConstruct
    public void initIndexes() {
        //mongo.indexOps(Way.class).ensureIndex(new GeospatialIndex("point"));
    }


    public void save(Collection<Route> r) {
        mongo.insertAll(r);
    }

    public List<Route> getByLimit(int limit, int skip) {
        Query query = new Query();
        query.limit(limit).skip(skip);
        return mongo.find(query, Route.class);
    }

    public Route getById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id)).limit(1);

        List<Route> routes = mongo.find(query, Route.class);
        if (routes == null || routes.size() == 0) return null;

        return routes.get(0);

    }

    public List<Route> get() {
        return mongo.findAll(Route.class);
    }

    public void save(Route r) {
        mongo.save(r);
    }

}
