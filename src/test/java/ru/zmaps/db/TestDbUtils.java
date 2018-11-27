package ru.zmaps.db;

import org.junit.Assert;
import org.junit.Test;
import ru.zmaps.parser.entity.Node;

public class TestDbUtils {
    private final DbUtils db = DbUtils.getInstance();

    @Test
    public void testGetNear() {
        Node nearest = db.getNearest(15.0, 15.0);
        Assert.assertNotNull(nearest);
    }
}
