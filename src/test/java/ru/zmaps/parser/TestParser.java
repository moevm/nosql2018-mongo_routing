package ru.zmaps.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.zmaps.start.Application;

import java.io.FileInputStream;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestParser {

    @Autowired
    ReaderXmlFile reader;

    @Test
    public void testOpen() throws Exception {

        //        mongo.indexOps(Node.class).ensureIndex(new GeospatialIndex("point"));

        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\map.osm");
        //InputStream inputStream = new BZip2CompressorInputStream(in);


        reader.read(in);
    }

    @Test
    public void parsingWays() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\ways.osm");

        reader.read(in);
    }
}
