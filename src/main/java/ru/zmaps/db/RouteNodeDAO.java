package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.RouteNode;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Repository
public class RouteNodeDAO {


    @Autowired
    private MongoOperations mongo;


    @PostConstruct
    public void initIndexes() {
        mongo.indexOps(RouteNode.class).ensureIndex(new GeospatialIndex("point"));
    }


    public RouteNode getById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id)).limit(1);

        List<RouteNode> routeNodes = mongo.find(query, RouteNode.class);
        if (routeNodes == null || routeNodes.size() == 0) return null;

        return routeNodes.get(0);

    }

    public RouteNode getNearest(Point position) {
        Query query = new Query();
        query.addCriteria(Criteria.where("point").near(position)).limit(1);

        return mongo.find(query, RouteNode.class).get(0);
    }


    public void save(Collection<RouteNode> rn) {
        mongo.insertAll(rn);
    }


    public void save(RouteNode rn) {
        mongo.save(rn);
    }

}
