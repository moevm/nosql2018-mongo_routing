package ru.zmaps.parser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class TestParser {
    @Test
    public void testOpen() throws Exception {
        FileInputStream in = new FileInputStream("C:\\Users\\shabashoff\\Downloads\\australia-oceania-latest.osm.bz2");
        BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);

        ReaderXmlFile reader = new ReaderXmlFile();

        reader.read(bzIn);
    }

}
