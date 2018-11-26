package ru.zmaps.parser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.junit.Test;

import java.awt.im.InputContext;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestParser {
    @Test
    public void testOpen() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\Shabashoff\\IdeaProjects\\nosql-mongo\\src\\test\\resources\\map.osm");
        //InputStream inputStream = new BZip2CompressorInputStream(in);

        ReaderXmlFile reader = new ReaderXmlFile();

        reader.read(in);
    }

}
