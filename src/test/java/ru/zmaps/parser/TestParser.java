package ru.zmaps.parser;

import org.junit.Test;

import java.io.FileInputStream;

public class TestParser {
    @Test
    public void testOpen() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\map.osm");
        //InputStream inputStream = new BZip2CompressorInputStream(in);

        ReaderXmlFile reader = new ReaderXmlFile();

        reader.read(in);
    }

    @Test
    public void parsingWays() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\ways.osm");

        ReaderXmlFile reader = new ReaderXmlFile();

        reader.read(in);
    }
}
