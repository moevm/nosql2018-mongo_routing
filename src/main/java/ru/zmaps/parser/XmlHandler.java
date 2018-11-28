package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.zmaps.db.DbUtils;
import ru.zmaps.parser.entity.Element;
import ru.zmaps.parser.entity.Node;
import ru.zmaps.parser.entity.Tag;
import ru.zmaps.parser.entity.Way;

@Log4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlHandler extends DefaultHandler {

    private Element lastElem = null;

    private final DbUtils db = DbUtils.getInstance();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parseElem(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("node".equals(qName)) {
            db.addNode((Node) lastElem);
            return;
        }

        if ("way".equals(qName)) {
            db.addWay((Way) lastElem);
        }
    }

    private void parseElem(String uri, String localName, String qName, Attributes attributes) {
        if ("node".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            double lon = Double.parseDouble(attributes.getValue("lon"));
            double lat = Double.parseDouble(attributes.getValue("lat"));

            lastElem = new Node(lat, lon, id);

            return;
        }

        if ("tag".equals(qName)) {
            String key = attributes.getValue("k");
            String val = attributes.getValue("v");

            lastElem.addTag(new Tag(key, val));

            return;
        }

        if ("nd".equals(qName)) {
            ((Way) lastElem).addNode(db.getNodeById(Long.valueOf(attributes.getValue("ref"))));
        }

        if ("way".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            lastElem = new Way(id);
        }
    }
}
