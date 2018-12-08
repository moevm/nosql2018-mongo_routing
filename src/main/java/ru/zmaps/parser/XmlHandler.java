package ru.zmaps.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.zmaps.db.NodeDAO;
import ru.zmaps.db.TipDAO;
import ru.zmaps.db.WayDAO;
import ru.zmaps.parser.entity.*;

@Log4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlHandler extends DefaultHandler {
    @Autowired
    NodeDAO nodeDAO;

    @Autowired
    WayDAO wayDAO;

    @Autowired
    TipDAO tipDAO;


    private Element lastElem = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        parseElem(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("node".equals(qName)) {
            nodeDAO.save((Node) lastElem);
            return;
        }

        if ("way".equals(qName)) {
            wayDAO.save((Way) lastElem);
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

            if (key.equals("name")) {
                tipDAO.save(new Tip(val, lastElem.getId()));
            }

            lastElem.addTag(key, val);

            return;
        }

        if ("nd".equals(qName)) {
            ((Way) lastElem).addNode(nodeDAO.getNodeById(Long.valueOf(attributes.getValue("ref"))));
        }

        if ("way".equals(qName)) {
            long id = Long.parseLong(attributes.getValue("id"));
            lastElem = new Way(id);
        }
    }
}
