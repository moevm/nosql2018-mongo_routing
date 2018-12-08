package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReaderXmlFile {

    SAXParserFactory factory = SAXParserFactory.newInstance();

    @Autowired
    XmlHandler dh;

    public void read(InputStream stream) throws Exception {
        factory.newSAXParser().parse(stream, dh);
    }

    public void read(InputStream stream, DefaultHandler handler) throws Exception {
        factory.newSAXParser().parse(stream, handler);
    }

}
