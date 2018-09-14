package ru.zmaps.parser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class TestParser {
    @Test
    public void testOpen() throws IOException {
        FileInputStream in = new FileInputStream("file-to-parse");
        BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
        final byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = bzIn.read(buffer))) {
            System.out.println(new String(buffer));
        }
    }

}
