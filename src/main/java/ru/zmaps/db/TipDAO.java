package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Tip;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class TipDAO {

    private final int DEFAULT_LIMIT = 10;

    @Autowired
    private MongoOperations mongo;

    public void save(Tip tip) {
        mongo.save(tip);
    }

    public List<Tip> getTips(String substr, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(Pattern.compile(substr, Pattern.CASE_INSENSITIVE))).limit(limit);
        return mongo.find(query, Tip.class);
    }

    public List<Tip> getTips(String substr) {
        return getTips(substr, DEFAULT_LIMIT);
    }

}
