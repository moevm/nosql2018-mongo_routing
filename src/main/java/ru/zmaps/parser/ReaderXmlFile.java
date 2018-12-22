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
        Thread.sleep(1_500);//To add all entities to db

        while (dh.getListNode().size() != 0) Thread.sleep(100);
        while (dh.getListTips().size() != 0) Thread.sleep(100);
        while (dh.getListWay().size() != 0) Thread.sleep(100);

        Thread.sleep(1_500);//To add all entities to db
    }

    public void read(InputStream stream, DefaultHandler handler) throws Exception {
        factory.newSAXParser().parse(stream, handler);
    }

}
