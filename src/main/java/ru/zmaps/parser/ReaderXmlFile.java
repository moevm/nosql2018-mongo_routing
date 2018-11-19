package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReaderXmlFile {

    SAXParserFactory factory = SAXParserFactory.newInstance();

    public void read(InputStream stream) throws Exception {
        factory.newSAXParser().parse(stream, new XmlHandler());
    }

    public void read(InputStream stream, DefaultHandler handler) throws Exception {
        factory.newSAXParser().parse(stream, handler);
    }

}
